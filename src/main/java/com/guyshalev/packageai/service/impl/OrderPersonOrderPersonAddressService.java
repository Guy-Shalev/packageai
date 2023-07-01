package com.guyshalev.packageai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guyshalev.packageai.dal.IAddressDAL;
import com.guyshalev.packageai.model.OrderPersonAddress;
import com.guyshalev.packageai.model.dto.GeocodingLocation;
import com.guyshalev.packageai.model.response.ClosestPersonResponse;
import com.guyshalev.packageai.service.IOrderPersonAddressService;
import com.guyshalev.packageai.util.Utils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
public class OrderPersonOrderPersonAddressService implements IOrderPersonAddressService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderPersonOrderPersonAddressService.class);
    public static final String CANNOT_ACCESS_FILE = "Cannot access file in path: ";
    public static final String CANNOT_FIND_FIELD = "Cannot find field: ";
    public static final String CANNOT_CONVERT_FILE = "Cannot convert file to Json";
    public static final String CANNOT_CONVERT_API_CALL_RESPONSE = "Cannot convert API call to object";
    public static final String GEOCODING_API_SEARCH_BY_ADDRESS_URL = "https://geocode.maps.co/search";
    public static final String GEOCODING_API_SEARCH_BY_ADDRESS_PARAM = "q";
    public static final String FILE_IS_EMPTY = "File {} is empty";
    public static final String FILES_PATH = "src/main/resources/";
    public static final String FILE_NAME = "sample_data.csv";

    @Autowired
    private IAddressDAL addressDAL;

    /**
     * Loading a file to DB (H2 in this case)
     */
    @Override
    public void loadAddresses() {
        List<OrderPersonAddress> orderPersonAddress = readFile();
        addressDAL.saveAll(orderPersonAddress);
    }

    @Override
    public ClosestPersonResponse findNearestNPeopleToAddress(String address, int nearestNPeople) {
        ImmutablePair<Double, Double> addressLocation = getAddressLocation(address);


        return null;
    }

    /**
     * Calling "https://geocode.maps.co/" to get the address coordinates
     * In a real world scenario (given more time) this call would be part of a system that can change the API vendor
     * easily by implementing Factory design pattern to get an interface for different API vendors.
     *
     * @param address - the address to search
     * @return - the address coordinates
     */
    private ImmutablePair<Double, Double> getAddressLocation(String address) {
        List<GeocodingLocation> geocodingLocations = null;

        try {
            String geocodingResponse = Utils.callRestApi(GEOCODING_API_SEARCH_BY_ADDRESS_URL, Map.of(GEOCODING_API_SEARCH_BY_ADDRESS_PARAM, address));
            // convert Json response to object
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            geocodingLocations = objectMapper.readValue(geocodingResponse, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            LOG.error(CANNOT_CONVERT_API_CALL_RESPONSE + " Because of: "
                    + e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
        }

        Double lat = null;
        Double lon = null;

        if (isNotEmpty(geocodingLocations)) {
            lat = Double.valueOf(geocodingLocations.get(0).getLat());
            lon = Double.valueOf(geocodingLocations.get(0).getLon());
        }

        return new ImmutablePair<>(lat, lon);
    }

    /**
     * Calculate the distance between two points on Earth
     *
     * @param lat1 - location 1 latitude
     * @param lon1 - location 1 longitude
     * @param lat2 - location 2 latitude
     * @param lon2 - location 2 longitude
     * @return the distance between two points on Earth
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // Radius of the Earth in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c; // Distance in kilometers
    }

    @Override
    public String findNearest3PeopleToAddress(String address) {
        ClosestPersonResponse closestPersonResponse = findNearestNPeopleToAddress(address, 3);
        String json = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(closestPersonResponse);
        } catch (JsonProcessingException e) {
            LOG.error(CANNOT_CONVERT_FILE + " Because of: "
                    + e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
        }

        return json;
    }

    /**
     * Get a list of OrderPersonAddress from file
     *
     * @return A list of OrderPersonAddress from file
     */
    private List<OrderPersonAddress> readFile() {
        Path path = Paths.get(FILES_PATH + FILE_NAME);
        List<OrderPersonAddress> orderPersonAddress = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(path);
            if (isEmpty(lines)) {
                LOG.error(FILE_IS_EMPTY, path);
            } else {
                // removing the first line because it's a header (order,name,address)
                lines.remove(0);
                orderPersonAddress = lines.stream()
                        .map(this::parseLineToObject)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            LOG.error(CANNOT_ACCESS_FILE + path + " Because of: "
                    + e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
        }

        return orderPersonAddress;
    }

    /**
     * Convert A line from a file to An {@code OrderPersonAddress} object
     *
     * @param line - A line from a file containing order number, person name and address
     * @return - An {@code OrderPersonAddress} object populated with data from the line
     */
    private OrderPersonAddress parseLineToObject(String line) {
        // Parse the line and create an instance of OrderPersonAddress
        // Assuming the line format is "order,name,address"
        String[] fields = line.split(",");
        String order = getField(fields, "order", 0);
        String name = getField(fields, "name", 1);
        String address = getField(fields, "address", 2);
        ImmutablePair<String, String> firstAndLastName = getFirstAndLastName(name);

        // Create an instance of OrderPersonAddress using the parsed fields
        return new OrderPersonAddress(firstAndLastName.getLeft(), firstAndLastName.getRight(), order, address);
    }

    /**
     * Get a field from an array representing a line in a document
     *
     * @param fields    - an array representing a line in a document
     * @param fieldName - the field name
     * @param index     - the field index in the line
     * @return - the field found in the line
     */
    private String getField(String[] fields, String fieldName, int index) {
        String field = "";
        try {
            field = fields[index];
        } catch (Exception e) {
            LOG.error(CANNOT_FIND_FIELD + fieldName + " Because of: "
                    + e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
        }
        return field;
    }

    /**
     * Returns the first name and last name of a person
     *
     * @param name - the name of a person consisting of first and last name separated by space
     * @return - a pair of first name and last name
     */
    private ImmutablePair<String, String> getFirstAndLastName(String name) {
        String[] fields = name.split(" ");
        String first = "";
        String second = "";

        if (isNotEmpty(fields)) {
            first = fields[0];
            if (fields.length == 2) {
                second = fields[1];
            }
        }

        return new ImmutablePair<>(first, second);
    }


}
