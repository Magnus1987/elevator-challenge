package com.tingco.codechallenge.elevator.controller;

import com.tingco.codechallenge.elevator.config.ElevatorApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElevatorApplication.class)
@ActiveProfiles("test")
public class ElevatorControllerEndPointsTest {

    @Autowired
    private ElevatorControllerEndPoints endPoints;

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Value("${com.tingco.elevator.minFloor}")
    private int minFloor;

    @Value("${com.tingco.elevator.maxFloor}")
    private int maxFloor;

    @Test
    public void ping() {
        Assert.assertEquals("pong", endPoints.ping());
    }

    @Test
    public void requestElevator() {
        Assert.assertEquals(minFloor, endPoints.requestElevator(minFloor).getBody().getAddressedFloor());
        Assert.assertEquals(HttpStatus.OK, endPoints.requestElevator(minFloor).getStatusCode());
    }

    @Test
    public void requestElevatorWithIncorrectLowFloor() {
        Assert.assertEquals(HttpStatus.BAD_REQUEST, endPoints.requestElevator(minFloor - 1).getStatusCode());
    }

    @Test
    public void requestElevatorWithIncorrectHighFloor() {
        Assert.assertEquals(HttpStatus.BAD_REQUEST, endPoints.requestElevator(maxFloor + 1).getStatusCode());
    }

    @Test
    public void getElevators() {
        Assert.assertEquals(numberOfElevators, endPoints.getElevators().size());
    }

    @Test
    public void releaseElevator() {
        Assert.assertEquals(ResponseEntity.ok(Boolean.TRUE), endPoints.releaseElevator(0));
    }

    @Test
    public void releaseElevatorWithMaxId() {
        Assert.assertEquals(ResponseEntity.notFound().build(), endPoints.releaseElevator(numberOfElevators));
    }

    @Test
    public void releaseElevatorWithIncorrectId() {
        Assert.assertEquals(ResponseEntity.notFound().build(), endPoints.releaseElevator(numberOfElevators + 1));
    }
}
