package com.linktic.productos;

import com.linktic.productos.application.implementation.ProductServiceImpl;
import com.linktic.productos.application.interfaces.IProductService;
import com.linktic.productos.domain.dto.ProductDTO;
import com.linktic.productos.domain.entity.Product;
import com.linktic.productos.infrastructure.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    /**
     * get tests
     */
    @Test
    public void shouldReturnProductWhenIdExists() {
        Product product = new Product(1L, "Producto test 1", 5000.0, "Description");
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.get(1L); //Working with dto

        assertEquals("Producto test 1", result.getName());
    }

    @Test
    public void shouldThrowExceptionWhenProductNotFound() {
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> productService.get(2L));
    }

    @Test
    public void shouldThrowExceptionWhenRepositoryFails() {
        Mockito.when(productRepository.findById(1L))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> {
            productService.get(1L);
        });
    }


    /**
     * Create test
     */
    @Test
    public void shouldCreateProductSuccessfully() {

        //entrance dto
        ProductDTO dto = new ProductDTO();
        dto.setName("Producto test create");
        dto.setPrice(1000.0);
        dto.setDescription("Descripcion test");

        Product savedProduct = new Product(1L, dto.getName(), dto.getPrice(), dto.getDescription());
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);

        ProductDTO result = productService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getPrice(), result.getPrice());
        assertEquals(dto.getDescription(), result.getDescription());
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenRepositoryFailsInCreateMethod() {
        //entrance dto
        ProductDTO dto = new ProductDTO();
        dto.setName("Producto test create");
        dto.setPrice(1000.0);
        dto.setDescription("Descripcion test");

        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            productService.create(dto);
        });

        assertEquals("Error al crear el producto", thrown.getMessage());
    }


    /**
     * List all tests
     */
    @Test
    public void shouldReturnListOfProductsWhenRepositoryIsNotEmpty() {
        List<Product> productList = List.of(
                new Product(1L, "Producto 1", 1000.0, "descripcion"),
                new Product(2L, "Producto 2", 2000.0, "descripcion")
        );

        Mockito.when(productRepository.findAll()).thenReturn(productList);

        List<ProductDTO> result = productService.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Producto 1", result.get(0).getName());
        assertEquals("Producto 2", result.get(1).getName());
    }

    @Test
    public void shouldThrowNoSuchElementExceptionWhenRepositoryReturnsEmptyList() {
        Mockito.when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class, () -> {
            productService.listAll();
        });
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenRepositoryFailsInListAllMethod() {
        Mockito.when(productRepository.findAll()).thenThrow(new RuntimeException("DB Error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            productService.listAll();
        });

        assertEquals("Error al obtener los productos", thrown.getMessage());
        assertNotNull(thrown.getCause());
        assertEquals("DB Error", thrown.getCause().getMessage());
    }




}
