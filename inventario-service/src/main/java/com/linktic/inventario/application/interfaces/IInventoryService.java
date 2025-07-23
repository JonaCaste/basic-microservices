package com.linktic.inventario.application.interfaces;

import com.linktic.inventario.domain.dto.InventoryDTO;
import com.linktic.inventario.domain.dto.ProductDTO;
import com.linktic.inventario.domain.dto.PurchaseResponseDTO;

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

    /**
     * Buy product, with availability validation and inventory discount
     * @param productId     product id
     * @param quantity      quantity of product to buy
     * @return              productId, quantity, purchasedQuantity, remainingQuantity (PurchaseResponseDTO);
     */
    PurchaseResponseDTO purchaseProduct(Long productId, int quantity);
}
