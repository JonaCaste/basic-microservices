package com.linktic.productos.application.interfaces;

import com.linktic.productos.domain.dto.ProductDTO;

import java.util.List;

public interface IProductService {

    /**
     * create product in model
     * @param dto   product info
     * @return      product dto
     */
    ProductDTO create(ProductDTO dto);

    /**
     * get product by id
     * @param id    id product to search
     * @return      product dto
     */
    ProductDTO get(Long id);

    /**
     * Get all products
     * @return  list of products
     */
    List<ProductDTO> listAll();

}
