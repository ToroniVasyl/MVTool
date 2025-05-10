package com.Main;

import java.sql.*;
import com.Main.DataBase.DataBaseConnect;

public class AuthService {
    
      public static boolean register(String username, String password) {
        try (Connection conn = DataBaseConnect.connect()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // 🔐 у майбутньому — з хешем
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Помилка реєстрації: " + e.getMessage());
            return false;
        }
    }

    public static boolean login(String username, String password) {
        try (Connection conn = DataBaseConnect.connect()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // знайдено користувача
        } catch (SQLException e) {
            System.out.println("Помилка входу: " + e.getMessage());
            return false;
        }
    }
}
