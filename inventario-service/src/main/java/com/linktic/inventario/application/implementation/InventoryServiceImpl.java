package com.linktic.inventario.application.implementation;

import com.linktic.inventario.application.interfaces.IInventoryService;
import com.linktic.inventario.domain.dto.InventoryDTO;
import com.linktic.inventario.domain.dto.ProductDTO;
import com.linktic.inventario.domain.entity.Inventory;
import com.linktic.inventario.infrastructure.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class InventoryServiceImpl implements IInventoryService {

    private final Logger logger = Logger.getLogger(InventoryServiceImpl.class.getName());

    @Value("${spring.security.api-key}")
    private String apiKey;

    @Value("${linktic.microservices.products.url}")
    private String productsMicroserviceUrl;
    @Value("${linktic.microservices.products.get-product-by-id.endpoint}")
    private String productByIdEndpoint;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get product from microservice
     * @param id   porduct id
     * @return      product
     */
    private ProductDTO fetchProduct(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        String finalUrl = String.format("%s%s/%s", productsMicroserviceUrl, productByIdEndpoint, id);

        logger.info(String.format("Solicitando producto %s al servicio %s", id, finalUrl));
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                request,
                ProductDTO.class
        );


        if(response == null) return null;

        ProductDTO productDTO = response.getBody();

        if(productDTO == null) return null;

        logger.info(String.format("Producto con id %s obtenido del servicio %s: %s", id, finalUrl, productDTO.toString()));

        return productDTO;
    }

    @Override
    public InventoryDTO getQuantity(Long productId) {
        logger.info(String.format("Obteniendo el inventario del producto %s", productId));
        try {
            ProductDTO productDTO = fetchProduct(productId);
            if (productDTO == null) throw new NoSuchElementException("No se encontro el producto");

            return inventoryRepository.findByProductId(productId)
                    .map(inventory -> new InventoryDTO(inventory.getProductId(), inventory.getQuantity(), productDTO))
                    .orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            logger.severe(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(String.format("Error al obtener el inventario del producto con ID: %s ", productId), e);
        }
    }

    @Override
    public void updateQuantity(Long productId, int quantity) {
        logger.info(String.format("Actualizando el inventario del producto %s", productId));
        try {
            Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(NoSuchElementException::new);
            inventory.setProductId(productId);
            inventory.setQuantity(quantity);
            inventoryRepository.save(inventory);
        } catch (NoSuchElementException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(String.format("No se encontro el inventario para el producto con ID: %s ", productId), e);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(String.format("Error al actualizar el inventario del producto con ID: %s ", productId), e);
        }
    }

}
