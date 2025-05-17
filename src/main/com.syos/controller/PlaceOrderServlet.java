package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.model.OnlineOrder;
import main.com.syos.repository.Impl.ItemDAOImpl;
import main.com.syos.repository.Impl.OnlineOrderDAOImpl;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.OnlineOrderDAO;
import main.com.syos.service.ProcessOnlineOrderUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/orders")
public class PlaceOrderServlet extends HttpServlet {
    private ProcessOnlineOrderUseCase orderUseCase;
    private ObjectMapper mapper;

    @Override
    public void init() {
        ItemDAO itemDAO               = new ItemDAOImpl();
        OnlineOrderDAO orderDAO       = new OnlineOrderDAOImpl();
        this.orderUseCase             = new ProcessOnlineOrderUseCase(itemDAO, orderDAO);

        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        // Emit ISO-8601 instead of timestamps
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static class OrderRequest {
        public int userId;
        public Map<String,Integer> items;
    }

    @Override
    protected void doPost(HttpServletRequest    req,
                          HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            // 1. Parse body
            OrderRequest request = mapper.readValue(
                    req.getInputStream(), OrderRequest.class
            );

            // 2. Execute use-case
            OnlineOrder order = orderUseCase.execute(
                    request.userId, request.items
            );

            // 3. Return created order
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getOutputStream(), order);

        } catch (IllegalArgumentException e) {
            // e.g. unknown item code
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(
                    resp.getOutputStream(),
                    Map.of("error", e.getMessage())
            );
        } catch (SQLException e) {
            // DB error
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(
                    resp.getOutputStream(),
                    Map.of("error", e.getMessage())
            );
        }
    }
}
