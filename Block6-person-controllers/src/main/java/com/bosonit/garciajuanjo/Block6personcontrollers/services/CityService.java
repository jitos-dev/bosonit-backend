package com.bosonit.garciajuanjo.Block6personcontrollers.services;

import com.bosonit.garciajuanjo.Block6personcontrollers.entities.City;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {

    private List<City> cities = new ArrayList<>();

    public CityService() {
        cities.add(new City("Sabiote", 3900));
        cities.add(new City("Ubeda", 40000));
        cities.add(new City("Baeza", 14000));
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public List<City> getCities() {
        return cities;
    }
}
