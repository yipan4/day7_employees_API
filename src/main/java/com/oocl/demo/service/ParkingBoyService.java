package com.oocl.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import com.oocl.demo.model.ParkingBoy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ParkingBoyService {
    private final List<ParkingBoy> parkingBoyList = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    public void addParkingBoy(ParkingBoy parkingBoy) {
        parkingBoyList.add(parkingBoy);
    }

    public JsonNode listParkingBoys() {
        ArrayNode response = mapper.createArrayNode();
        for (ParkingBoy parkingBoy: parkingBoyList) {
            ObjectNode parkingBodyInfo = mapper.createObjectNode();
            parkingBodyInfo.put("id",String.valueOf(parkingBoy.getUUID()));
            parkingBodyInfo.put("name", parkingBoy.getName());
            response.add(parkingBodyInfo);
        }
        return response;
    }

    public ParkingBoy findParkingBoyWithUUID(String uuid) {
        UUID id = UUID.fromString(uuid);
        return (ParkingBoy) parkingBoyList.stream().filter(parkingBoy -> parkingBoy.getUUID().equals(id));
    }
}
