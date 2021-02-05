package com.vikmak.distance.controllers;

import com.vikmak.distance.dao.CityDAO;
import com.vikmak.distance.entity.City;
import com.vikmak.distance.utils.Cities;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.vikmak.distance.dao.CityDAO.addCityToDB;

/**
 * @author Viktor Makarov
 */


@Path("/cities")
public class CityController {

    Connection connection = CityDAO.getConnection();

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response citiesList() {
        HashMap<Long, String> display = new HashMap<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM cities";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                display.put(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Response.status(200)
                .entity(display)
                .build();
    }

    @GET
    @Path("/xml")
    @Produces(MediaType.APPLICATION_XML)
    public Cities citiesListXml() {
        List<City> cities = new ArrayList<>();

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
        Cities output = new Cities();
        output.setCities(cities);
        return output;
    }

    /*
        @POST
        @Path("/addNewCities")
        @Consumes(MediaType.APPLICATION_XML)
        @Produces(MediaType.APPLICATION_XML)
        public Response addNewCities(Cities newCities) {
            for (City city : newCities.getCities()) {
                addCityToDB(city);
            }
            return Response.ok().build();
        }

        //Overload for different types of XML input - single input or collection
        @POST
        @Path("/addNewCities")
        @Consumes(MediaType.APPLICATION_XML)
        @Produces(MediaType.APPLICATION_XML)
        public Response addNewCity(City newCity) {
            addCityToDB(newCity);
            return Response.ok().build();
        }
    */
    //Parsing XML file
    @GET
    @Path("/addNewCities")
    @Produces(MediaType.APPLICATION_XML)
    public Response addNewCityXml(@QueryParam("param") String message) throws JAXBException, URISyntaxException {

        URI error = new URI("/cities/error");
        URI none = new URI("/cities/noFile");

        switch (message) {
            case "ok":
                System.out.println("File uploaded");
                break;
            case "error":
                System.out.println("Error happened");
                return Response.temporaryRedirect(error).build();
            case "none":
                System.out.println("File not found");
                return Response.temporaryRedirect(none).build();
            default:
                System.out.println("Request without parameters");
                break;
        }

        Cities cities;
        JAXBContext context = JAXBContext.newInstance(Cities.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        cities = (Cities) unmarshaller.unmarshal(new File(CityDAO.getPath() + "NewCities.xml"));

        for (City city : cities.getCities()) {
            addCityToDB(city);
        }
        return Response.status(200).build();
    }

    @GET
    @Path("/error")
    public Response errorPage() {
        return Response.status(200).entity("Error happened during upload. Check upload file.").build();
    }

    @GET
    @Path("/noFile")
    public Response noFile() {
        return Response.status(200).entity("No file have been uploaded. Please upload an xml file.").build();
    }
}
