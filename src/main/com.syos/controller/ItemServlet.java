package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.com.syos.model.Item;
import main.com.syos.repository.Impl.ItemDAOImpl;
import main.com.syos.repository.ItemDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/items/*")
public class ItemServlet extends HttpServlet {
    private ItemDAO itemDAO;
    private ObjectMapper mapper;

    @Override
    public void init() {
        this.itemDAO = new ItemDAOImpl();
        this.mapper  = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo(); // e.g. "/ITM001" or null

        try {
            if (path == null || "/".equals(path)) {
                // GET /api/items → list all
                List<Item> items = itemDAO.findAll();
                mapper.writeValue(resp.getOutputStream(), items);
            } else {
                // GET /api/items/{code}
                String code = path.substring(1);
                Item item = itemDAO.findByCode(code);
                if (item == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    mapper.writeValue(resp.getOutputStream(),
                            Map.of("error", "Item not found: " + code));
                } else {
                    mapper.writeValue(resp.getOutputStream(), item);
                }
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("error", e.getMessage()));
        }
    }
}
