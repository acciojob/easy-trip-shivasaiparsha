package com.driver.controllers;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {
    HashMap<String,Airport>AirportDB=new HashMap<>();
    HashMap<Integer, Flight>FlightDb=new HashMap<>();
    HashMap<Integer,Passenger>PassengerDb=new HashMap<>();
    HashMap<Integer, List<Integer>>flighttopassengersDb=new HashMap<>();
    HashMap<Integer,Integer>canceltikets=new HashMap<>();
    public void addAirport(Airport airport) {
        String name= airport.getAirportName();
        AirportDB.put(name,airport);
    }
    public String addFlight(Flight flight) {
        //Return a "SUCCESS" message string after adding a flight.
        FlightDb.put(flight.getFlightId(), flight);
        return "SUCCESS";
    }
    public String addPassenger(Passenger passenger) {
        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
        int id= passenger.getPassengerId();
        PassengerDb.put(id,passenger);
        return "SUCCESS";
    }
    public String getLargestAirport() {
        //Largest airport is in terms of terminals. 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName
        int noofterminals=0;
        String name="";
        for(String n:AirportDB.keySet()){
            if(AirportDB.get(n).getNoOfTerminals()>noofterminals){
                noofterminals=AirportDB.get(n).getNoOfTerminals();
                name=n;
            }else if(AirportDB.get(n).getNoOfTerminals()==noofterminals){
                if(name.compareTo(n)>0){
                    name=n;
                }
            }
        }
        return name;
    }

    public double getShortestDurationPossibleBetweenTwoCities(City fromCity, City toCity) {
        //Find the duration by finding the shortest flight that connects these 2 cities directly
        //If there is no direct flight between 2 cities return -1.
        double duration=-1;
        for(int id: FlightDb.keySet()){
            if(FlightDb.get(id).getFromCity().equals(fromCity)&&FlightDb.get(id).getToCity().equals(toCity)){

                if(duration==-1){
                    duration=FlightDb.get(id).getDuration();
                }else{
                    duration=Math.min(duration,FlightDb.get(id).getDuration());
                }
            }
        }
        return duration;
    }


    public int getNumberOfPeopleOn(Date date, String airportName) {
        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight
        int noOfpersons=0;
        if(AirportDB.containsKey(airportName)){
            City city=AirportDB.get(airportName).getCity();
            for(Flight flight: FlightDb.values()){
                if((flight.getToCity().equals(city)||flight.getFromCity().equals(city))&&flight.getFlightDate().equals(date)){
                    int id=flight.getFlightId();
                    noOfpersons+=flighttopassengersDb.getOrDefault(id,new ArrayList<>()).size();
                }
            }
        }
        return noOfpersons;
    }


    public String bookTicket(Integer flightId, Integer passengerId) {
        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"
        List<Integer>passengers=flighttopassengersDb.getOrDefault(flightId,new ArrayList<>());
        if(passengers.size()<FlightDb.get(flightId).getMaxCapacity()){
            if(passengers.contains(passengerId)){
                return "FAILURE";
            }else{
                passengers.add(passengerId);
                flighttopassengersDb.put(flightId,passengers);
                return "SUCCESS";
            }
        }
        return "FAILURE";
    }

    public String cancelTicket(Integer flightId, Integer passengerId) {
        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId
        if(flighttopassengersDb.containsKey(flightId)){
            List<Integer>passengers=flighttopassengersDb.get(flightId);
            if(passengers.contains(passengerId)){
                passengers.remove(passengerId);
                flighttopassengersDb.put(flightId,passengers);
                canceltikets.put(flightId,canceltikets.getOrDefault(flightId,0)+1);
                return "SUCCESS";
            }else{
                return "FAILURE";
            }
        }
        return "FAILURE";
    }

    public int calcluateFlightFare(Integer flightId) {
        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price
        int fare=0;
        if(flighttopassengersDb.containsKey(flightId)){
            fare=3000+(flighttopassengersDb.get(flightId).size()*50);
        }
        return fare;
    }

    public int countBookingsofPassenger(Integer passengerId) {
        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        int count=0;
        for(List<Integer>passenger: flighttopassengersDb.values()){
            if(passenger.contains(passengerId)){
                count++;
            }
        }
        return count;
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        //We need to get the starting airportName from where the flight will be taking off (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName
        if(FlightDb.containsKey(flightId)){
            City city=FlightDb.get(flightId).getFromCity();
            for(Airport airport: AirportDB.values()){
                if(airport.getCity().equals(city)){
                    return airport.getAirportName();
                }
            }
            return null;
        }
        return null;
    }

    public int calculateRevenueofaFlight(Integer flightId) {
        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight
        int cancelfare = canceltikets.getOrDefault(flightId, 1) * 50;

        return calcluateFlightFare(flightId) - cancelfare;
    }
}
