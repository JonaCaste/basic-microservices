package com.linktic.inventario.interfaces;

import com.linktic.inventario.application.interfaces.IInventoryService;
import com.linktic.inventario.domain.dto.PurchaseRequestDTO;
import com.linktic.inventario.domain.dto.PurchaseResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private IInventoryService inventoryService;

    @PostMapping
    public ResponseEntity<?> purchase(@RequestBody PurchaseRequestDTO request) {
            return ResponseEntity.ok(inventoryService.purchaseProduct(request.getProductId(), request.getQuantity()));
    }
}
