package main.com.syos.repository;

import main.com.syos.model.UserAccount;

import java.sql.SQLException;
import java.util.Optional;

public interface UserAccountDAO {
    // Creates a new user account for internet shopping.
    void createUser(UserAccount user) throws SQLException;

    // Finds a user account by username for login/authentication.
    Optional<UserAccount> findByUsername(String username) throws SQLException;

    // Updates a user's password hash (e.g., on password reset).
    void updatePassword(int userId, String newHash) throws SQLException;

    // Deactivates (logical delete) a user account.
    // void deactivateUser(int userId) throws SQLException;
}
