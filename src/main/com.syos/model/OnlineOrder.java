package main.com.syos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OnlineOrder {
    private int orderId;
    private int userId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;

    public OnlineOrder(int orderId, int userId, LocalDateTime orderDate, String status, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public OnlineOrder() {}

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
