package com.capibaracode.backend.infraestructure.services;

import com.capibaracode.backend.api.models.requests.PurchaseDetailRequest;
import com.capibaracode.backend.api.models.requests.PurchaseRequest;
import com.capibaracode.backend.api.models.responses.*;
import com.capibaracode.backend.common.CustomAPIResponse;
import com.capibaracode.backend.common.CustomResponseBuilder;
import com.capibaracode.backend.domain.entities.*;
import com.capibaracode.backend.domain.repositories.*;
import com.capibaracode.backend.infraestructure.abstract_services.IPurchaseService;
import com.capibaracode.backend.util.mappers.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class PurchaseServiceImpl implements IPurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final SupplierRepository supplierRepository;

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;
    private  final ProductRepository productRepository;

    private final PurchaseDetailRepository detailRepository;
    private final CustomResponseBuilder responseBuilder;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository, UserRepository userRepository, PaymentRepository paymentRepository, ProductRepository productRepository, PurchaseDetailRepository detailRepository, CustomResponseBuilder responseBuilder) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.detailRepository = detailRepository;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> save(PurchaseRequest request) {
        Purchase purchase = PurchaseMapper.INSTANCE.purchaseFromPurchaseRequest(request);
        SupplierResponse supplierResponse = null;
        PaymentResponse paymentResponse = null;
        UserResponseDTO userResponseDTO = null;
        if (request.getSupplier() != null){
            Supplier supplier = supplierRepository.findById(request.getSupplier()).orElseThrow(()-> new RuntimeException("El proveedor con id " + request.getSupplier()+ " no existe."));
            purchase.setSupplier(supplier);
            supplierResponse = SupplierMapper.INSTANCE.supplierResponseFromSupplier(supplier);
        }
        if (request.getPayment() != null){
            Payment payment = paymentRepository.findById(request.getPayment()).orElseThrow(()-> new RuntimeException("El tipo de pago con id " + request.getPayment()+ " no existe."));
            purchase.setPayment(payment);
            paymentResponse = PaymentMapper.INSTANCE.paymentResponseFromPayment(payment);
        }
        if (request.getUser() != null){
            User user = userRepository.findById(request.getUser()).orElseThrow(()-> new RuntimeException("El usuario con id " + request.getUser()+ " no existe."));
            purchase.setUser(user);
            userResponseDTO = UserMapper.INSTANCE.userResponseDTOFromUser(user);
        }
        purchase.setStatus(request.getStatus());
        List<PurchaseDetail> purchaseDetailList = request.getDetails().stream().map(
                d -> {
                    Product product = productRepository.findById(d.getProduct()).orElseThrow(()-> new RuntimeException("El producto con id " + d.getProduct()+ " no existe."));
                    product.setQuantity(product.getQuantity() + d.getQuantity());
                    productRepository.save(product);
                    PurchaseDetail purchaseDetail = new PurchaseDetail();
                    purchaseDetail.setQuantity(d.getQuantity());
                    purchaseDetail.setDiscount(d.getDiscount());
                    purchaseDetail.setSubtotal(d.getSubtotal());
                    purchaseDetail.setStatus(true);
                    purchaseDetail.setProduct(product);
                    return purchaseDetail;
                }
        ).toList();
        purchase.setDetails(purchaseDetailList);
        PurchaseResponse purchaseResponse = PurchaseMapper.INSTANCE.purchaseResponseFromPurchase(purchaseRepository.save(purchase), supplierResponse, paymentResponse, userResponseDTO);
        return responseBuilder.buildResponse(HttpStatus.CREATED, "Compra creada exitosamente.", purchaseResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAll() {
        List<Purchase> purchaseList = purchaseRepository.findAll();
        List<PurchaseResponse> purchaseResponseList = purchaseList.stream().
                map(purchase -> {
                    SupplierResponse supplierResponse = null;
                    PaymentResponse paymentResponse = null;
                    UserResponseDTO userResponseDTO = null;
                    if (purchase.getSupplier() != null){
                        Supplier supplier = supplierRepository.findById(purchase.getSupplier().getId()).orElseThrow(()-> new RuntimeException("El proveedor con id " + purchase.getSupplier().getId()+ " no existe."));
                        purchase.setSupplier(supplier);
                        supplierResponse = SupplierMapper.INSTANCE.supplierResponseFromSupplier(supplier);
                    }
                    if (purchase.getPayment() != null){
                        Payment payment = paymentRepository.findById(purchase.getPayment().getId()).orElseThrow(()-> new RuntimeException("El tipo de pago con id " + purchase.getPayment().getId()+ " no existe."));
                        purchase.setPayment(payment);
                        paymentResponse = PaymentMapper.INSTANCE.paymentResponseFromPayment(payment);
                    }
                    if (purchase.getUser() != null){
                        User user = userRepository.findById(purchase.getUser().getId()).orElseThrow(()-> new RuntimeException("El usuario con id " + purchase.getUser().getId()+ " no existe."));
                        purchase.setUser(user);
                        userResponseDTO = UserMapper.INSTANCE.userResponseDTOFromUser(user);
                    }
                    return PurchaseMapper.INSTANCE.purchaseResponseFromPurchase(purchase, supplierResponse, paymentResponse, userResponseDTO);
                }).toList();
        return responseBuilder.buildResponse(HttpStatus.OK, "Lista de Compras", purchaseResponseList);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> update(UUID id, PurchaseRequest request) {
        Purchase purchaseToEdit = purchaseRepository.findById(id).orElseThrow(()-> new RuntimeException("La compra con id " + id + " no existe."));
        purchaseToEdit.setSubtotalExcludingIVA(request.getSubtotalExcludingIVA());
        purchaseToEdit.setTotal(request.getTotal());
        purchaseToEdit.setStatus(request.getStatus());
        purchaseToEdit.setIva(request.getIva());
        SupplierResponse supplierResponse = null;
        PaymentResponse paymentResponse = null;
        UserResponseDTO userResponseDTO = null;
        if (request.getSupplier() != null){
            Supplier supplier = supplierRepository.findById(request.getSupplier()).orElseThrow(()-> new RuntimeException("El proveedor con id " + request.getSupplier()+ " no existe."));
            purchaseToEdit.setSupplier(supplier);
            supplierResponse = SupplierMapper.INSTANCE.supplierResponseFromSupplier(supplier);
        }
        if (request.getPayment() != null){
            Payment payment = paymentRepository.findById(request.getPayment()).orElseThrow(()-> new RuntimeException("El tipo de pago con id " + request.getPayment()+ " no existe."));
            purchaseToEdit.setPayment(payment);
            paymentResponse = PaymentMapper.INSTANCE.paymentResponseFromPayment(payment);
        }
        if (request.getUser() != null){
            User user = userRepository.findById(request.getUser()).orElseThrow(()-> new RuntimeException("El usuario con id " + request.getUser()+ " no existe."));
            purchaseToEdit.setUser(user);
            userResponseDTO = UserMapper.INSTANCE.userResponseDTOFromUser(user);
        }
        List<PurchaseDetail> purchaseDetailList = request.getDetails().stream().map(
                detail -> {
                    PurchaseDetail purchaseDetailToEdit = detailRepository.findById(detail.getId()).orElseThrow(()-> new RuntimeException("El detalle de compra con id " + detail.getId() + " no existe."));
                    purchaseDetailToEdit.setSubtotal(detail.getSubtotal());
                    purchaseDetailToEdit.setStatus(detail.getStatus());
                    purchaseDetailToEdit.setDiscount(detail.getDiscount());
                    Product product = productRepository.findById(detail.getProduct()).orElseThrow(()-> new RuntimeException("El producto con id " + detail.getProduct()+ " no existe."));
                    if (purchaseDetailToEdit.getProduct().getId().equals(product.getId()) && !Objects.equals(purchaseDetailToEdit.getQuantity(), detail.getQuantity())){
                        product.setQuantity((product.getQuantity() - purchaseDetailToEdit.getQuantity()) + detail.getQuantity());
                    }if(!purchaseDetailToEdit.getProduct().getId().equals(product.getId())) {
                        Product previousProduct = purchaseDetailToEdit.getProduct();
                        previousProduct.setQuantity(previousProduct.getQuantity() - purchaseDetailToEdit.getQuantity());
                        productRepository.save(previousProduct);
                        product.setQuantity(product.getQuantity() + detail.getQuantity());
                        purchaseDetailToEdit.setProduct(product);
                    }
                    productRepository.save(product);
                    purchaseDetailToEdit.setQuantity(detail.getQuantity());
                    return purchaseDetailToEdit;
                }
        ).toList();
        purchaseToEdit.getDetails().clear();
        purchaseToEdit.getDetails().addAll(purchaseDetailList);
        purchaseRepository.save(purchaseToEdit);
        //PurchaseResponse purchaseResponse = PurchaseMapper.INSTANCE.purchaseResponseFromPurchase(purchaseRepository.save(purchaseToEdit), supplierResponse, paymentResponse, userResponseDTO);
        return responseBuilder.buildResponse(HttpStatus.OK, "Compra actualizada exitosamente.");
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getByID(UUID id) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(()-> new RuntimeException("La compra con id " + id + " no existe."));
        SupplierResponse supplierResponse = null;
        PaymentResponse paymentResponse = null;
        UserResponseDTO userResponseDTO = null;
        if (purchase.getSupplier() != null){
            Supplier supplier = supplierRepository.findById(purchase.getSupplier().getId()).orElseThrow(()-> new RuntimeException("El proveedor con id " + purchase.getSupplier().getId()+ " no existe."));
            purchase.setSupplier(supplier);
            supplierResponse = SupplierMapper.INSTANCE.supplierResponseFromSupplier(supplier);
        }
        if (purchase.getPayment() != null){
            Payment payment = paymentRepository.findById(purchase.getPayment().getId()).orElseThrow(()-> new RuntimeException("El tipo de pago con id " + purchase.getPayment().getId()+ " no existe."));
            purchase.setPayment(payment);
            paymentResponse = PaymentMapper.INSTANCE.paymentResponseFromPayment(payment);
        }
        if (purchase.getUser() != null){
            User user = userRepository.findById(purchase.getUser().getId()).orElseThrow(()-> new RuntimeException("El usuario con id " + purchase.getUser().getId()+ " no existe."));
            purchase.setUser(user);
            userResponseDTO = UserMapper.INSTANCE.userResponseDTOFromUser(user);
        }
        PurchaseResponse purchaseResponse = PurchaseMapper.INSTANCE.purchaseResponseFromPurchase(purchase, supplierResponse, paymentResponse, userResponseDTO);
        return responseBuilder.buildResponse(HttpStatus.OK, "Compra encontrada exitosamente.", purchaseResponse);
    }

    private PurchaseDetail update(UUID id, PurchaseDetailRequest request) {
        PurchaseDetail purchaseDetailToEdit = detailRepository.findById(id).orElseThrow(()-> new RuntimeException("El detalle de compra con id " + id + " no existe."));
        purchaseDetailToEdit.setSubtotal(request.getSubtotal());
        purchaseDetailToEdit.setStatus(request.getStatus());
        purchaseDetailToEdit.setDiscount(request.getDiscount());
        Product product = productRepository.findById(request.getProduct()).orElseThrow(()-> new RuntimeException("El producto con id " + request.getProduct()+ " no existe."));
        if (purchaseDetailToEdit.getProduct().getId().equals(product.getId()) && !Objects.equals(purchaseDetailToEdit.getQuantity(), request.getQuantity())){
            product.setQuantity((product.getQuantity() - purchaseDetailToEdit.getQuantity()) + request.getQuantity());
        }if(!purchaseDetailToEdit.getProduct().getId().equals(product.getId())) {
            Product previousProduct = purchaseDetailToEdit.getProduct();
            previousProduct.setQuantity(previousProduct.getQuantity() - purchaseDetailToEdit.getQuantity());
            productRepository.save(previousProduct);
            product.setQuantity(product.getQuantity() + request.getQuantity());
            purchaseDetailToEdit.setProduct(product);
        }
        productRepository.save(product);
        purchaseDetailToEdit.setQuantity(request.getQuantity());
        return purchaseDetailToEdit;
    }

}
