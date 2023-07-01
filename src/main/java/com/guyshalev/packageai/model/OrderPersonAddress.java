package com.guyshalev.packageai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_person_address")
public class OrderPersonAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "address")
    private String address;

    public OrderPersonAddress() {
    }

    public OrderPersonAddress(String firstName, String lastName, String orderNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.orderNumber = orderNumber;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
