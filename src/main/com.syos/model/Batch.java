package main.com.syos.model;

import java.time.LocalDate;

public class Batch {
    private int batchId;
    private String itemCode;
    private int qtyReceived;
    private LocalDate dateReceived;
    private LocalDate expiryDate;

    public Batch(int batchId, String itemCode, int qtyReceived, LocalDate dateReceived, LocalDate expiryDate) {
        this.batchId = batchId;
        this.itemCode = itemCode;
        this.qtyReceived = qtyReceived;
        this.dateReceived = dateReceived;
        this.expiryDate = expiryDate;
    }

    public Batch() {}

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public int getQtyReceived() {
        return qtyReceived;
    }

    public void setQtyReceived(int qtyReceived) {
        this.qtyReceived = qtyReceived;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }
}
