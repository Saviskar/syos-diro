package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.model.Item;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.Impl.BatchDAOImpl;
import main.com.syos.repository.Impl.ItemDAOImpl;
import main.com.syos.repository.Impl.ShelfStockDAOImpl;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.ShelfStockDAO;
import main.com.syos.service.CheckReorderUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reports/reorder")
public class ReorderServlet extends HttpServlet {
    private CheckReorderUseCase checkReorder;
    private ObjectMapper mapper;

    @Override
    public void init() {
        ItemDAO itemDAO         = new ItemDAOImpl();
        ShelfStockDAO ssDAO     = new ShelfStockDAOImpl();
        BatchDAO batchDAO       = new BatchDAOImpl();
        this.checkReorder       = new CheckReorderUseCase(itemDAO, ssDAO, batchDAO);
        this.mapper             = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            // Explicitly declare the list type instead of using 'var'
            List<Item> lowStockItems = checkReorder.execute();
            mapper.writeValue(resp.getOutputStream(), lowStockItems);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(
                    resp.getOutputStream(),
                    Map.of("error", e.getMessage())
            );
        }
    }
}
