package main.com.syos.repository.Impl;

import main.com.syos.model.Batch;
import main.com.syos.repository.BatchDAO;
import main.com.syos.util.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BatchDAOImpl implements BatchDAO {

    @Override
    public void createBatch(Batch batch) throws SQLException {
        String sql = "INSERT INTO batch (batch_id, item_code, qty_received, date_received, expiry_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, batch.getBatchId());
            ps.setString(2, batch.getItemCode());
            ps.setInt(3, batch.getQtyReceived());
            ps.setDate(4, java.sql.Date.valueOf(batch.getDateReceived())); //MIGHT CAUSE ISSUE
            ps.setDate(5, java.sql.Date.valueOf(batch.getExpiryDate())); //MIGHT CAUSE ISSUE

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Batch findById(int batchId) throws SQLException {
        String sql = "SELECT * FROM batch WHERE batch_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, batchId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Batch(
                        rs.getInt("batch_id"),
                        rs.getString("item_code"),
                        rs.getInt("qty_received"),
                        rs.getDate("date_received").toLocalDate(),
                        rs.getDate("expiry_date").toLocalDate()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // should we really create list? cant we just get the item from this item code what are the pros and cons
    @Override
    public List<Batch> findByItemCode(String itemCode) throws SQLException {
        String sql = "SELECT * FROM batch WHERE item_code = ?";
        List<Batch> batches = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemCode);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Batch batch = new Batch(
                        rs.getInt("batch_id"),
                        rs.getString("item_code"),
                        rs.getInt("qty_received"),
                        rs.getDate("date_received").toLocalDate(),
                        rs.getDate("expiry_date").toLocalDate()
                );
                batches.add(batch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batches;
    }

    @Override
    public List<Batch> findExpiringBefore(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM batch WHERE expiry_date < ?";
        List<Batch> batches = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(date));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Batch batch = new Batch(
                        rs.getInt("batch_id"),
                        rs.getString("item_code"),
                        rs.getInt("qty_received"),
                        rs.getDate("date_received").toLocalDate(),
                        rs.getDate("expiry_date").toLocalDate()
                );
                batches.add(batch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batches;
    }

    @Override
    public void updateQtyReceived(int batchId, int newQty) throws SQLException {
        String sql = "UPDATE batch SET qty_received = ? WHERE batch_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newQty);
            ps.setInt(2, batchId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
