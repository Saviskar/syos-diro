package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.model.OnlineOrder;
import main.com.syos.repository.Impl.ItemDAOImpl;
import main.com.syos.repository.Impl.OnlineOrderDAOImpl;
import main.com.syos.repository.ItemDAO;
import main.com.syos.repository.OnlineOrderDAO;
import main.com.syos.service.ListOnlineOrdersUseCase;
import main.com.syos.service.ProcessOnlineOrderUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet {
    private ProcessOnlineOrderUseCase placeUseCase;
    private ListOnlineOrdersUseCase listUseCase;
    private ObjectMapper mapper;

    @Override
    public void init() {
        ItemDAO itemDAO                   = new ItemDAOImpl();
        OnlineOrderDAO orderDAO           = new OnlineOrderDAOImpl();
        this.placeUseCase                 = new ProcessOnlineOrderUseCase(itemDAO, orderDAO);
        this.listUseCase                  = new ListOnlineOrdersUseCase(orderDAO);

        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // POST /api/orders → place new order
        resp.setContentType("application/json");
        try {
            // parse { "userId":1, "items":{...} }
            OrderRequest r = mapper.readValue(req.getInputStream(), OrderRequest.class);
            OnlineOrder o = placeUseCase.execute(r.userId, r.items);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getOutputStream(), o);

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), Map.of("error", e.getMessage()));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(), Map.of("error", e.getMessage()));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // GET  /api/orders?userId={id} → list orders
        resp.setContentType("application/json");
        String u = req.getParameter("userId");
        if (u == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("error","Missing required 'userId' parameter"));
            return;
        }
        int userId;
        try {
            userId = Integer.parseInt(u);
        } catch (NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("error","Invalid 'userId'; must be integer"));
            return;
        }

        try {
            List<OnlineOrder> orders = listUseCase.execute(userId);
            mapper.writeValue(resp.getOutputStream(), orders);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("error", e.getMessage()));
        }
    }

    // --- DTO for POST ---
    public static class OrderRequest {
        public int userId;
        public Map<String,Integer> items;
    }
}