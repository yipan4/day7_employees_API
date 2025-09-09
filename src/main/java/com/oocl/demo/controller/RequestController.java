package com.oocl.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.oocl.demo.model.ParkingBoy;
import com.oocl.demo.service.ParkingBoyService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RestController("parking-boys")
public class RequestController {
    private final ParkingBoyService parkingBoyService = new ParkingBoyService();

    @PostMapping("/")
    public JsonNode createParkingBoy(@RequestBody JsonNode body) {
        ParkingBoy parkingBoy = new ParkingBoy(String.valueOf(body.get("name")));
        parkingBoyService.addParkingBoy(parkingBoy);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("id", String.valueOf(parkingBoy.getUUID()));
        return response;
    }

    @GetMapping("/")
    public JsonNode listExistingParkingBoys() {
        return parkingBoyService.listParkingBoys();
    }

//    @PostMapping("/{id}/parked-cars")
//    public JsonNode park(@PathVariable String id) {
//        ParkingBoy parkingBoy = parkingBoyService.
//    }
}
