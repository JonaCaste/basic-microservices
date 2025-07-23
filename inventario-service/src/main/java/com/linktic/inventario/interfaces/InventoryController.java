package com.linktic.inventario.interfaces;

import com.linktic.inventario.application.interfaces.IInventoryService;
import com.linktic.inventario.domain.dto.InventoryDTO;
import com.linktic.inventario.domain.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;

    @GetMapping("/quantity/{id}")
    public ResponseEntity<InventoryDTO> getQuantity(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getQuantity(id));
    }

    @PutMapping("/quantity/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestParam int quantity) {
        inventoryService.updateQuantity(id, quantity);
        return ResponseEntity.ok(null);
    }
}
