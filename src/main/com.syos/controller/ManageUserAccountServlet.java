package main.com.syos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.com.syos.model.UserAccount;
import main.com.syos.repository.Impl.UserAccountDAOImpl;
import main.com.syos.repository.UserAccountDAO;
import main.com.syos.service.ManageUserAccountUseCase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/api/users/*")
public class ManageUserAccountServlet extends HttpServlet {
    private ManageUserAccountUseCase useCase;
    private ObjectMapper mapper;

    @Override
    public void init() {
        UserAccountDAO dao = new UserAccountDAOImpl();
        this.useCase = new ManageUserAccountUseCase(dao);

        // Configure Jackson to handle LocalDateTime
        this.mapper = new ObjectMapper();
        // register the module for java.time
        mapper.registerModule(new JavaTimeModule());
        // emit dates as ISO-8601 strings instead of timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo(); // either "/auth" or null/"/"
        try {
            if ("/auth".equals(path)) {
                // --- LOGIN ---
                AuthRequest auth = mapper.readValue(req.getInputStream(), AuthRequest.class);
                Optional<UserAccount> user = useCase.authenticate(auth.username, auth.password);
                if (user.isPresent()) {
                    mapper.writeValue(resp.getOutputStream(), user.get());
                } else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    mapper.writeValue(resp.getOutputStream(),
                            Map.of("error", "Invalid credentials"));
                }

            } else {
                // --- REGISTER ---
                RegisterRequest reg = mapper.readValue(req.getInputStream(), RegisterRequest.class);
                UserAccount created = useCase.register(
                        reg.username, reg.password, reg.fullName, reg.email
                );
                resp.setStatus(HttpServletResponse.SC_CREATED);
                mapper.writeValue(resp.getOutputStream(), created);
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(), Map.of("error", e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), Map.of("error", e.getMessage()));
        }
    }

    // --- Request DTOs ---
    public static class RegisterRequest {
        public String username;
        public String password;
        public String fullName;
        public String email;
    }
    public static class AuthRequest {
        public String username;
        public String password;
    }
}
