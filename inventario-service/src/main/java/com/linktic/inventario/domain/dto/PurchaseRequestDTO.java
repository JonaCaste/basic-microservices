package com.linktic.inventario.domain.dto;

public class PurchaseRequestDTO {

    private Long productId;
    private int quantity;

    public PurchaseRequestDTO() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
