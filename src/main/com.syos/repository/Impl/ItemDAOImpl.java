package main.com.syos.repository.Impl;

import main.com.syos.model.Item;
import main.com.syos.repository.ItemDAO;
import main.com.syos.util.db.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public void createItem(Item item) throws SQLException {
        String sql = "INSERT INTO item (item_code, name, unit_price, reorder_level) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getItemCode());
            ps.setString(2, item.getName());
            ps.setBigDecimal(3, item.getUnitPrice());
            ps.setInt(4, item.getReorderLevel());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to create item with code: " + item.getItemCode(), e);
        }
    }

    @Override
    public Item findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM item WHERE item_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Item(
                        rs.getString("item_code"),
                        rs.getString("name"),
                        rs.getBigDecimal("unit_price"),
                        rs.getInt("reorder_level")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to find item with code: " + code, e);
        }

        return null;
    }

    @Override
    public List<Item> findAll() throws SQLException {
        String sql = "SELECT * FROM item";
        List<Item> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getString("item_code"),
                        rs.getString("name"),
                        rs.getBigDecimal("unit_price"),
                        rs.getInt("reorder_level")
                );
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to retrieve all items", e);
        }

        return items;
    }

    @Override
    public void updatePrice(String code, double newPrice) throws SQLException {
        String sql = "UPDATE item SET unit_price = ? WHERE item_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, BigDecimal.valueOf(newPrice));
            ps.setString(2, code);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to update price for item with code: " + code, e);
        }
    }

    @Override
    public void updateReorderLevel(String code, int newLevel) throws SQLException {
        String sql = "UPDATE item SET reorder_level = ? WHERE item_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newLevel);
            ps.setString(2, code);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to update reorder level for item with code: " + code, e);
        }
    }

    @Override
    public void deleteItem(String code) throws SQLException {
        String sql = "DELETE FROM item WHERE item_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to delete item with code: " + code, e);
        }
    }
}
