package com.tingco.codechallenge.elevator.service.impl;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.service.Elevator;
import com.tingco.codechallenge.elevator.model.ElevatorEventBuilder;
import lombok.ToString;

import static com.tingco.codechallenge.elevator.service.Elevator.Direction.*;
import static com.tingco.codechallenge.elevator.model.ElevatorEvent.EventType.*;

@ToString
public class ElevatorImpl implements Elevator, Runnable {

    private int id;
    private int minFloor;
    private int maxFloor;
    private EventBus eventBus;

    private Elevator.Direction direction;
    private int currentFloor;
    private int addressedFloor;

    public ElevatorImpl(final int id, final int minFloor, final int maxFloor, final EventBus eventBus) {
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.eventBus = eventBus;

        this.direction = NONE;
        this.currentFloor = minFloor;
        this.addressedFloor = minFloor;
    }

    @Override
    public void run() {
        operate();
    }

    @Override
    public Elevator.Direction getDirection() {
        return direction;
    }

    @Override
    public int getAddressedFloor() {
        return addressedFloor;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void moveElevator(final int toFloor) {
        // TODO: Improve fail handling when triggering toFloor out of bounds
        if (currentFloor == toFloor || toFloor > maxFloor || toFloor < minFloor) {
            return;
        }

        if (currentFloor < toFloor) {
            setDirection(UP);
        } else {
            setDirection(DOWN);
        }

        addressedFloor = toFloor;
    }

    @Override
    public boolean isBusy() {
        return !direction.equals(NONE);
    }

    @Override
    public int currentFloor() {
        return currentFloor;
    }

    @Override
    public void reset() {
        setDirection(NONE);
        eventBus.post(new ElevatorEventBuilder()
                .setEventType(BECAME_IDLE)
                .setId(getId())
                .build());
        addressedFloor = currentFloor;
    }

    @Override
    public void operate() {
        moveElevator();
        if (currentFloor == addressedFloor && !direction.equals(NONE)) {
            stopElevator();
        }
    }

    private void moveElevator() {
        if (direction.equals(UP)) {
            currentFloor++;
            eventBus.post(new ElevatorEventBuilder()
                    .setEventType(MOVING_UP)
                    .setId(getId())
                    .build());
        } else if (direction.equals(DOWN)) {
            currentFloor--;
            eventBus.post(new ElevatorEventBuilder()
                    .setEventType(MOVING_DOWN)
                    .setId(getId())
                    .build());
        }
    }

    private void stopElevator() {
        setDirection(NONE);
        eventBus.post(new ElevatorEventBuilder()
                .setEventType(BECAME_IDLE)
                .setId(getId())
                .build());
    }

    public void setDirection(final Elevator.Direction direction) {
        this.direction = direction;
    }
}
