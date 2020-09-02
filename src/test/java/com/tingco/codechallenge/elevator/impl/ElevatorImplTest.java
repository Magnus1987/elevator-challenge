package com.tingco.codechallenge.elevator.impl;

import com.google.common.eventbus.EventBus;
import org.junit.Test;
import org.mockito.Mockito;

import static com.tingco.codechallenge.elevator.api.Elevator.Direction.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ElevatorImplTest {

    private static final int MIN_FLOOR = 0;
    private static final int MAX_FLOOR = 5;

    private ElevatorImpl elevator = new ElevatorImpl(1, MIN_FLOOR, MAX_FLOOR, Mockito.mock(EventBus.class));

    @Test
    public void verifyCorrectInitialized() {
        assertEquals(NONE, elevator.getDirection());
        assertEquals(MIN_FLOOR, elevator.currentFloor());
        assertEquals(MIN_FLOOR, elevator.getAddressedFloor());
        assertEquals(false, elevator.isBusy());
    }

    @Test
    public void stopAtAdressedFloor() {
        elevator.moveElevator(MIN_FLOOR + 1);
        elevator.operate();

        assertEquals(NONE, elevator.getDirection());
        assertEquals(MIN_FLOOR + 1, elevator.getAddressedFloor());
        assertEquals(MIN_FLOOR + 1, elevator.currentFloor());
        assertEquals(false, elevator.isBusy());
    }

    @Test
    public void moveElevatorUpwards() {
        elevator.moveElevator(MAX_FLOOR);

        assertEquals(UP, elevator.getDirection());
        assertEquals(MAX_FLOOR, elevator.getAddressedFloor());
        assertEquals(true, elevator.isBusy());
    }

    @Test
    public void moveElevatorDownwards() {
        elevator.moveElevator(MIN_FLOOR + 2);
        elevator.operate();
        elevator.operate();
        elevator.moveElevator(MIN_FLOOR);
        elevator.operate();

        assertEquals(DOWN, elevator.getDirection());
        assertEquals(MIN_FLOOR, elevator.getAddressedFloor());
        assertEquals(MIN_FLOOR + 1, elevator.currentFloor());
        assertEquals(true, elevator.isBusy());
    }

    @Test
    public void cannotPassMaxFloor() {
        elevator.moveElevator(MAX_FLOOR + 1);

        assertEquals(NONE, elevator.getDirection());
        assertEquals(MIN_FLOOR, elevator.getAddressedFloor());
        assertEquals(MIN_FLOOR, elevator.currentFloor());
        assertEquals(false, elevator.isBusy());
    }

    @Test
    public void cannotPassMinFloor() {
        elevator.moveElevator(MIN_FLOOR - 1);

        assertEquals(NONE, elevator.getDirection());
        assertEquals(MIN_FLOOR, elevator.getAddressedFloor());
        assertEquals(MIN_FLOOR, elevator.currentFloor());
        assertEquals(false, elevator.isBusy());
    }

    @Test
    public void cannotMoveIfSameFloor() {
        elevator.moveElevator(MIN_FLOOR);

        assertEquals(NONE, elevator.getDirection());
        assertEquals(MIN_FLOOR, elevator.getAddressedFloor());
        assertEquals(MIN_FLOOR, elevator.currentFloor());
        assertEquals(false, elevator.isBusy());
    }

    @Test
    public void continueInRequestQueue() {
        elevator.moveElevator(MIN_FLOOR + 1);
        elevator.moveElevator(MIN_FLOOR + 2);
        elevator.operate();

        assertEquals(UP, elevator.getDirection());
        assertEquals(MIN_FLOOR + 2, elevator.getAddressedFloor());
        assertEquals(MIN_FLOOR + 1, elevator.currentFloor());
        assertEquals(true, elevator.isBusy());
    }

    @Test
    public void nothingAfterCompletedQueue() {
        elevator.moveElevator(MAX_FLOOR);
        for (int i = 0; i < MAX_FLOOR; i++) {
            elevator.operate();
        }
        // Operate one extra time without any pending requests
        elevator.operate();

        assertEquals(NONE, elevator.getDirection());
        assertEquals(MAX_FLOOR, elevator.getAddressedFloor());
        assertEquals(MAX_FLOOR, elevator.currentFloor());
        assertFalse(elevator.isBusy());
    }

    @Test
    public void resetElevator() {
        elevator.moveElevator(MIN_FLOOR + 1);
        elevator.moveElevator(MIN_FLOOR + 2);
        elevator.operate();
        elevator.reset();

        assertEquals(NONE, elevator.getDirection());
        assertEquals(1, elevator.getAddressedFloor());
        assertEquals(MIN_FLOOR + 1, elevator.currentFloor());
        assertEquals(false, elevator.isBusy());
    }
}
