package com.vikmak.distance.dao;

import com.vikmak.distance.services.CityService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;

/**
 * @author Viktor Makarov
 */

@Path("/distance")
public class DistanceDAO {

    Connection connection = CityService.getConnection();

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMethod(@FormParam("CalculationType")String string){

        return Response.status(200)
                .entity("addUser is called, name : " + string)
                .build();
    }

}
