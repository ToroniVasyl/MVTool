package com.Main;

import java.sql.*;
import com.Main.DataBase.DataBaseConnect;

public class AuthService {

    public static boolean register(String username, String password) {
        try (Connection conn = DataBaseConnect.connect()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // 🔐 У майбутньому — з хешем
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Помилка реєстрації: " + e.getMessage());
            return false;
        }
    }

    public static Integer login(String username, String password) {
        try (Connection conn = DataBaseConnect.connect()) {
            String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id"); // Повертаємо user_id
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Помилка входу: " + e.getMessage());
            return null;
        }
    }
}
