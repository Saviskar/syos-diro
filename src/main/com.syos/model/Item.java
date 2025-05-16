package main.com.syos.model;

import java.math.BigDecimal;

public class Item {
    private String itemCode;
    private String name;
    private BigDecimal unitPrice;
    private int reorderLevel;

    public Item(String itemCode, String name, BigDecimal unitPrice, int reorderLevel) {
        this.itemCode = itemCode;
        this.name = name;
        this.unitPrice = unitPrice;
        this.reorderLevel = reorderLevel;
    }

    public Item () {}

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
}
