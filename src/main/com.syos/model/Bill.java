package main.com.syos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bill {
    private int billId;
    private LocalDateTime billDate;
    private BigDecimal cashTendered;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal changeAmount;

    public Bill(int billId, LocalDateTime billDate, BigDecimal cashTendered, BigDecimal totalAmount,
                BigDecimal discount, BigDecimal changeAmount) {
        this.billId = billId;
        this.billDate = billDate;
        this.cashTendered = cashTendered;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.changeAmount = changeAmount;
    }

    public Bill() {}

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getCashTendered() {
        return cashTendered;
    }

    public void setCashTendered(BigDecimal cashTendered) {
        this.cashTendered = cashTendered;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }
}
