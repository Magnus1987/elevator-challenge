package com.tingco.codechallenge.elevator.controller;

import com.tingco.codechallenge.elevator.api.Elevator;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ElevatorEndPoints {

    /**
     * Request {@link Elevator} to floor
     *
     * @return requested elevator
     */
    ResponseEntity<Elevator> requestElevator(final Integer toFloor);

    /**
     * Report list of {@link Elevator}
     *
     * @return list of elevators
     */
    List<Elevator> getElevators();

    /**
     * Release {@link Elevator}
     *
     * @return true if release was successful, otherwise false
     */
    ResponseEntity<Boolean> releaseElevator(final Integer id);

    /**
     * Ping service to test if we are alive.
     *
     * @return pong
     */
    String ping();
}
