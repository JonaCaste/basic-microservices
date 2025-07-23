package com.linktic.productos;

import com.linktic.productos.domain.entity.Product;
import com.linktic.productos.infrastructure.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Value("${spring.security.api-key}")
    private String apiKey;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        productRepository.save(new Product(null, "Product test integration 1", 15000.0, "descripcion" ));
        productRepository.save(new Product(null, "Product test integration 2", 25000.0, "descripcion" ));
    }

    @Test
    public void shouldReturnAllProducts() throws Exception {

        mockMvc
                .perform(
                        get("/products")    //Dinamyc prop (spring.mvc.servlet.path) is not loaded this
                                .header("x-api-key", apiKey)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void shouldThrowUnauthorized() throws Exception {

        mockMvc
                .perform(
                        get("/products")    //Dinamyc prop (spring.mvc.servlet.path) is not loaded this
                                .header("x-api-key", "12345-6789")
                )
                .andExpect(status().isUnauthorized());
    }

    @AfterEach
    public void finish() {
        productRepository.deleteAll();
    }

}
