package main.com.syos.service;

import main.com.syos.model.Bill;
import main.com.syos.model.BillLine;
import main.com.syos.model.Batch;
import main.com.syos.model.Item;
import main.com.syos.model.ShelfStock;
import main.com.syos.repository.BillDAO;
import main.com.syos.repository.BillLineDAO;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.ShelfStockDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Use case for processing an over-the-counter checkout:
 *   1. Calculate totals, persist Bill and BillLines
 *   2. Decrement on-shelf stock by consuming batches in expiry/FIFO order
 *   3. Return the final Bill with change, etc.
 */
public class ProcessCheckoutUseCase {
    private final ItemDAO itemDAO;
    private final BillDAO billDAO;
    private final BillLineDAO billLineDAO;
    private final ShelfStockDAO shelfStockDAO;
    private final BatchDAO batchDAO;

    public ProcessCheckoutUseCase(
            ItemDAO itemDAO,
            BillDAO billDAO,
            BillLineDAO billLineDAO,
            ShelfStockDAO shelfStockDAO,
            BatchDAO batchDAO
    ) {
        this.itemDAO       = itemDAO;
        this.billDAO       = billDAO;
        this.billLineDAO   = billLineDAO;
        this.shelfStockDAO = shelfStockDAO;
        this.batchDAO      = batchDAO;
    }

    public Bill execute(Map<String,Integer> items,
                        BigDecimal cashTendered) throws SQLException {
        // 1. Build and persist the Bill header
        BigDecimal total = calculateTotal(items);
        Bill bill = new Bill();
        bill.setBillDate(LocalDateTime.now());
        bill.setTotalAmount(total);
        bill.setCashTendered(cashTendered);
        bill.setDiscount(BigDecimal.ZERO);
        bill.setChangeAmount(cashTendered.subtract(total));
        billDAO.createBill(bill);

        // 2. Persist each line, then decrement stock
        for (Map.Entry<String,Integer> entry : items.entrySet()) {
            String code = entry.getKey();
            int    qty  = entry.getValue();

            // a) lookup item and compute line total
            Item product = itemDAO.findByCode(code);
            BigDecimal lineTotal = product.getUnitPrice()
                    .multiply(BigDecimal.valueOf(qty));

            // b) persist BillLine
            BillLine line = new BillLine();
            line.setBillId(bill.getBillId());
            line.setItemCode(code);
            line.setQuantity(qty);
            line.setLineTotal(lineTotal);
            billLineDAO.createBillLine(line);

            // c) decrement on‐shelf stock
            decrementShelfStock(code, qty);
        }

        return bill;
    }

    private BigDecimal calculateTotal(Map<String,Integer> items) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String,Integer> entry : items.entrySet()) {
            Item item = itemDAO.findByCode(entry.getKey());
            total = total.add(
                    item.getUnitPrice().multiply(BigDecimal.valueOf(entry.getValue()))
            );
        }
        return total;
    }

    /**
     * Decrements on-shelf stock for the given itemCode by the given quantity.
     * Chooses batches by nearest-expiry, then by date received (FIFO).
     */
    private void decrementShelfStock(String itemCode, int qty) throws SQLException {
        // 1) load all batches for this code, sorted by expiry then dateReceived
        List<Batch> batches = batchDAO.findByItemCode(itemCode);
        batches.sort(Comparator
                .comparing(Batch::getExpiryDate)
                .thenComparing(Batch::getDateReceived)
        );

        int remaining = qty;
        // 2) iterate batches and their shelf-stock entries
        for (Batch batch : batches) {
            List<ShelfStock> stocks = shelfStockDAO.findByBatchId(batch.getBatchId());
            for (ShelfStock stock : stocks) {
                int available = stock.getQtyOnShelf();
                if (available <= 0) continue;

                int toTake = Math.min(available, remaining);
                int newQty  = available - toTake;
                shelfStockDAO.updateQtyOnShelf(stock.getShelfStockId(), newQty);

                remaining -= toTake;
                if (remaining == 0) {
                    return;  // fully allocated
                }
            }
        }

        // 3) if we still have some left, not enough stock on shelf
        if (remaining > 0) {
            throw new SQLException(
                    "Insufficient on-shelf stock for item " + itemCode +
                            " (short by " + remaining + " units)"
            );
        }
    }
}
