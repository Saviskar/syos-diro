package main.com.syos.service;

import main.com.syos.model.OnlineOrder;
import main.com.syos.repository.OnlineOrderDAO;

import java.sql.SQLException;
import java.util.List;

public class ListOnlineOrdersUseCase {
    private final OnlineOrderDAO orderDAO;

    public ListOnlineOrdersUseCase(OnlineOrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * @param userId ID of the user
     * @return List of OnlineOrder for that user
     * @throws SQLException on DB error
     */
    public List<OnlineOrder> execute(int userId) throws SQLException {
        return orderDAO.findByUserId(userId);
    }
}