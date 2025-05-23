package main.com.syos.repository.Impl;

import main.com.syos.model.ShelfStock;
import main.com.syos.repository.ShelfStockDAO;
import main.com.syos.util.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShelfStockDAOImpl implements ShelfStockDAO {

    @Override
    public void createShelfStock(ShelfStock stock) throws SQLException {
        String sql = "INSERT INTO shelfstock (batch_id, qty_on_shelf) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stock.getBatchId());
            ps.setInt(2, stock.getQtyOnShelf());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error creating shelf stock for batch ID: " + stock.getBatchId(), e);
        }
    }

    @Override
    public List<ShelfStock> findAll() throws SQLException {
        String sql = "SELECT * FROM shelfstock";
        List<ShelfStock> shelfStocks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ShelfStock shelfStock = new ShelfStock(
                        rs.getInt("shelf_stock_id"),
                        rs.getInt("batch_id"),
                        rs.getInt("qty_on_shelf")
                );
                shelfStocks.add(shelfStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return shelfStocks;
    }
    @Override
    public ShelfStock findById(int shelfStockId) throws SQLException {
        String sql = "SELECT * FROM shelfstock WHERE shelf_stock_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shelfStockId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ShelfStock(
                        rs.getInt("shelf_stock_id"),
                        rs.getInt("batch_id"),
                        rs.getInt("qty_on_shelf")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding shelf stock by ID: " + shelfStockId, e);
        }

        return null;
    }

    @Override
    public List<ShelfStock> findByBatchId(int batchId) throws SQLException {
        String sql = "SELECT * FROM shelfstock WHERE batch_id = ?";
        List<ShelfStock> shelfStocks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, batchId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ShelfStock shelfStock = new ShelfStock(
                        rs.getInt("shelf_stock_id"),
                        rs.getInt("batch_id"),
                        rs.getInt("qty_on_shelf")
                );
                shelfStocks.add(shelfStock);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding shelf stocks by batch ID: " + batchId, e);
        }

        return shelfStocks;
    }

    @Override
    public void updateQtyOnShelf(int shelfStockId, int newQty) throws SQLException {
        String sql = "UPDATE shelfstock SET qty_on_shelf = ? WHERE shelf_stock_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newQty);
            ps.setInt(2, shelfStockId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating quantity for shelf stock ID: " + shelfStockId, e);
        }
    }
}
