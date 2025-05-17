package main.com.syos.service;

import main.com.syos.model.Item;
import main.com.syos.model.OnlineOrder;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.OnlineOrderDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

public class ProcessOnlineOrderUseCase {
    private final ItemDAO itemDAO;
    private final OnlineOrderDAO orderDAO;

    public ProcessOnlineOrderUseCase(ItemDAO itemDAO,
                                     OnlineOrderDAO orderDAO) {
        this.itemDAO  = itemDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Places an order for the given user and items.
     *
     * @param userId the ID of the user placing the order
     * @param items  map of item_code → quantity
     * @return the persisted OnlineOrder (with generated orderId)
     * @throws SQLException           on any database error
     * @throws IllegalArgumentException if an item code is not found
     */
    public OnlineOrder execute(int userId,
                               Map<String,Integer> items) throws SQLException {
        // 1. Calculate total amount
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String,Integer> entry : items.entrySet()) {
            String code = entry.getKey();
            Integer quantity = entry.getValue();

            Item item = itemDAO.findByCode(code);
            if (item == null) {
                throw new IllegalArgumentException("Unknown item code: " + code);
            }

            BigDecimal lineTotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(quantity));
            total = total.add(lineTotal);
        }

        // 2. Build order
        OnlineOrder order = new OnlineOrder();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(total);

        // 3. Persist
        orderDAO.createOrder(order);

        return order;
    }
}