package com.tingco.codechallenge.elevator.impl;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.Elevator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.tingco.codechallenge.elevator.api.Elevator.Direction.*;
import static com.tingco.codechallenge.elevator.impl.ElevatorEvent.EventType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorControllerImplTest {

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private ElevatorControllerImpl elevatorController;

    @Test
    public void returnAvailableElevator() {
        ElevatorImpl elevator1 = createTestElevator(0);
        ElevatorImpl elevator2 = createTestElevator(1);
        elevatorController.setElevators(Arrays.asList(elevator1, elevator2));
        elevator2.moveElevator(9);

        Elevator requestedElevator = elevatorController.requestElevator(10);
        assertEquals(elevator1, requestedElevator);
    }

    @Test
    public void returnUnavailableElevator() {
        elevatorController.setElevators(Collections.emptyList());

        assertNull(elevatorController.requestElevator(10));
        assertFalse(elevatorController.getPendingRequests().isEmpty());

        verify(eventBus, times(1))
                .post(new ElevatorEventBuilder()
                        .setEventType(PENDING_REQUEST)
                        .build());
    }

    @Test
    public void returnClosestElevator() {
        ElevatorImpl elevator = createTestElevatorAtFloor(0, 9);
        ElevatorImpl elevator2 = createTestElevatorAtFloor(1, 6);
        elevatorController.setElevators(Arrays.asList(elevator, elevator2));
        elevatorController.requestElevator(7);

        // The first elevator shouldn't move
        assertEquals(9, elevator.getAddressedFloor());
        assertEquals(NONE, elevator.getDirection());

        // The second elevator should target 7th floor as it is closest
        assertEquals(UP, elevator2.getDirection());
        assertEquals(7, elevator2.getAddressedFloor());

        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ASSIGNED)
                .setId(1)
                .build());
    }


    @Test
    public void returnClosestAvailableElevator() {
        ElevatorImpl elevator = createTestElevator(0);
        ElevatorImpl elevator2 = createTestElevator(1);
        elevatorController.setElevators(Arrays.asList(elevator, elevator2));
        elevatorController.requestElevator(6);
        elevator.operate(); // Move first elevator 1 floor
        elevatorController.requestElevator(4);

        // Second elevator will be assigned as the first elevator is busy, even if its closer
        assertEquals(UP, elevator.getDirection());
        assertEquals(6, elevator.getAddressedFloor());
        assertEquals(4, elevator2.getAddressedFloor());

        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ASSIGNED)
                .setId(0)
                .build());
        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ASSIGNED)
                .setId(1)
                .build());
    }


    @Test
    public void resetDirectionAfterReachingToFloor() {
        ElevatorImpl elevator = createTestElevator(0);
        elevatorController.setElevators(Collections.singletonList(elevator));
        elevatorController.requestElevator(3);
        elevator.operate(); // Move elevator 3 floors
        elevator.operate();
        elevator.operate();

        assertEquals(NONE, elevator.getDirection());
        assertEquals(3, elevator.currentFloor());

        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ASSIGNED)
                .setId(0)
                .build());
    }

    @Test
    public void resetElevatorState() {
        ElevatorImpl elevator = createTestElevator(0);
        elevatorController.setElevators(Collections.singletonList(elevator));
        elevatorController.requestElevator(10);
        elevator.operate(); // Move elevator 1 floor
        elevatorController.releaseElevator(elevator);

        assertEquals(NONE, elevator.getDirection());
        assertEquals(1, elevator.currentFloor());

        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(RESETED)
                .setId(0)
                .build());
    }

    private ElevatorImpl createTestElevator(final int id) {
        return createTestElevatorAtFloor(id, 0);
    }

    private ElevatorImpl createTestElevatorAtFloor(final int id, final int currentFloor) {
        return new ElevatorImpl(id, currentFloor, 10, eventBus);
    }
}