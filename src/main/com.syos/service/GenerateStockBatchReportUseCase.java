package main.com.syos.service;

import main.com.syos.model.Batch;
import main.com.syos.model.ShelfStock;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.ShelfStockDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateStockBatchReportUseCase {
    private final BatchDAO batchDAO;
    private final ShelfStockDAO shelfStockDAO;

    public GenerateStockBatchReportUseCase(BatchDAO batchDAO,
                                           ShelfStockDAO shelfStockDAO) {
        this.batchDAO       = batchDAO;
        this.shelfStockDAO  = shelfStockDAO;
    }

    /**
     * @return A list of maps, each containing:
     *   "batchId"      → Integer,
     *   "itemCode"     → String,
     *   "qtyReceived"  → Integer,
     *   "dateReceived" → LocalDate,
     *   "expiryDate"   → LocalDate,
     *   "qtyOnShelf"   → Integer
     * @throws SQLException on DB error
     */
    public List<Map<String,Object>> execute() throws SQLException {

        List<Batch> batches = batchDAO.findAll();

        List<Map<String,Object>> report = new ArrayList<>();
        for (Batch batch : batches) {
            // Sum up how many units of this batch are on shelf
            List<ShelfStock> stocks = shelfStockDAO.findByBatchId(batch.getBatchId());
            int onShelf = stocks.stream()
                    .mapToInt(ShelfStock::getQtyOnShelf)
                    .sum();

            Map<String,Object> entry = new LinkedHashMap<>();
            entry.put("batchId",      batch.getBatchId());
            entry.put("itemCode",     batch.getItemCode());
            entry.put("qtyReceived",  batch.getQtyReceived());
            entry.put("dateReceived", batch.getDateReceived());
            entry.put("expiryDate",   batch.getExpiryDate());
            entry.put("qtyOnShelf",   onShelf);

            report.add(entry);
        }
        return report;
    }
}