package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AirportService {

    private AirportRepository repo=new AirportRepository();



    public void addAirport(Airport airport) {
        repo.addAirport(airport);
    }

    public String getLargestAirport() {
        return repo.getLargestAirport();
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        return repo.getShortestDurationPossibleBetweenTwoCities(fromCity,toCity);
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        return repo.getNumberOfPeopleOn(date,airportName);
    }

    public String addPassenger(Passenger passenger) {
        return repo.addPassenger(passenger);
    }

    public String addFlight(Flight flight) {
        return repo.addFlight(flight);
    }

    public String bookTicket(Integer flightId, Integer passengerId) {
        return repo.bookTicket(flightId,passengerId);
    }

    public String cancelTicket(Integer flightId, Integer passengerId) {
        return repo.cancelTicket(flightId,passengerId);
    }

    public int calculateFlightFare(Integer flightId) {
        return repo.calcluateFlightFare(flightId);
    }

    public int countBookingsofPassenger(Integer passengerId) {
        return repo.countBookingsofPassenger(passengerId);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        return repo.getAirportNameFromFlightId(flightId);
    }

    public int calculateRevenueOfaFlight(Integer flightId) {
        return repo.calculateRevenueofaFlight(flightId);
    }
}
