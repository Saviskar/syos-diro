package main.com.syos.repository.Impl;

import main.com.syos.model.BillLine;
import main.com.syos.repository.BillLineDAO;
import main.com.syos.util.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillLineDAOImpl implements BillLineDAO {

    @Override
    public void createBillLine(BillLine line) throws SQLException {
        String sql = "INSERT INTO billline (bill_id, item_code, quantity, line_total) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, line.getBillId());
            ps.setString(2, line.getItemCode());
            ps.setInt(3, line.getQuantity());
            ps.setBigDecimal(4, line.getLineTotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    line.setBillLineId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to create bill line for bill ID: " + line.getBillId(), e);
        }
    }

    @Override
    public List<BillLine> findByBillId(int billId) throws SQLException {
        String sql = "SELECT * FROM billline WHERE bill_id = ?";
        List<BillLine> lines = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BillLine line = new BillLine(
                        rs.getInt("bill_line_id"),
                        rs.getInt("bill_id"),
                        rs.getString("item_code"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("line_total")
                );
                lines.add(line);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to retrieve bill lines for bill ID: " + billId, e);
        }

        return lines;
    }
}
