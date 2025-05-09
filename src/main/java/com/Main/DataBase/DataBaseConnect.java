package com.Main.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnect {
    
       private static final String URL = "jdbc:mysql://localhost:3306/mvtool";
    private static final String USER = "vtoron";
    private static final String PASSWORD = "11061985SdSd"; 

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
