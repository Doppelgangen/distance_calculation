package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Viktor Makarov
 */

@ApplicationScoped
@Path("/cities")
public class CityDAO {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    private static final String URL = "jdbc:mysql://localhost:3306/distance_calculator";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

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

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public List<City> cities() {
        List<City> cities = new ArrayList<City>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM cities";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                City city = new City();
                city.setId(resultSet.getLong("id"));
                city.setName(resultSet.getString("name"));
                city.setLatitude(resultSet.getDouble("latitude"));
                city.setLongitude(resultSet.getDouble("longitude"));
                cities.add(city);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cities;
    }
}
