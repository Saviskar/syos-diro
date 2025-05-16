package main.com.syos.model;

import java.math.BigDecimal;

public class BillLine {
    private int billLineId;
    private int billId;
    private String itemCode;
    private int quantity;
    private BigDecimal lineTotal;

    public BillLine(int billLineId, int billId, String itemCode, int quantity, BigDecimal lineTotal) {
        this.billLineId = billLineId;
        this.billId = billId;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.lineTotal = lineTotal;
    }

    public BillLine() {}

    public int getBillLineId() {
        return billLineId;
    }

    public void setBillLineId(int billLineId) {
        this.billLineId = billLineId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
