package com.tingco.codechallenge.elevator;

import com.tingco.codechallenge.elevator.service.Elevator;
import com.tingco.codechallenge.elevator.controller.ElevatorEndPoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.awaitility.Awaitility.await;
import static com.tingco.codechallenge.elevator.service.Elevator.Direction.NONE;
import static com.tingco.codechallenge.elevator.service.Elevator.Direction.UP;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElevatorApplication.class)
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private ElevatorEndPoints endPoints;

    @Value("${com.tingco.elevator.elevatorSpeed}")
    private int elevatorSpeed;

    @Value("${com.tingco.elevator.pollSpeed}")
    private int pollSpeed;

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Value("${com.tingco.elevator.minFloor}")
    private int minFloor;

    @Value("${com.tingco.elevator.maxFloor}")
    private int maxFloor;

    // Since the request polling and elevator movement is done asynchronous we will add the polling time to the
    // elevator speed whenever we call requestElevator.
    @Test
    public void simulateAnElevatorShaft() {
        // Send away all except the last elevator
        for (int i = 0; i < numberOfElevators - 1; i++) {
            endPoints.requestElevator(maxFloor);
        }

        // They should all be up and running
        endPoints.getElevators().stream()
                .limit(endPoints.getElevators().size() - 1) // exclude the last one
                .forEach(elevator -> awaitMovingUp(elevator));

        // Send the last elevator as the others are unavailable
        endPoints.requestElevator(5);
        awaitMovingUpFloors(4, endPoints.getElevators().get(numberOfElevators - 1));

        // Make another request to ground floor. Since the last elevator will reach its target quicker it will be able
        // to pick up this request before the other elevators. In total it will travel 6 floors before completion.
        endPoints.requestElevator(minFloor);
        awaitReachFloor(6, minFloor, endPoints.getElevators().get(numberOfElevators - 1));

        // The other elevators take about elevatorSpeed * maxFloor seconds to reach their target
        endPoints.getElevators().stream()
                .limit(endPoints.getElevators().size() - 1) // exclude the last one
                .forEach(elevator -> awaitReachFloor(maxFloor, maxFloor, elevator));
    }

    private void awaitMovingUp(final Elevator elevator) {
        await().atMost(elevatorSpeed + pollSpeed, SECONDS).until(() ->
                elevator.isBusy() && elevator.getDirection().equals(UP));
    }

    private void awaitMovingUpFloors(final int floors, final Elevator elevator) {
        await().atMost((elevatorSpeed * floors) + pollSpeed, SECONDS).until(() ->
                elevator.currentFloor() == floors
                        && elevator.isBusy()
                        && elevator.getDirection().equals(UP));
    }

    private void awaitReachFloor(final int floors, final int targetFloor, final Elevator elevator) {
        await().atMost((elevatorSpeed * floors) + pollSpeed, SECONDS).until(() ->
                elevator.currentFloor() == targetFloor
                        && !elevator.isBusy()
                        && elevator.getDirection().equals(NONE));
    }
}
