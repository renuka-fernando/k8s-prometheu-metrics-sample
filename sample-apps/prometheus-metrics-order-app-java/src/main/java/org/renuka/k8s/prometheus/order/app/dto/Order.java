package org.renuka.k8s.prometheus.order.app.dto;

import java.util.Date;

public class Order {
    Integer orderId;
    Integer productId;
    Double quanity;
    Date shipDate;
    String status;
    Boolean completed;

    public Order(Integer orderId, Integer productId, Double quanity, Date shipDate, String status, Boolean completed) {
        this.orderId = orderId;
        this.productId = productId;
        this.quanity = quanity;
        this.shipDate = shipDate;
        this.status = status;
        this.completed = completed;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getQuanity() {
        return quanity;
    }

    public void setQuanity(Double quanity) {
        this.quanity = quanity;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
