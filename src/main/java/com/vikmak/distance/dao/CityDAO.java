package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;
import com.vikmak.distance.services.CityService;
import com.vikmak.distance.utils.Cities;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Makarov
 */


@Path("/cities")
public class CityDAO {

    Connection connection = CityService.getConnection();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public Cities citiesList() {
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
        Cities output = new Cities();
        output.setCities(cities);
        return output;
    }


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

    //Parsing XML file
    @GET
    @Path("/addNewCities")
    @Produces(MediaType.APPLICATION_XML)
    public Response addNewCityXml(@QueryParam("param") String message) throws JAXBException {

        switch (message) {
            case "ok":
                System.out.println("File uploaded");
                break;
            case "error":
                System.out.println("Error happened");
                break;
            case "none":
                System.out.println("File not found");
                break;
            default:
                System.out.println("Request without parameters");
                break;
        }

        Cities cities;
        JAXBContext context = JAXBContext.newInstance(Cities.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        cities = (Cities) unmarshaller.unmarshal(new File(CityService.getPath() + "NewCities.xml"));

        for (City city : cities.getCities()) {
            addCityToDB(city);
        }
        return Response.ok().build();
    }

    /*
        @GET
        @Path("/3")
        @Produces(MediaType.APPLICATION_XML)
        public Response jaxb() throws JAXBException {
            City city = new City(1, "Nox", 99, 380);
            JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(city, new File("C:/Java_env/file2.xml"));
            return Response.ok(city).build();
        }
*/

    private void addCityToDB(City newCity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO cities (name, latitude, longitude) VALUES (?, ?, ?)");

            //Removing "/n" symbols from XML file
            String prettyString = newCity.getName().replaceAll("\\W*", "");

            preparedStatement.setString(1, prettyString);
            preparedStatement.setDouble(2, newCity.getLatitude());
            preparedStatement.setDouble(3, newCity.getLongitude());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
