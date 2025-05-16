package main.com.syos.repository.Impl;

import main.com.syos.model.Item;
import main.com.syos.model.OnlineOrder;
import main.com.syos.repository.OnlineOrderDAO;
import main.com.syos.util.db.DBConnection;

import java.io.DataInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OnlineOrderDAOImpl implements OnlineOrderDAO {

    @Override
    public void createOrder(OnlineOrder order) throws SQLException {
        String sql = "INSERT INTO onlineorder (user_id, order_date, status, total_amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(order.getOrderDate()));
            ps.setString(3, order.getStatus());
            ps.setBigDecimal(4, order.getTotalAmount());

            ps.executeUpdate();

            // Retrieve generated order_id if needed
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setOrderId(rs.getInt(1)); // set auto-generated ID back to the object
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public OnlineOrder findById(int orderId) throws SQLException {

        String sql = "SELECT * FROM onlineorder WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new OnlineOrder(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<OnlineOrder> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM onlineorder WHERE user_id = ?";
        List<OnlineOrder> orders = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OnlineOrder order = new OnlineOrder(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount")
                );
                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }


    @Override
    public void updateStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE onlineorder SET status = ? WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, orderId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
