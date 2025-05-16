package main.com.syos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OnlineOrder {
    private int orderId;
    private int userId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
}
