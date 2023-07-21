package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.InvoiceRequest;
import com.capibaracode.backend.api.models.responses.*;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.*;
import com.capibaracode.backend.domain.repositories.*;
import com.capibaracode.backend.infraestructure.abstract_services.IInvoiceService;
import com.capibaracode.backend.util.enums.IdentificationType;
import com.capibaracode.backend.util.enums.InvoiceStatus;
import com.capibaracode.backend.util.mappers.*;
import com.capibaracode.backend.util.xml.InvoiceXMLModel;
import com.capibaracode.backend.util.xml.XMLUtils;
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
        String date = now.format(formatter).replace("/", "");
        String type = "01";
        String ruc = user.getCompany().getRuc();
        String environment = "1";
        String sequentialNumber = establishment + emissionPoint + sequential;
        String numericCode = "12345678";
        String emissionType = "1";
        accessKey = date + type + ruc + environment + sequentialNumber + numericCode + emissionType;
        accessKey = accessKey+generateVerificationDigit(accessKey);
        System.out.println(accessKey.length());
        invoice.setKeyAccess(accessKey);
        invoice.setClient(client);
        invoice.setIssueDate(now);
        invoice.setStatus(InvoiceStatus.PENDIENTE);
        invoice.setUser(user);
        invoice.setPayment(payment);
        invoice.setIva(request.getIva());
        invoice.setSubtotalExcludingIVA(request.getSubtotalExcludingIVA());
        invoice.setTotal(request.getTotal());
        invoice.setDiscount(request.getDiscount());
        invoice.setDescription(request.getDescription());

        List<InvoiceXMLModel.Detalles.Detalle> detailsXml = request.getDetails().stream()
                .map(detail -> {
                            InvoiceXMLModel.Detalles.Detalle invoiceDetail = new InvoiceXMLModel.Detalles.Detalle();
                            invoiceDetail.setDescuento(String.format("%12.2f", detail.getDiscount()).trim().replace(",", "."));
                            invoiceDetail.setPrecioUnitario(String.format("%12.2f", detail.getPrice()).trim().replace(",", "."));
                            invoiceDetail.setCantidad(String.format("%12.2f", (double) detail.getQuantity()).trim().replace(",", "."));
                            invoiceDetail.setPrecioTotalSinImpuesto(String.format("%12.2f", detail.getSubtotalExcludingIVA()).trim().replace(",", "."));
                            Product product = productRepository
                                    .findById(detail.getProduct())
                                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                            invoiceDetail.setCodigoPrincipal(product.getCode());
                            invoiceDetail.setDescripcion(product.getName());
                            InvoiceXMLModel.Detalles.Detalle.Impuestos impuestos = new InvoiceXMLModel.Detalles.Detalle.Impuestos();


                            if (product.getCategory().getTax().getPercentage().equals(0d)) {
                                invoiceDetail.setImpuestos(new InvoiceXMLModel.Detalles.Detalle.Impuestos());
                                InvoiceXMLModel.Detalles.Detalle.Impuestos.Impuesto impuesto0 = new InvoiceXMLModel.Detalles.Detalle.Impuestos.Impuesto();
                                impuesto0.setBaseImponible(String.format("%12.2f", detail.getSubtotalExcludingIVA()).trim().replace(",", "."));
                                impuesto0.setCodigo("2");
                                impuesto0.setCodigoPorcentaje("0");
                                impuesto0.setTarifa("0");

                                impuesto0.setValor(String.format("%12.2f", detail.getSubtotal()).trim().replace(",", "."));
                                impuestos.getImpuesto().add(impuesto0);
                            }
                            if (product.getCategory().getTax().getPercentage().equals(12d)) {
                                InvoiceXMLModel.Detalles.Detalle.Impuestos.Impuesto impuesto12 = new InvoiceXMLModel.Detalles.Detalle.Impuestos.Impuesto();
                                impuesto12.setBaseImponible(String.format("%12.2f", detail.getSubtotalExcludingIVA()).trim().replace(",", "."));
                                impuesto12.setCodigo("2");
                                impuesto12.setCodigoPorcentaje("2");
                                impuesto12.setTarifa("12");
                                impuesto12.setValor(String.format("%12.2f", detail.getSubtotal()).trim().replace(",", "."));
                                impuestos.getImpuesto().add(impuesto12);
                            }
                            invoiceDetail.setImpuestos(impuestos);
                            return invoiceDetail;
                        }
                ).toList();

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
                            if (product.getQuantity() < detail.getQuantity())
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



        InvoiceXMLModel xmlModel = new InvoiceXMLModel();
        xmlModel.setId("comprobante");
        xmlModel.setVersion("2.1.0");
        InvoiceXMLModel.InfoTributaria infoTributaria = new InvoiceXMLModel.InfoTributaria();
        infoTributaria.setAmbiente("1");
        infoTributaria.setTipoEmision("1");
        infoTributaria.setRazonSocial(user.getCompany().getName());
        infoTributaria.setNombreComercial(user.getCompany().getName());
        infoTributaria.setRuc(user.getCompany().getRuc().length() == 9 ? user.getCompany().getRuc() + "001" : user.getCompany().getRuc());
        infoTributaria.setClaveAcceso(accessKey);
        infoTributaria.setCodDoc("01");
        infoTributaria.setEstab(establishment);
        infoTributaria.setPtoEmi(emissionPoint);
        infoTributaria.setSecuencial(sequential);
        infoTributaria.setDirMatriz(user.getCompany().getAddress());
        xmlModel.setInfoTributaria(infoTributaria);

        InvoiceXMLModel.InfoFactura infoFactura = new InvoiceXMLModel.InfoFactura();
        infoFactura.setFechaEmision(now.format(formatter));
        infoFactura.setDirEstablecimiento(user.getCompany().getAddress());
        infoFactura.setContribuyenteEspecial("536");

        infoFactura.setObligadoContabilidad(user.getCompany().getAccounting() ? "SI" : "NO");


        infoFactura.setTipoIdentificacionComprador(client.getIdentification()
                .equals("9999999999999") ? "07" : getIdentificationCode(client.getIdentificationType()));
        infoFactura.setGuiaRemision(establishment + "-" + emissionPoint + "-" + sequential);
        infoFactura.setRazonSocialComprador(client.getFullname());
        infoFactura.setIdentificacionComprador(client.getIdentification());
        infoFactura.setDireccionComprador(client.getAddress());
        infoFactura.setTotalSinImpuestos(request.getSubtotalExcludingIVA().toString());
        infoFactura.setTotalDescuento(request.getDiscount().toString());


        InvoiceXMLModel.InfoFactura.TotalConImpuestos totalConImpuestos = new InvoiceXMLModel.InfoFactura.TotalConImpuestos();
        if (request.getIva().equals(0d)) {
            InvoiceXMLModel.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuestoIVA0 = new InvoiceXMLModel.InfoFactura.TotalConImpuestos.TotalImpuesto();
            totalImpuestoIVA0.setCodigo("2");
            totalImpuestoIVA0.setCodigoPorcentaje("0");
            totalImpuestoIVA0.setDescuentoAdicional(String.format("%12.2f", request.getIva()).trim().replace(",", "."));
            totalImpuestoIVA0.setBaseImponible(String.format("%12.2f", request.getSubtotalExcludingIVA()).trim().replace(",", "."));
            totalImpuestoIVA0.setValor(String.format("%12.2f", request.getSubtotalExcludingIVA()).trim().replace(",", "."));
            totalConImpuestos.getTotalImpuesto().add(totalImpuestoIVA0);
        } else {
            InvoiceXMLModel.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuestoIVA12 = new InvoiceXMLModel.InfoFactura.TotalConImpuestos.TotalImpuesto();
            totalImpuestoIVA12.setCodigo("2");
            totalImpuestoIVA12.setCodigoPorcentaje("2");
            totalImpuestoIVA12.setBaseImponible(String.format("%12.2f", request.getSubtotalExcludingIVA()).trim().replace(",", "."));
            totalImpuestoIVA12.setDescuentoAdicional(String.format("%12.2f", request.getIva()).trim().replace(",", "."));
            totalImpuestoIVA12.setValor(String.format("%12.2f", request.getTotal()).trim().replace(",", "."));
            totalConImpuestos.getTotalImpuesto().add(totalImpuestoIVA12);
        }

        infoFactura.setPropina("0.00");
        infoFactura.setImporteTotal(request.getTotal().toString());
        infoFactura.setMoneda("DOLAR");


        infoFactura.setTotalConImpuestos(totalConImpuestos);
        InvoiceXMLModel.InfoFactura.Pagos.Pago pago = new InvoiceXMLModel.InfoFactura.Pagos.Pago();
        pago.setFormaPago(paymentCode(payment));
        pago.setTotal(String.format("%12.2f", request.getTotal()).trim().replace(",", "."));
        pago.setPlazo("0");
        pago.setUnidadTiempo("dias");
        InvoiceXMLModel.InfoFactura.Pagos pagos = new InvoiceXMLModel.InfoFactura.Pagos();
        pagos.getPago().add(pago);
        infoFactura.setPagos(pagos);
        xmlModel.setInfoFactura(infoFactura);

        InvoiceXMLModel.Detalles detalles = new InvoiceXMLModel.Detalles();
        detalles.setDetalle(detailsXml);
        xmlModel.setDetalles(detalles);

        String xml = XMLUtils.createXML(xmlModel, accessKey);
        invoice.setXml(xml);
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

    @Override
    public ResponseEntity<CustomAPIResponse<?>> findById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
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
        InvoiceResponse invoiceResponse = InvoiceMapper.INSTANCE.invoiceResponseFromInvoice(invoice, detailsResponse, userResponseDTO, clientResponse);
        return responseBuilder.buildResponse(HttpStatus.OK, "Factura encontrada", invoiceResponse);
    }

    private String generateVerificationDigit(String key) {
        int baseMultiplicador = 7;
        int[] resultados = new int[key.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador = 0;
        for (int i = resultados.length - 1; i >= 0; i--) {
            resultados[i] = Integer.parseInt(Character.toString(key.charAt(i)));
            resultados[i] = resultados[i] * multiplicador;
            multiplicador++;
            if (multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
            total += resultados[i];
        }
        if (total == 0 || total == 1) {
            verificador = 0;
        } else {
            verificador = (11 - (total % 11)) == 11 ? 0 : (11 - (total % 11));
        }
        if (verificador == 10) {
            verificador = 1;
        }
        return String.valueOf(verificador);
    }

    private String paymentCode(Payment payment) {
        switch (payment.getPayment()) {
            case "SIN UTILIZACION DEL SISTEMA FINANCIERO":
                return "01";
            case "COMPENSACIÓN DE DEUDAS":
                return "15";
            case "TARJETA DE DÉBITO":
                return "16";
            case "DINERO ELECTRÓNICO":
                return "17";
            case "TARJETA PREPAGO":
                return "18";
            case "TARJETA DE CRÉDITO":
                return "19";
            case "OTROS CON UTILIZACIÓN DEL SISTEMA FINANCIERO":
                return "20";
            case "ENDOSO DE TÍTULOS":
                return "21";
            default:
                return "01";

        }
    }

    private String getIdentificationCode(IdentificationType identificationType) {
        switch (identificationType) {
            case CEDULA:
                return "05";
            case RUC:
                return "04";
            case PASAPORTE:
                return "06";
            default:
                return "07";
        }
    }


}
