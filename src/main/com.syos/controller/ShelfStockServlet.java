package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.model.ShelfStock;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.Impl.BatchDAOImpl;
import main.com.syos.repository.Impl.ShelfStockDAOImpl;
import main.com.syos.repository.ShelfStockDAO;
import main.com.syos.service.ManageStockUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/stock/levels")
public class ShelfStockServlet extends HttpServlet {
    private ManageStockUseCase stockUseCase;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        // Wire DAOs into the use case
        BatchDAO batchDAO       = new BatchDAOImpl();
        ShelfStockDAO shelfDAO  = new ShelfStockDAOImpl();
        this.stockUseCase = new ManageStockUseCase(batchDAO, shelfDAO);

        // JSON mapper
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            List<ShelfStock> levels = stockUseCase.getCurrentShelfLevels();
            objectMapper.writeValue(resp.getOutputStream(), levels);

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(
                    resp.getOutputStream(),
                    Map.of("error", e.getMessage())
            );
        }
    }
}