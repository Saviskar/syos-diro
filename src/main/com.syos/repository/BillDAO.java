package main.com.syos.repository;

import main.com.syos.model.Bill;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface BillDAO {
    // Persists a new Bill (transaction) into the database.
    void createBill(Bill bill) throws SQLException;

    // Retrieves a Bill by its unique ID.
    Bill findById(int billId) throws SQLException;

    // Fetches all bills generated on the specified date (daily sales report).
    List<Bill> findByDate(LocalDate date) throws SQLException;
}
