package com.vikmak.distance.dao;

import com.vikmak.distance.entity.City;
import com.vikmak.distance.services.DistanceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Viktor Makarov
 */

@Path("/distance")
public class DistanceDAO {

    DistanceService distanceService = new DistanceService();

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMethod(
            @FormParam("calculationType") String calculationType,
            @FormParam("cityFrom") String cityFrom,
            @FormParam("cityTo") String cityTo) {

        List<String> origins = Arrays.asList(cityFrom.split("\\W+"));
        List<String> destinations = Arrays.asList(cityTo.split("\\W+"));

        switch (calculationType) {
            case "Crowflight":
                distanceService.getMapping(origins, destinations);
           /* case "Distance Matrix":
                showDistanceMatrix(origins, destinations);
                break;
            case "All":
                showAll(origins, destinations);
                break;*/
            default:
                Response.noContent().build();
        }
        return Response.status(200)
                .entity("addUser is called, name : " + calculationType + cityFrom + cityTo)
                .build();
    }

    public Response test() throws URISyntaxException {
        return Response.temporaryRedirect(new URI("/")).build();
    }
}
