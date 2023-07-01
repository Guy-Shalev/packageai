package com.guyshalev.packageai.model.response;

import com.guyshalev.packageai.model.dto.ClosestPerson;

import java.util.List;

public class ClosestPersonResponse {

    List<ClosestPerson> closestPersonsToAddress;

    public ClosestPersonResponse() {
    }

    public ClosestPersonResponse(List<ClosestPerson> closestPersonsToAddress) {
        this.closestPersonsToAddress = closestPersonsToAddress;
    }

    public List<ClosestPerson> getClosestPersonsToAddress() {
        return closestPersonsToAddress;
    }

    public void setClosestPersonsToAddress(List<ClosestPerson> closestPersonsToAddress) {
        this.closestPersonsToAddress = closestPersonsToAddress;
    }
}
