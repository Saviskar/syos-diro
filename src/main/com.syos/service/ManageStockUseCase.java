package main.com.syos.service;

import main.com.syos.model.Batch;
import main.com.syos.model.ShelfStock;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.ShelfStockDAO;

import java.sql.SQLException;
import java.util.List;

public class ManageStockUseCase {
    private final BatchDAO batchDAO;
    private final ShelfStockDAO shelfStockDAO;

    /**
     * @param batchDAO       DAO for batch operations
     * @param shelfStockDAO  DAO for shelf stock operations
     */
    public ManageStockUseCase(BatchDAO batchDAO, ShelfStockDAO shelfStockDAO) {
        this.batchDAO = batchDAO;
        this.shelfStockDAO = shelfStockDAO;
    }

    /**
     * Records an incoming batch and makes all its quantity available on shelves.
     * @param batch the new batch to receive
     * @throws SQLException on DB errors
     */
    public void receiveNewBatch(Batch batch) throws SQLException {
        // Persist batch
        batchDAO.createBatch(batch);
        // Initialize shelf stock with full quantity received
        ShelfStock stock = new ShelfStock();
        stock.setBatchId(batch.getBatchId());
        stock.setQtyOnShelf(batch.getQtyReceived());
        shelfStockDAO.createShelfStock(stock);
    }

    /**
     * Fetches all current shelf stock entries.
     * @return list of ShelfStock
     * @throws SQLException on DB errors
     */
    public List<ShelfStock> getCurrentShelfLevels() throws SQLException {
        return shelfStockDAO.findAll();
    }
}

