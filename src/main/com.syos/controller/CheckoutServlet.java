package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.model.Bill;
import main.com.syos.repository.BillDAO;
import main.com.syos.repository.BillLineDAO;
import main.com.syos.repository.Impl.BillDAOImpl;
import main.com.syos.repository.Impl.BillLineDAOImpl;
import main.com.syos.repository.Impl.ItemDAOImpl;
import main.com.syos.repository.Impl.ShelfStockDAOImpl;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.ShelfStockDAO;
import main.com.syos.service.ProcessCheckoutUseCase;
import main.com.syos.repository.BatchDAO;
import main.com.syos.repository.Impl.BatchDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/checkout")
public class CheckoutServlet extends HttpServlet {
    private ProcessCheckoutUseCase checkoutUseCase;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        // Instantiate DAO implementations
        ItemDAO itemDAO           = new ItemDAOImpl();
        BillDAO billDAO           = new BillDAOImpl();
        BillLineDAO billLineDAO   = new BillLineDAOImpl();
        ShelfStockDAO shelfDAO    = new ShelfStockDAOImpl();
        BatchDAO batchDAO         = new BatchDAOImpl();
        // Wire up use case
        this.checkoutUseCase = new ProcessCheckoutUseCase(
                itemDAO, billDAO, billLineDAO, shelfDAO, batchDAO
        );
        // Configure JSON mapper for Java time support
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private static class CheckoutRequest {
        public Map<String, Integer> items;
        public BigDecimal cashTendered;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            // Parse request JSON
            CheckoutRequest request = objectMapper.readValue(
                    req.getInputStream(), CheckoutRequest.class
            );
            // Execute business logic
            Bill bill = checkoutUseCase.execute(request.items, request.cashTendered);
            // Return the bill as JSON
            objectMapper.writeValue(resp.getOutputStream(), bill);
        } catch (SQLException e) {
            // Database error
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(
                    resp.getOutputStream(), Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            // JSON parsing or other errors
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(
                    resp.getOutputStream(), Map.of("error", e.getMessage())
            );
        }
    }
}

