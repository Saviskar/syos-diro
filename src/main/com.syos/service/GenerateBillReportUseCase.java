package main.com.syos.service;

import main.com.syos.model.Bill;
import main.com.syos.repository.BillDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Use case for fetching all bills in a date range.
 */
public class GenerateBillReportUseCase {
    private final BillDAO billDAO;

    public GenerateBillReportUseCase(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    /**
     * Returns all bills for the given date.
     * (You could extend this to a range if you add a findBetweenDates method.)
     */
    public List<Bill> execute(LocalDate date) throws SQLException {
        return billDAO.findByDate(date);
    }
}
