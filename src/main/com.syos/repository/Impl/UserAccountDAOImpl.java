package main.com.syos.repository.Impl;

import main.com.syos.model.UserAccount;
import main.com.syos.repository.UserAccountDAO;

import java.sql.SQLException;
import java.util.Optional;

public class UserAccountDAOImpl implements UserAccountDAO {

    @Override
    public void createUser(UserAccount user) throws SQLException {

    }

    @Override
    public Optional<UserAccount> findByUsername(String username) throws SQLException {
        return Optional.empty();
    }

    @Override
    public void updatePassword(int userId, String newHash) throws SQLException {

    }

    @Override
    public void deactivateUser(int userId) throws SQLException {

    }

}
