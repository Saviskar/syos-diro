package main.com.syos.repository;

import main.com.syos.model.ShelfStock;

import java.sql.SQLException;
import java.util.List;

public interface ShelfStockDAO {
    // Inserts a new shelf-stock record for a batch.
    void createShelfStock(ShelfStock stock) throws SQLException;

    // Retrieves a shelf stock entry by its ID.
    ShelfStock findById(int shelfStockId) throws SQLException;

    // Lists all shelf stock entries for a given batch.
    List<ShelfStock> findByBatchId(int batchId) throws SQLException;

    // List all shelf stock entries
    List<ShelfStock> findAll() throws SQLException;

    // Updates the quantity currently on the shelf for a batch.
    void updateQtyOnShelf(int shelfStockId, int newQty) throws SQLException;
}
