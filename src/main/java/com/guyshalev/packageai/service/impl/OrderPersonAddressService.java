package com.guyshalev.packageai.service.impl;

import com.guyshalev.packageai.dal.IAddressDAL;
import com.guyshalev.packageai.model.OrderPersonAddress;
import com.guyshalev.packageai.service.IAddressService;
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
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
public class OrderPersonAddressService implements IAddressService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderPersonAddressService.class);
    public static final String CANNOT_ACCESS_FILE = "Cannot access file in path: ";
    public static final String CANNOT_FIND_FIELD = "Cannot find field: ";
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
