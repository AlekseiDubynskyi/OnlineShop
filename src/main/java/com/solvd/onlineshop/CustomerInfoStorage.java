package com.solvd.onlineshop;

import com.solvd.onlineshop.location.City;
import com.solvd.onlineshop.location.Country;
import com.solvd.onlineshop.people.Customer;

import java.util.ArrayList;
import java.util.HashSet;

public class CustomerInfoStorage extends Customer {
    public static void main(String[] args) {
        HashSet<ArrayList> arrayCustomerInfoStorage = new HashSet<ArrayList>();

        ArrayList<Customer> customers = new ArrayList<Customer>();
        customers.add(new Customer("Petro", "Kalyna", "+380960000555","OI123141",
                "Great address to live"));

        ArrayList<Country> countries = new ArrayList<Country>();
        countries.add(new Country("Ukraine"));

        ArrayList<City> cities = new ArrayList<City>();
        cities.add(new City("Kyiv", "01031"));
        cities.add(new City("Kharkiv", "04143"));

        arrayCustomerInfoStorage.add(customers);
        arrayCustomerInfoStorage.add(countries);
        arrayCustomerInfoStorage.add(cities);

        System.out.println(customers);
        System.out.println(countries);
        System.out.println(cities);
        System.out.println(arrayCustomerInfoStorage);
    }
}