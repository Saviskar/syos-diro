package main.com.syos.repository;

import main.com.syos.model.BillLine;

import java.sql.SQLException;
import java.util.List;

public interface BillLineDAO {
    // Inserts a new BillLine record (an item sold in a transaction).
    void createBillLine(BillLine line) throws SQLException;

    // Retrieves all line items for a particular bill (receipt breakdown).
    List<BillLine> findByBillId(int billId) throws SQLException;
}
