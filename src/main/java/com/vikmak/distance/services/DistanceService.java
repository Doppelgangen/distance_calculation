package com.vikmak.distance.services;

import com.vikmak.distance.entity.City;
import com.vikmak.distance.utils.Calculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Viktor Makarov
 */
public class DistanceService {

    Connection connection = CityService.getConnection();

    private static DistanceService instance;

    public static synchronized DistanceService getInstance() {
        if (instance == null) {
            instance = new DistanceService();
        }
        return instance;
    }

    public LinkedHashMap<String[], Double> getMapping(List<String> origins, List<String> destinations) {
        //Pre sorting for LinkedHashMap
        Collections.sort(origins);
        Collections.sort(destinations);

        LinkedHashMap<String[], Double> output = new LinkedHashMap<>();

        LinkedHashMap<Long, City> originsMap = cityMapping(origins);
        LinkedHashMap<Long, City> destinationsMap = cityMapping(destinations);

        output = getDistance(originsMap, destinationsMap);
        return output;
    }

    public LinkedHashMap<Long, City> cityMapping(List<String> cityList) {
        LinkedHashMap<Long, City> map = new LinkedHashMap<>();
        try {
            for (String s : cityList) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM cities WHERE name = ?");

                preparedStatement.setString(1, s);

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();

                City city = new City(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude")
                );
                map.put(city.getId(), city);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return map;
    }

    public LinkedHashMap<String[], Double> getDistance(Map<Long, City> origin, Map<Long, City> destination) {
        Double output = -1d;
        LinkedHashMap<String[], Double> toOutput = new LinkedHashMap<>(); //Mapping for couple cities and distance between them.

        for (Long i : origin.keySet()) {
            for (Long j : destination.keySet()) {
                try {

                    String[] citiesNames = new String[2];
                    citiesNames[0] = origin.get(i).getName();
                    citiesNames[1] = destination.get(j).getName();

                    if (i == j) {
                        output = 0d;
                        toOutput.put(citiesNames, output);
                        break;
                    }

                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "SELECT distance FROM distances WHERE from_city = ? AND to_city = ?");


                    preparedStatement.setLong(1, origin.get(i).getId());
                    preparedStatement.setLong(2, destination.get(j).getId());

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (!resultSet.next()) {     //if no measurements found => calculate new distance
                        output = Calculation.getDistance(
                                origin.get(i).getLatitude(),
                                origin.get(i).getLongitude(),
                                destination.get(j).getLatitude(),
                                destination.get(j).getLongitude()
                        );
                        PreparedStatement ps = connection.prepareStatement(
                                "INSERT INTO distances (from_city, to_city, distance) VALUES (?, ?, ?)"
                        );

                        ps.setLong(1, i);
                        ps.setLong(2, j);
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
