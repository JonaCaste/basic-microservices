package com.linktic.inventario;

import com.jayway.jsonpath.JsonPath;
import com.linktic.inventario.domain.dto.ProductDTO;
import com.linktic.inventario.domain.entity.Inventory;
import com.linktic.inventario.infrastructure.InventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @Test
    void testBuyProductEndpoint() throws Exception {


        /**
         * TODO create product in microservice
         * productId hardcode
         */
        Long productId = 18L;
        ProductDTO productDTOReq = new ProductDTO(productId,"Laptop", 1000.0, "Descripcion");

        ResponseEntity<ProductDTO> responseEntity = new ResponseEntity<>(productDTOReq, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ProductDTO.class)
        )).thenReturn(responseEntity);

        mockMvc
                .perform(put(String.format("/inventory/quantity/%s", productId)) //Dinamyc prop (spring.mvc.servlet.path) is not loaded this
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("quantity", String.valueOf(100))
                        .header("x-api-key", apiKey))
                .andExpect(status().isOk());

        String purchaseJson = String.format("{\n" +
                "            \"productId\": %d, \n" +
                "            \"quantity\": 50\n" +
                "        }", productId);

        mockMvc
                .perform(post("/purchase") //Dinamyc prop (spring.mvc.servlet.path) is not loaded this
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchaseJson)
                        .header("x-api-key", apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.purchasedQuantity").value(50))
                .andExpect(jsonPath("$.remainingQuantity").value(50));
    }

    @Test
    public void shouldThrowUnauthorized() throws Exception {

        mockMvc
                .perform(
                        get("/inventory/quantity/1")    //Dinamyc prop (spring.mvc.servlet.path) is not loaded this
                                .header("x-api-key", "12345-6789")
                )
                .andExpect(status().isUnauthorized());
    }

    @AfterEach
    public void finish() {
        inventoryRepository.deleteAll();
    }

}
