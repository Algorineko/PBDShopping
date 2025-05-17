package com.pbdcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class DbTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db/test")
    public String testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                return "Connected to database successfully!";
            } else {
                return "Failed to connect to database.";
            }
        } catch (SQLException e) {
            return "Error connecting to database: " + e.getMessage();
        }
    }
}
