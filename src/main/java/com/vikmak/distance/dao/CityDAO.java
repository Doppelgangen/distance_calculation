package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;
import com.vikmak.distance.services.CityService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

    /*
        @POST
        @Path("/addNewCities")
        @Consumes(MediaType.APPLICATION_XML)
        @Produces(MediaType.APPLICATION_XML)
        public Response addNewCities(List<City> newCities) {
            for (City city : newCities) {
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
        }*/

    //Parsing xml file
    @GET
    @Path("/addNewCities")
    @Produces(MediaType.APPLICATION_XML)
    public Response addNewCityXml(@QueryParam("param") String message) {

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

        File file = new File(CityService.getPath() + "NewCities.xml");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("city");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    City city = new City();
                    Element eElement = (Element) nNode;
                    city.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                    city.setLatitude(Double.parseDouble(eElement.getElementsByTagName("latitude").item(0).getTextContent()));
                    city.setLongitude(Double.parseDouble(eElement.getElementsByTagName("longitude").item(0).getTextContent()));
                    addCityToDB(city);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        @GET
        @Path("/4")
        @Produces(MediaType.APPLICATION_XML)
        public Response jaxb2() throws JAXBException {
            City city = new City();
            JAXBContext context = JAXBContext.newInstance(City.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            city = (City) unmarshaller.unmarshal(new File("C:/Java_env/file2.xml"));
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
