package main.com.syos.repository;

import main.com.syos.model.Batch;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface BatchDAO {
    // Adds a new batch of stock into the system.
    void createBatch(Batch batch) throws SQLException;

    // Retrieves a Batch record by its database ID.
    Batch findById(int batchId) throws SQLException;

    // Retrieves all batches associated with a specific item code.
    List<Batch> findByItemCode(String itemCode) throws SQLException;

    // Finds batches whose expiry date is before the given date.
    List<Batch> findExpiringBefore(LocalDate date) throws SQLException;

    // Updates the received quantity in an existing batch (e.g., for corrections).
    void updateQtyReceived(int batchId, int newQty) throws SQLException;
}
