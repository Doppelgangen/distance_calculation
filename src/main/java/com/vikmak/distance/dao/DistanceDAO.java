package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;
import com.vikmak.distance.services.CityService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * @author Viktor Makarov
 */

@Path("/distance")
public class DistanceDAO {

    Connection connection = CityService.getConnection();

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMethod(
            @FormParam("calculationType")String calculationType,
            @FormParam("cityFrom") String cityFrom,
            @FormParam("cityTo") String cityTo){

        List<String> origins = Arrays.asList(cityFrom.split("\\W+"));
        List<String> destinations = Arrays.asList(cityTo.split("\\W+"));
/*
        switch (calculationType) {
            case "Crowflight":
                showCroflight(List<String> origins, List<String> destinations);
                break;
            case "Distance Matrix":
                showDistanceMatrix();
                break;
            case "All":
                showAll();
                break;
            default:
                Response.noContent().build();
        }
*/
        return Response.status(200)
                .entity("addUser is called, name : " + calculationType + cityFrom + cityTo)
                .build();
    }



}
