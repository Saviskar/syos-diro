package main.com.syos.model;

public class ShelfStock {
    private int shelfStockId;
    private int batchId;
    private int qtyOnShelf;

    public ShelfStock(int shelfStockId, int batchId, int qtyOnShelf) {
        this.shelfStockId = shelfStockId;
        this.batchId = batchId;
        this.qtyOnShelf = qtyOnShelf;
    }

    public ShelfStock() {}

    public int getShelfStockId() {
        return shelfStockId;
    }

    public void setShelfStockId(int shelfStockId) {
        this.shelfStockId = shelfStockId;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getQtyOnShelf() {
        return qtyOnShelf;
    }

    public void setQtyOnShelf(int qtyOnShelf) {
        this.qtyOnShelf = qtyOnShelf;
    }
}
