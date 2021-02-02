package com.vikmak.distance.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Viktor Makarov
 */
@XmlRootElement
@Entity
@Table(name = "distances")
public class Distance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn(name = "from_city")
    @ManyToOne(targetEntity = City.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private long origin;

    @JoinColumn(name = "to_city")
    @ManyToOne(targetEntity = City.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private long destination;

    @Column(name = "distance")
    private double distance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrigin() {
        return origin;
    }

    public void setOrigin(long origin) {
        this.origin = origin;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Distance distance1 = (Distance) o;

        if (id != distance1.id) return false;
        if (origin != distance1.origin) return false;
        if (destination != distance1.destination) return false;
        return Double.compare(distance1.distance, distance) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (origin ^ (origin >>> 32));
        result = 31 * result + (int) (destination ^ (destination >>> 32));
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
