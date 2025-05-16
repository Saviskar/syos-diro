package main.com.syos.repository.Impl;

import main.com.syos.model.OnlineOrder;
import main.com.syos.repository.OnlineOrderDAO;

import java.sql.SQLException;
import java.util.List;

public class OnlineOrderDAOImpl implements OnlineOrderDAO {

    @Override
    public void createOrder(OnlineOrder order) throws SQLException {

    }

    @Override
    public OnlineOrder findById(int orderId) throws SQLException {
        return null;
    }

    @Override
    public List<OnlineOrder> findByUserId(int userId) throws SQLException {
        return null;
    }

    @Override
    public void updateStatus(int orderId, String newStatus) throws SQLException {

    }

}
