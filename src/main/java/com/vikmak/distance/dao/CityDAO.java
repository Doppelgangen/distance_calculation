package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Viktor Makarov
 */
public class CityDAO {

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

    public static void addCityToDB(City newCity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO cities (name, latitude, longitude) VALUES (?, ?, ?)");

            //Removing "/n" symbols from XML file
            String prettyString = newCity.getName().replaceAll("\\W{2,}+", "");

            preparedStatement.setString(1, prettyString);
            preparedStatement.setDouble(2, newCity.getLatitude());
            preparedStatement.setDouble(3, newCity.getLongitude());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
