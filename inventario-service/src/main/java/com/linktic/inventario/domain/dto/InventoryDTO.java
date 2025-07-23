package com.linktic.inventario.domain.dto;

public class InventoryDTO {

    private Long productId;
    private Integer quantity;
    private ProductDTO producto;

    public InventoryDTO() {
    }

    public InventoryDTO(Long productId,Integer quantity, ProductDTO producto) {
        this.productId = productId;
        this.quantity = quantity;
        this.producto = producto;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductDTO producto) {
        this.producto = producto;
    }
}
