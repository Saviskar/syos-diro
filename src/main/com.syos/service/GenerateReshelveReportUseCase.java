package main.com.syos.service;

import main.com.syos.model.Bill;
import main.com.syos.model.BillLine;
import main.com.syos.model.Item;
import main.com.syos.repository.BillDAO;
import main.com.syos.repository.BillLineDAO;
import main.com.syos.repository.ItemDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class GenerateReshelveReportUseCase {
    private final BillDAO billDAO;
    private final BillLineDAO billLineDAO;
    private final ItemDAO itemDAO;

    public GenerateReshelveReportUseCase(BillDAO billDAO,
                                         BillLineDAO billLineDAO,
                                         ItemDAO itemDAO) {
        this.billDAO     = billDAO;
        this.billLineDAO = billLineDAO;
        this.itemDAO     = itemDAO;
    }

    /**
     * @param date the date to report on (YYYY-MM-DD)
     * @return a list of maps, each containing:
     *   "itemCode" → String,
     *   "itemName" → String,
     *   "reshelveQuantity" → Integer
     * @throws SQLException on any DB error
     */
    public List<Map<String,Object>> execute(LocalDate date) throws SQLException {
        List<Bill> bills = billDAO.findByDate(date);

        // Preserve order of first-seen item codes
        Map<String, Map<String,Object>> reportMap = new LinkedHashMap<>();

        for (Bill bill : bills) {
            List<BillLine> lines = billLineDAO.findByBillId(bill.getBillId());
            for (BillLine line : lines) {
                String code = line.getItemCode();
                Map<String,Object> entry = reportMap.get(code);
                if (entry == null) {
                    Item item = itemDAO.findByCode(code);
                    String name = item != null ? item.getName() : code;

                    entry = new HashMap<>();
                    entry.put("itemCode", code);
                    entry.put("itemName", name);
                    entry.put("reshelveQuantity", 0);
                    reportMap.put(code, entry);
                }
                int qtySoFar = (Integer) entry.get("reshelveQuantity");
                entry.put("reshelveQuantity", qtySoFar + line.getQuantity());
            }
        }

        return new ArrayList<>(reportMap.values());
    }
}
