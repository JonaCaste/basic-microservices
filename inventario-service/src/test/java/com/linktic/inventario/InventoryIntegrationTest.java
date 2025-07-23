package com.linktic.inventario;

import com.linktic.inventario.domain.dto.ProductDTO;
import com.linktic.inventario.domain.entity.Inventory;
import com.linktic.inventario.infrastructure.InventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class InventoryIntegrationTest {

    @Value("${spring.security.api-key}")
    private String apiKey;
    @Value("${linktic.microservices.products.url}")
    private String productsMicroserviceUrl;
    @Value("${linktic.microservices.products.create-product.endpoint}")
    private String createProductEndpoint;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private InventoryRepository inventoryRepository;

    @LocalServerPort
    private int port;

    private Long testProductId;
    private String testProductName;

    @BeforeEach
    void setup() {
        ProductDTO mockProduct = new ProductDTO(null, "Producto test inventario", 15000.0, "Descripcion");

        /**
         * Error in product persistence in external microservice
         */
        // Crear producto en el microservicio
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("x-api-key", apiKey);
//        HttpEntity<ProductDTO> request = new HttpEntity<>(mockProduct, headers);
//        String finalUrl = String.format("%s%s", productsMicroserviceUrl, createProductEndpoint);
//
//        System.out.println(finalUrl);
//
//        ResponseEntity<ProductDTO> response = null;
//        try {
//            response = restTemplate.exchange(
//                    finalUrl,
//                    HttpMethod.POST,
//                    request,
//                    ProductDTO.class
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Error al crear producto en microservicio", e);
//        }
//
//        if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException("No se pudo crear el producto de prueba. Response: " + response);
//        }
//
//        ProductDTO productDTO = response.getBody();
//
//        inventoryRepository.save(new Inventory(null, 99L, 50));
//
//        testProductId = 99L;
//        testProductName = "Producto test integration 1";
    }

    /**
     * Error in product persistence in external microservice
     * @throws Exception
     */
//    @Test
//    void testGetInventoryEndpoint() throws Exception {
//        mockMvc
//                .perform(
//                        get(String.format("/inventory/quantity/%s", testProductId))
//                                .header("x-api-key", apiKey)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.productId").value(testProductId))
//                .andExpect(jsonPath("$.quantity").value(50));
//    }

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
        inventoryRepository.deleteAll();
    }

}
