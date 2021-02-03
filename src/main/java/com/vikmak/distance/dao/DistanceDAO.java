package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;
import com.vikmak.distance.utils.Calculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Viktor Makarov
 */
public class DistanceDAO {

    Connection connection = CityDAO.getConnection();

    private static DistanceDAO instance;

    public static synchronized DistanceDAO getInstance() {
        if (instance == null) {
            instance = new DistanceDAO();
        }
        return instance;
    }

    public LinkedHashMap<String[], Double> getMapping(List<String> origins, List<String> destinations) {
        //Pre sorting for LinkedHashMap
        Collections.sort(origins);
        Collections.sort(destinations);

        LinkedHashMap<String[], Double> output;

        LinkedHashSet<City> originsSet = citySet(origins);
        LinkedHashSet<City> destinationsSet = citySet(destinations);

        output = getDistance(originsSet, destinationsSet);
        return output;
    }

    public LinkedHashSet<City> citySet(List<String> cityList) {
        LinkedHashSet<City> set = new LinkedHashSet<>();
            for (int i = 0; i < cityList.size(); i++){
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "SELECT * FROM cities WHERE name = ?");

                    preparedStatement.setString(1, cityList.get(i));

                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();

                    City city = new City(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("latitude"),
                            resultSet.getDouble("longitude")
                    );
                    if (!set.contains(city)) {
                        set.add(city);
                    }
                } catch (SQLException throwables) {
                    //If no such city in DB => delete it from list
                    System.out.println("No such city " + cityList.get(i));
                    cityList.remove(cityList.get(i));
                    set = citySet(cityList);
                }
            }
        return set;
    }

    public LinkedHashMap<String[], Double> getDistance(Set<City> origin, Set<City> destination) {
        Double output = -1d;
        LinkedHashMap<String[], Double> toOutput = new LinkedHashMap<>(); //Mapping for couple cities and distance between them.

        for (City i : origin) {
            for (City j : destination) {
                try {

                    String[] citiesNames = new String[2];
                    citiesNames[0] = i.getName();
                    citiesNames[1] = j.getName();

                    if (i == j) {
                        output = 0d;
                        toOutput.put(citiesNames, output);
                        break;
                    }

                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "SELECT distance FROM distances WHERE from_city = ? AND to_city = ?");

                    preparedStatement.setLong(1, i.getId());
                    preparedStatement.setLong(2, j.getId());

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (!resultSet.next()) {     //if no measurements found => calculate new distance
                        output = Calculation.getDistance(
                                i.getLatitude(),
                                i.getLongitude(),
                                j.getLatitude(),
                                j.getLongitude()
                        );
                        PreparedStatement ps = connection.prepareStatement(
                                "INSERT INTO distances (from_city, to_city, distance) VALUES (?, ?, ?)"
                        );

                        ps.setLong(1, i.getId());
                        ps.setLong(2, j.getId());
                        ps.setDouble(3, output);

                        ps.executeUpdate();     //then write it to database
                    }
                    output = resultSet.getDouble("distance");
                    toOutput.put(citiesNames, output);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return toOutput;
    }
}
