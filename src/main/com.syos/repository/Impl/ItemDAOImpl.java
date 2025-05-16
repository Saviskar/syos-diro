package main.com.syos.repository.Impl;

import main.com.syos.model.Item;
import main.com.syos.repository.ItemDAO;
import main.com.syos.util.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public void createItem(Item item) throws SQLException {
        String sql = "INSERT INTO item (item_code, name, unit_price, reorder_level)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getItemCode());
            ps.setString(2, item.getName());
            ps.setBigDecimal(3, item.getUnitPrice());
            ps.setInt(4, item.getReorderLevel());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Item findByCode(String code) throws SQLException {
        return null;
    }

    @Override
    public List<Item> findAll() throws SQLException {
        return null;
    }

    @Override
    public void updatePrice(String code, double newPrice) throws SQLException {

    }

    @Override
    public void updateReorderLevel(String code, int newLevel) throws SQLException {

    }

    @Override
    public void deleteItem(String code) throws SQLException {

    }

}
