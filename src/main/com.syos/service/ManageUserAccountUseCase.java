package main.com.syos.service;

import main.com.syos.model.UserAccount;
import main.com.syos.repository.UserAccountDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class ManageUserAccountUseCase {
    private final UserAccountDAO userDao;

    public ManageUserAccountUseCase(UserAccountDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * Registers a new user. Caller must pass a hashed password.
     */
    public UserAccount register(String username,
                                String passwordHash,
                                String fullName,
                                String email) throws SQLException {
        UserAccount u = new UserAccount();
        u.setUsername(username);
        u.setPasswordHash(passwordHash);
        u.setFullName(fullName);
        u.setEmail(email);
        u.setCreatedAt(LocalDateTime.now());
        userDao.createUser(u);
        return u;
    }

    /**
     * Authenticates an existing user by username + hashed password.
     * @return an Optional containing the UserAccount if creds match.
     */
    public Optional<UserAccount> authenticate(String username,
                                              String passwordHash) throws SQLException {
        Optional<UserAccount> maybe = userDao.findByUsername(username);
        if (maybe.isPresent() && maybe.get().getPasswordHash().equals(passwordHash)) {
            return maybe;
        }
        return Optional.empty();
    }
}