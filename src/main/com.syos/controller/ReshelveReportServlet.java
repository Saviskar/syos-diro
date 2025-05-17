package main.com.syos.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.repository.BillDAO;
import main.com.syos.repository.BillLineDAO;
import main.com.syos.repository.Impl.BillDAOImpl;
import main.com.syos.repository.Impl.BillLineDAOImpl;
import main.com.syos.repository.Impl.ItemDAOImpl;
import main.com.syos.repository.ItemDAO;
import main.com.syos.service.GenerateReshelveReportUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reports/reshelve")
public class ReshelveReportServlet extends HttpServlet {
    private GenerateReshelveReportUseCase reshelveUseCase;
    private ObjectMapper mapper;

    @Override
    public void init() {
        BillDAO billDAO        = new BillDAOImpl();
        BillLineDAO lineDAO    = new BillLineDAOImpl();
        ItemDAO itemDAO        = new ItemDAOImpl();
        this.reshelveUseCase   = new GenerateReshelveReportUseCase(billDAO, lineDAO, itemDAO);

        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String dateParam = req.getParameter("date");
        if (dateParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String,String> err = new HashMap<>();
            err.put("error", "Missing required 'date' parameter (YYYY-MM-DD)");
            mapper.writeValue(resp.getOutputStream(), err);
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateParam);
        } catch (DateTimeParseException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String,String> err = new HashMap<>();
            err.put("error", "Invalid date format. Use YYYY-MM-DD.");
            mapper.writeValue(resp.getOutputStream(), err);
            return;
        }

        try {
            List<Map<String,Object>> report = reshelveUseCase.execute(date);
            mapper.writeValue(resp.getOutputStream(), report);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String,String> err = new HashMap<>();
            err.put("error", e.getMessage());
            mapper.writeValue(resp.getOutputStream(), err);
        }
    }
}