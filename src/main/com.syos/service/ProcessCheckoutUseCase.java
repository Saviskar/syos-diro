package main.com.syos.service;

import main.com.syos.model.Bill;
import main.com.syos.model.BillLine;
import main.com.syos.model.Item;
import main.com.syos.repository.BillDAO;
import main.com.syos.repository.BillLineDAO;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.ShelfStockDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

public class ProcessCheckoutUseCase {
    private final ItemDAO itemDAO;
    private final BillDAO billDAO;
    private final BillLineDAO billLineDAO;
    private final ShelfStockDAO shelfStockDAO;

    public ProcessCheckoutUseCase(ItemDAO itemDAO, BillDAO billDAO,
                                  BillLineDAO billLineDAO, ShelfStockDAO shelfStockDAO) {
        this.itemDAO = itemDAO;
        this.billDAO = billDAO;
        this.billLineDAO = billLineDAO;
        this.shelfStockDAO = shelfStockDAO;
    }

    public Bill execute(Map<String,Integer> items, BigDecimal cashTendered) throws SQLException {
        // 1. Build Bill
        BigDecimal total = calculateTotal(items);
        Bill bill = new Bill();
        bill.setBillDate(LocalDateTime.now());
        bill.setTotalAmount(total);
        bill.setCashTendered(cashTendered);
        bill.setDiscount(BigDecimal.ZERO);
        bill.setChangeAmount(cashTendered.subtract(total));
        billDAO.createBill(bill);

        // 2. Persist lines and update stock
        items.forEach((code, qty) -> {
            try {
                Item product = itemDAO.findByCode(code);
                BigDecimal lineTotal = product.getUnitPrice().multiply(BigDecimal.valueOf(qty));
                BillLine line = new main.com.syos.model.BillLine();
                line.setBillId(bill.getBillId());
                line.setItemCode(code);
                line.setQuantity(qty);
                line.setLineTotal(lineTotal);
                billLineDAO.createBillLine(line);

                // TODO: select batch, decrement shelfStock
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return bill;
    }

    private BigDecimal calculateTotal(Map<String,Integer> items) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Item item = itemDAO.findByCode(entry.getKey());
            total = total.add(item.getUnitPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return total;
    }
}
