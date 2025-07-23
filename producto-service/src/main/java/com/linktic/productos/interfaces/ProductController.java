package com.linktic.productos.interfaces;

import com.linktic.productos.application.interfaces.IProductService;
import com.linktic.productos.domain.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(productService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listAll() {
        return ResponseEntity.ok(productService.listAll());
    }

}
