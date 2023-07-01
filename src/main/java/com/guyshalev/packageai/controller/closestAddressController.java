package com.guyshalev.packageai.controller;

import com.guyshalev.packageai.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("address")
public class closestAddressController {

    @Autowired
    private IAddressService addressService;

    @PutMapping("/load")
    public void loadAddresses() {
        addressService.loadAddresses();
    }
}
