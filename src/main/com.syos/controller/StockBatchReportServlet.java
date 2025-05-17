package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.Impl.BatchDAOImpl;
import main.com.syos.repository.Impl.ShelfStockDAOImpl;
import main.com.syos.repository.ShelfStockDAO;
import main.com.syos.service.GenerateStockBatchReportUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reports/stock-batch")
public class StockBatchReportServlet extends HttpServlet {
    private GenerateStockBatchReportUseCase reportUseCase;
    private ObjectMapper mapper;

    @Override
    public void init() {
        BatchDAO batchDAO       = new BatchDAOImpl();
        ShelfStockDAO shelfDAO  = new ShelfStockDAOImpl();
        this.reportUseCase      = new GenerateStockBatchReportUseCase(batchDAO, shelfDAO);

        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        try {
            List<Map<String,Object>> report = reportUseCase.execute();
            mapper.writeValue(resp.getOutputStream(), report);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String,String> err = new HashMap<>();
            err.put("error", e.getMessage());
            mapper.writeValue(resp.getOutputStream(), err);
        }
    }
}
