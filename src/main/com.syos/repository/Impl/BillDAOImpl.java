package main.com.syos.repository.Impl;

import main.com.syos.model.Batch;
import main.com.syos.model.Bill;
import main.com.syos.repository.BillDAO;
import main.com.syos.util.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillDAOImpl implements BillDAO {

    @Override
    public void createBill(Bill bill) throws SQLException {

        String sql = "INSERT INTO bill (bill_date, cash_tendered, total_amount, discount, change_amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(bill.getBillDate()));
            ps.setBigDecimal(2, bill.getCashTendered());
            ps.setBigDecimal(3, bill.getTotalAmount());
            ps.setBigDecimal(4, bill.getDiscount());
            ps.setBigDecimal(5, bill.getChangeAmount());

            ps.executeUpdate();

            // Optional: retrieve auto-generated bill_id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bill.setBillId(rs.getInt(1)); // set generated ID back to the object
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Bill findById(int billId) throws SQLException {

        String sql = "SELECT * FROM bill WHERE bill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Bill(
                        rs.getInt("bill_id"),
                        rs.getTimestamp("bill_date").toLocalDateTime(),
                        rs.getBigDecimal("cash_tendered"),
                        rs.getBigDecimal("total_amount"),
                        rs.getBigDecimal("discount"),
                        rs.getBigDecimal("change_amount")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Bill> findByDate(LocalDate date) throws SQLException {

        String sql = "SELECT * FROM bill WHERE DATE(bill_date) = ?";
        List<Bill> bills = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(date));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("bill_id"),
                        rs.getTimestamp("bill_date").toLocalDateTime(),
                        rs.getBigDecimal("cash_tendered"),
                        rs.getBigDecimal("total_amount"),
                        rs.getBigDecimal("discount"),
                        rs.getBigDecimal("change_amount")
                );
                bills.add(bill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }
}
