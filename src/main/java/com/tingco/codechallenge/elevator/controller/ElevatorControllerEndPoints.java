package com.tingco.codechallenge.elevator.controller;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints implements ElevatorEndPoints {

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Value("${com.tingco.elevator.minFloor}")
    private int minFloor;

    @Value("${com.tingco.elevator.maxFloor}")
    private int maxFloor;

    @NonNull
    private ElevatorController elevatorController;

    @Override
    @RequestMapping(value = "/request/{toFloor}", method = RequestMethod.POST)
    public ResponseEntity<Elevator> requestElevator(@PathVariable Integer toFloor) {
        ResponseEntity<Elevator> response;
        if (toFloor < minFloor || toFloor > maxFloor) {
            response = ResponseEntity.badRequest().build();
        } else {
            response = ResponseEntity.ok(elevatorController.requestElevator(toFloor));
        }
        return response;
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Elevator> getElevators() {
        return elevatorController.getElevators();
    }

    @Override
    @RequestMapping(value = "/release/{id}", method = RequestMethod.POST)
    public ResponseEntity<Boolean> releaseElevator(@PathVariable Integer id) {
        ResponseEntity<Boolean> response;
        if (id < 0 || id >= numberOfElevators) {
            response = ResponseEntity.notFound().build();
        } else {
            elevatorController.releaseElevator(elevatorController.getElevators().get(id));
            response = ResponseEntity.ok(Boolean.TRUE);
        }
        return response;
    }

    @Override
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }
}
