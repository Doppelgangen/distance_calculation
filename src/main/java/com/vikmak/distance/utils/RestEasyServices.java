package com.vikmak.distance.utils;

import com.vikmak.distance.dao.CityDAO;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Viktor Makarov
 */
@ApplicationPath("/rest")
public class RestEasyServices extends Application {

    private Set<Object> singletons = new HashSet<Object>();

    public RestEasyServices() {
        singletons.add(new CityDAO());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}