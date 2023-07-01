package com.guyshalev.packageai.model.dto;

public class ClosestPerson {

    private String order;
    private String firstName;
    private String lastName;
    private String DistanceToAddress;

    public ClosestPerson() {
    }

    public ClosestPerson(String order, String firstName, String lastName, String distanceToAddress) {
        this.order = order;
        this.firstName = firstName;
        this.lastName = lastName;
        DistanceToAddress = distanceToAddress;
    }

    public String getDistanceToAddress() {
        return DistanceToAddress;
    }

    public void setDistanceToAddress(String distanceToAddress) {
        DistanceToAddress = distanceToAddress;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
