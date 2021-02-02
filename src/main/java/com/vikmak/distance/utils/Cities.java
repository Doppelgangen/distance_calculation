package com.vikmak.distance.utils;

import com.vikmak.distance.entity.City;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Viktor Makarov
 */

//Adding collection of cities for presentation in xml and unmarshaller
@XmlRootElement(name = "cities")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cities {

    @XmlElement(name = "city")
    private List<City> cities = null;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
