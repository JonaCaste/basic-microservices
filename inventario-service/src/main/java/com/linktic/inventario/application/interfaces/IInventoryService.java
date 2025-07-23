package com.linktic.inventario.application.interfaces;

import com.linktic.inventario.domain.dto.InventoryDTO;
import com.linktic.inventario.domain.dto.ProductDTO;

public interface IInventoryService {

    /**
     * get quantity of the product from inventory
     * @param productId     product id
     * @return              quantity
     */
    InventoryDTO getQuantity(Long productId);

    /**
     * update quantity of products in inventory
     * @param productId     product id
     * @param quantity      new quantity
     */
    void updateQuantity(Long productId, int quantity);
}
