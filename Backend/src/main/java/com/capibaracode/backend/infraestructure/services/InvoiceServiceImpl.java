package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.InvoiceRequest;
import com.capibaracode.backend.api.models.responses.*;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.*;
import com.capibaracode.backend.domain.repositories.*;
import com.capibaracode.backend.infraestructure.abstract_services.IInvoiceService;
import com.capibaracode.backend.util.mappers.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InvoiceServiceImpl implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final InvoiceSerialRepository invoiceSerialRepository;
    private final CompanyRepository companyRepository;

    private final CustomResponseBuilder responseBuilder;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, UserRepository userRepository, ClientRepository clientRepository, ProductRepository productRepository, InvoiceSerialRepository invoiceSerialRepository, CompanyRepository companyRepository, CustomResponseBuilder responseBuilder) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.invoiceSerialRepository = invoiceSerialRepository;
        this.companyRepository = companyRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> createInvoice(InvoiceRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Client client = clientRepository.findById(request.getClient()).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        User user = userRepository.findById(request.getUser()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Payment payment = paymentRepository.findById(request.getPayment()).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        Invoice invoice = new Invoice();
        LocalDate now = LocalDate.now();
        InvoiceSerial invoiceSerial = invoiceSerialRepository.findById(1L).get();
        String establishment = invoiceSerial.getSerial().substring(0, 3);
        String emissionPoint = invoiceSerial.getSerial().substring(3, 6);
        String sequential = invoiceSerial.getSequential();
        invoice.setInvoiceNumber(establishment + "-" + emissionPoint + "-" + sequential);
        Integer newSequential = Integer.parseInt(sequential) + 1;
        invoiceSerial.setSequential(String.format("%09d", newSequential));
        invoiceSerialRepository.save(invoiceSerial);
        String accessKey = "";
        String date = now.format(formatter).replace("-", "");
        String type = "01";
        String ruc = user.getCompany().getRuc();
        String environment = "1";
        String sequentialNumber = establishment + emissionPoint + sequential;
        String numericCode = "12345678";
        String emissionType = "1";
        accessKey = date + type + ruc + environment + sequentialNumber + numericCode + emissionType;
        accessKey = generateVerificationDigit(accessKey);
        System.out.println(accessKey.length());
        invoice.setKeyAccess(accessKey);
        invoice.setClient(client);
        invoice.setIssueDate(now);
        invoice.setUser(user);
        invoice.setPayment(payment);
        invoice.setIva(request.getIva());
        invoice.setSubtotalExcludingIVA(request.getSubtotalExcludingIVA());
        invoice.setTotal(request.getTotal());
        invoice.setDiscount(request.getDiscount());
        invoice.setDescription(request.getDescription());
        List<InvoiceDetail> details = request.getDetails().stream()
                .map(detail -> {
                            InvoiceDetail invoiceDetail = new InvoiceDetail();
                            invoiceDetail.setDiscount(detail.getDiscount());
                            invoiceDetail.setPrice(detail.getPrice());
                            invoiceDetail.setQuantity(detail.getQuantity());
                            invoiceDetail.setSubtotal(detail.getSubtotal());
                            Product product = productRepository
                                    .findById(detail.getProduct())
                                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                            if(product.getQuantity() < detail.getQuantity())
                                throw new RuntimeException("No hay suficiente stock");
                            product.setQuantity(product.getQuantity() - detail.getQuantity());
                            productRepository.save(product);
                            invoiceDetail.setProduct(product);
                            invoiceDetail.setInvoice(invoice);
                            return invoiceDetail;
                        }
                ).toList();
        invoice.setDetails(details);
        UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userResponseDTOFromUser(user);
        ClientResponse clientResponse = ClientMapper.INSTANCE.clientToClientResponse(client);
        Invoice invoiceFromDB = invoiceRepository.save(invoice);
        List<InvoiceDetailsResponse> detailsResponse = invoiceFromDB.getDetails()
                .stream()
                .map(invoiceDetail -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE
                            .productResponseFromProductWithoutRelations(invoiceDetail.getProduct());
                     return InvoiceDetailMapper.INSTANCE
                             .invoiceDetailsResponseFromInvoiceDetail(invoiceDetail, productResponse);
                })
                .toList();
        InvoiceResponse invoiceResponse = InvoiceMapper.INSTANCE
                .invoiceResponseFromInvoice(invoiceFromDB, detailsResponse, userResponseDTO, clientResponse);
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Factura creada con exito", invoiceResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findAll() {
        List<Invoice> invoices = invoiceRepository.findAll();
        List<InvoiceResponse> invoiceResponses = invoices.stream()
                .map(invoice -> {
                    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userResponseDTOFromUser(invoice.getUser());
                    ClientResponse clientResponse = ClientMapper.INSTANCE.clientToClientResponse(invoice.getClient());
                    List<InvoiceDetailsResponse> detailsResponse = invoice.getDetails()
                            .stream()
                            .map(invoiceDetail -> {
                                ProductResponse productResponse = ProductMapper.INSTANCE
                                        .productResponseFromProductWithoutRelations(invoiceDetail.getProduct());
                                return InvoiceDetailMapper.INSTANCE
                                        .invoiceDetailsResponseFromInvoiceDetail(invoiceDetail, productResponse);
                            })
                            .toList();
                    return InvoiceMapper.INSTANCE
                            .invoiceResponseFromInvoice(invoice, detailsResponse, userResponseDTO, clientResponse);
                })
                .toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Listado de facturas", invoiceResponses);
    }

    private String  generateVerificationDigit(String key){
        int factor = 2;
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            int digit = Integer.parseInt(key.substring(i, i + 1));
            sum += (digit * factor);
            factor = (factor == 7) ? 2 : ++factor;
        }
        int verificationDigit = 11 - (sum % 11);
        key += verificationDigit;
        return key;
    }
}
