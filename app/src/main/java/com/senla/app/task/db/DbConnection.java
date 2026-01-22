package com.senla.app.task.db;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation_processor.ConfigProcessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnection {

    static private DbConnection INSTANCE = null;

    private Connection connection = null;

    @ConfigProperty(propertyName = "db_url")
    private String url = "";
    @ConfigProperty(propertyName = "db_username")
    private String username = "";
    @ConfigProperty(propertyName = "db_password")
    private String password = "";

    static public DbConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DbConnection();
            ConfigProcessor.applyConfig(INSTANCE);
        }
        return INSTANCE;
    }

    private DbConnection() { }

    public Connection initOrGetConnection() {

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
