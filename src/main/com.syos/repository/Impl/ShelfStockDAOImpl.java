package main.com.syos.repository.Impl;

import main.com.syos.model.ShelfStock;
import main.com.syos.repository.ShelfStockDAO;

import java.sql.SQLException;
import java.util.List;

public class ShelfStockDAOImpl implements ShelfStockDAO {

    @Override
    public void createShelfStock(ShelfStock stock) throws SQLException {

    }

    @Override
    public ShelfStock findById(int shelfStockId) throws SQLException {
        return null;
    }

    @Override
    public List<ShelfStock> findByBatchId(int batchId) throws SQLException {
        return null;
    }

    @Override
    public void updateQtyOnShelf(int shelfStockId, int newQty) throws SQLException {

    }

}
