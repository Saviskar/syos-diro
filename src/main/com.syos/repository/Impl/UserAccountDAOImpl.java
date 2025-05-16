package main.com.syos.repository.Impl;

import main.com.syos.model.UserAccount;
import main.com.syos.repository.UserAccountDAO;
import main.com.syos.util.db.DBConnection;

import java.sql.*;
import java.util.Optional;

public class UserAccountDAOImpl implements UserAccountDAO {

    @Override
    public void createUser(UserAccount user) throws SQLException {
        String sql = "INSERT INTO useraccount (username, password_hash, full_name, email, created_at) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to create user", e);
        }
    }


    @Override
    public Optional<UserAccount> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM useraccount WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserAccount user = new UserAccount(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error finding user by username", e);
        }

        return Optional.empty();
    }


    @Override
    public void updatePassword(int userId, String newHash) throws SQLException {
        String sql = "UPDATE useraccount SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error updating password", e);
        }
    }

    //@Override
    //public void deactivateUser(int userId) throws SQLException {
    //
    //}

}
