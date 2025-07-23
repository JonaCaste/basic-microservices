package com.linktic.inventario.domain.dto;

public class PurchaseResponseDTO {

    private Long productId;
    private String productName;
    private int purchasedQuantity;
    private int remainingQuantity;

    public PurchaseResponseDTO() {
    }

    public PurchaseResponseDTO(Long productId, String productName, int purchasedQuantity, int remainingQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.purchasedQuantity = purchasedQuantity;
        this.remainingQuantity = remainingQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(int purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }
}
