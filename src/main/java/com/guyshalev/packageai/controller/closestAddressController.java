package com.guyshalev.packageai.controller;

import com.guyshalev.packageai.model.response.ClosestPersonResponse;
import com.guyshalev.packageai.service.IOrderPersonAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("address")
public class closestAddressController {

    @Autowired
    private IOrderPersonAddressService orderPersonAddressService;

    @PutMapping("/load")
    public void loadAddresses() {
        orderPersonAddressService.loadAddresses();
    }

    @GetMapping("/nearestNPeopleToAddress")
    public ClosestPersonResponse findNearestNPeopleToAddress(@RequestParam("address") String address, @RequestParam("nearestNPeople") int nearestNPeople) {
        return orderPersonAddressService.findNearestNPeopleToAddress(address, nearestNPeople);
    }

    @GetMapping("/nearest3PeopleToAddress")
    public String findNearest3PeopleToAddress(@RequestParam("address") String address) {
        return orderPersonAddressService.findNearest3PeopleToAddress(address);
    }
}
