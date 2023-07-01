package com.guyshalev.packageai.service;

import com.guyshalev.packageai.model.response.ClosestPersonResponse;
import org.springframework.stereotype.Service;

@Service
public interface IOrderPersonAddressService {

    void loadAddresses();

    ClosestPersonResponse findNearestNPeopleToAddress(String address, int nearestNPeople);
    String findNearest3PeopleToAddress(String address);

}
