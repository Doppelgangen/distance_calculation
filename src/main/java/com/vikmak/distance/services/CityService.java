package com.vikmak.distance.services;

import jdk.nashorn.internal.objects.annotations.Property;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Viktor Makarov
 */
public class CityService {

    private static final String URL = "jdbc:mysql://localhost:3306/distance_calculator";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String uploadPath = "C:\\calculation\\temp\\";
//            "java:/magenta/datasource/test-distance-calculator";
//
//File uploads = new File(System.getProperty("jboss.server.data.dir"), "uploads");

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String getPath(){
        return uploadPath;
    }
}
