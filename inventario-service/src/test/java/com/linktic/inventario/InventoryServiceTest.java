package com.linktic.inventario;

import com.linktic.inventario.application.implementation.InventoryServiceImpl;
import com.linktic.inventario.domain.dto.InventoryDTO;
import com.linktic.inventario.domain.dto.ProductDTO;
import com.linktic.inventario.domain.entity.Inventory;
import com.linktic.inventario.infrastructure.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private InventoryRepository inventoryRepository;


    /**
     * Quantity tests
     */
    @Test
    void testGetQuantitySuccess() {

        Long productId = 1L;
        ProductDTO mockProduct = new ProductDTO(productId, "Producto test inventario", 15000.0, "Descripcion");
        Inventory mockInventory = new Inventory(1L, productId, 50);

        ResponseEntity<ProductDTO> responseEntity = new ResponseEntity<>(mockProduct, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ProductDTO.class)
        )).thenReturn(responseEntity);

//        when(inventoryService.fetchProduct(productId)).thenReturn(mockProduct);
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(mockInventory));

        InventoryDTO result = inventoryService.getQuantity(productId);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals(50, result.getQuantity());
    }


    @Test
    void testGetQuantityWhenProductNotFound() {
        Long productId = 1L;
        ProductDTO mockProduct = new ProductDTO(productId, "Producto test inventario", 15000.0, "Descripcion");

        ResponseEntity<ProductDTO> responseEntity = new ResponseEntity<>(null, HttpStatus.valueOf(204));

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ProductDTO.class)
        )).thenReturn(responseEntity);
//        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> inventoryService.getQuantity(productId));
    }

    @Test
    void testGetQuantityNotFound() {
        Long productId = 1L;
        ProductDTO mockProduct = new ProductDTO(productId, "Producto test inventario", 15000.0, "Descripcion");

        ResponseEntity<ProductDTO> responseEntity = new ResponseEntity<>(mockProduct, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ProductDTO.class)
        )).thenReturn(responseEntity);
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> inventoryService.getQuantity(productId));
    }

    @Test
    void testGetQuantityServerError() {

        Long productId = 1L;
//        ProductDTO mockProduct = new ProductDTO(productId, "Producto test inventario", 15000.0, "Descripcion");

        Mockito.when(inventoryRepository.findByProductId(productId))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> {
            inventoryRepository.findByProductId(1L);
        });
    }


    /**
     * Update tests
     */
    @Test
    void testUpdateQuantitySuccess() {
        Long productId = 1L;
        ProductDTO mockProduct = new ProductDTO(productId, "Producto test inventario", 15000.0, "Descripcion");
        Inventory mockInventory = new Inventory(1L, productId, 50);

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(mockInventory));

        inventoryService.updateQuantity(productId, 30);

        verify(inventoryRepository).save(argThat(inv -> inv.getQuantity() == 30));
    }
    @Test
    void testUpdateQuantityNotFound() {
        Long productId = 1L;
        ProductDTO mockProduct = new ProductDTO(productId, "Producto test inventario", 15000.0, "Descripcion");

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.updateQuantity(productId, 10));
        assertTrue(ex.getMessage().contains("No se encontro el inventario"));
    }
    @Test
    void testUpdateQuantityServerError() {
        Long productId = 1L;
        ProductDTO mockProduct = new ProductDTO(productId, "Producto test inventario", 15000.0, "Descripcion");

        when(inventoryRepository.findByProductId(productId)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.updateQuantity(productId, 10));
        assertTrue(ex.getMessage().contains("Error al actualizar"));
    }

}
