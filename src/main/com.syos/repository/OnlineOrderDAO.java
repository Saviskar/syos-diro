package main.com.syos.repository;

import main.com.syos.model.OnlineOrder;

import java.sql.SQLException;
import java.util.List;

public interface OnlineOrderDAO {
    // Persists a new online order record.
    void createOrder(OnlineOrder order) throws SQLException;

    // Retrieves an order by its ID (for order-status views).
        OnlineOrder findById(int orderId) throws SQLException;

    // Lists all orders placed by a specific user.
    List<OnlineOrder> findByUserId(int userId) throws SQLException;

    // Updates the status of an existing order (e.g., to SHIPPED).
    void updateStatus(int orderId, String newStatus) throws SQLException;
}
