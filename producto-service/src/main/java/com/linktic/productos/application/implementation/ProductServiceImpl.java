package com.linktic.productos.application.implementation;

import com.linktic.productos.application.interfaces.IProductService;
import com.linktic.productos.domain.dto.ProductDTO;
import com.linktic.productos.domain.entity.Product;
import com.linktic.productos.infrastructure.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class ProductServiceImpl implements IProductService {

    private final Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDTO create(ProductDTO dto) {
        logger.info(String.format("Creando el producto %s", dto));
        try {
            Product product = new Product();
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());

            Product saved = productRepository.save(product);
            dto.setId(saved.getId());
            return dto;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Error al crear el producto", e);
        }
    }

    @Override
    public ProductDTO get(Long id) {
        logger.info(String.format("Buscando el producto %s", id));
        try {
            return productRepository.findById(id)
                    .map(product -> new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDescription()))
                    .orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            logger.severe(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(String.format("Error al obtener el producto con ID: %s ", id), e);
        }
    }

    @Override
    public List<ProductDTO> listAll() {

        logger.info("Buscando todos los productos");
        try {
            List<ProductDTO> products = productRepository.findAll().stream()
                    .map(p -> new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getDescription()))
                    .toList();

            if (products.isEmpty()) {
                throw new NoSuchElementException();
            }

            return products;
        } catch (NoSuchElementException e) {
            logger.severe(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Error al obtener los productos", e);
        }
    }
}
