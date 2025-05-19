package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.repository.BillDAO;
import main.com.syos.repository.Impl.BillDAOImpl;
import main.com.syos.service.GenerateBillReportUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reports/bills")
public class BillReportServlet extends HttpServlet {
    private GenerateBillReportUseCase useCase;
    private ObjectMapper mapper;

    @Override
    public void init() {
        BillDAO billDAO      = new BillDAOImpl();
        this.useCase         = new GenerateBillReportUseCase(billDAO);
        this.mapper          = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String dateParam = req.getParameter("date");
        if (dateParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("error", "Missing 'date' parameter"));
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateParam);
        } catch (DateTimeParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("error", "Invalid date format. Use YYYY-MM-DD."));
            return;
        }

        try {
            List<?> bills = useCase.execute(date);
            mapper.writeValue(resp.getOutputStream(), bills);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(),
                    Map.of("error", e.getMessage()));
        }
    }
}
