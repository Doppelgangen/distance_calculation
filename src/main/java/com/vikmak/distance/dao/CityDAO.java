package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Makarov
 */


@Path("/cities")
public class CityDAO {

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

    @GET
    @Path("/1")
    @Produces(MediaType.APPLICATION_XML)
    public Response hello(){
        City city = new City(1, "Mos", 23, 35);
        return Response.ok(city).build();
    }

    @GET
    @Path("/2")
    @Produces(MediaType.APPLICATION_XML)
    public City test() {
        return new City(1, "Mos", 33, 55);
    }

    @GET
    @Path("/3")
    @Produces(MediaType.APPLICATION_XML)
    public Response jaxb() throws JAXBException {
        City city = new City(1, "Nox", 99, 380);
        JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(city, new File("C:/Java_env/file.xml"));
        return Response.ok(city).build();
    }

    @GET
    @Path("/4")
    @Produces(MediaType.APPLICATION_XML)
    public Response jaxb2() throws JAXBException {
        City city = new City();
        JAXBContext context = JAXBContext.newInstance(City.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        city = (City) unmarshaller.unmarshal(new File("C:/Java_env/file.xml"));
        return Response.ok(city).build();
    }
}
