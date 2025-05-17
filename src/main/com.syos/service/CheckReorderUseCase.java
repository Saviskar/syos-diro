package main.com.syos.service;

import main.com.syos.model.Batch;
import main.com.syos.model.Item;
import main.com.syos.model.ShelfStock;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.ShelfStockDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckReorderUseCase {
    private final ItemDAO itemDAO;
    private final ShelfStockDAO shelfStockDAO;
    private final BatchDAO batchDAO;

    /**
     * @param itemDAO         DAO for Item lookups (to get reorder levels)
     * @param shelfStockDAO   DAO for ShelfStock (to get quantities on shelf)
     * @param batchDAO        DAO for Batch (to map shelf-stock to item codes)
     */
    public CheckReorderUseCase(ItemDAO itemDAO,
                               ShelfStockDAO shelfStockDAO,
                               BatchDAO batchDAO) {
        this.itemDAO       = itemDAO;
        this.shelfStockDAO = shelfStockDAO;
        this.batchDAO      = batchDAO;
    }

    /**
     * Executes the reorder check.
     *
     * @return List of Items whose total on-hand quantity is below their reorder_level.
     * @throws SQLException if any DAO operation fails.
     */
    public List<Item> execute() throws SQLException {
        // 1. Load all shelf-stock entries
        List<ShelfStock> shelfStocks = shelfStockDAO.findAll();

        // 2. Aggregate quantities per itemCode
        Map<String, Integer> onHandMap = new HashMap<>();
        for (ShelfStock ss : shelfStocks) {
            Batch batch = batchDAO.findById(ss.getBatchId());
            String code = batch.getItemCode();
            onHandMap.put(code,
                    onHandMap.getOrDefault(code, 0) + ss.getQtyOnShelf());
        }

        // 3. Find all items and collect those below reorder threshold
        List<Item> lowStockItems = new ArrayList<>();
        for (Item item : itemDAO.findAll()) {
            int available = onHandMap.getOrDefault(item.getItemCode(), 0);
            if (available < item.getReorderLevel()) {
                lowStockItems.add(item);
            }
        }

        return lowStockItems;
    }
}
