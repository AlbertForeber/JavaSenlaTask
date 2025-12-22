package com.senla.app.task.db;

import com.senla.annotation.ConfigProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    static private DbConnection INSTANCE = null;

    private Connection connection = null;

    @ConfigProperty(propertyName = "db_url")
    private String url = "";
    @ConfigProperty(propertyName = "db_username")
    private String username = "";
    @ConfigProperty(propertyName = "db_password")
    private String password = "";

    static private DbConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DbConnection();
        }
        return INSTANCE;
    }

    static public Connection getConnection() {
        return INSTANCE.initOrGetConnection();
    }

    private DbConnection() {}

    private Connection initOrGetConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.err.println("КРИТИЧЕСКАЯ ОШИБКА: Нет связи с БД: " + e);
                System.exit(1);
            }
        }
        return connection;
    }
}
