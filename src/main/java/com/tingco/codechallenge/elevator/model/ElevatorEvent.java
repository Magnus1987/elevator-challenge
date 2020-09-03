package com.tingco.codechallenge.elevator.model;

import lombok.Data;

@Data
public class ElevatorEvent {

    private int id;
    private EventType eventType;

    public enum EventType {
        ASSIGNED,
        PENDING_REQUEST,
        MOVING_UP,
        MOVING_DOWN,
        BECAME_IDLE,
        RESETED
    }

    public ElevatorEvent(final int id, final EventType eventType) {
        this.id = id;
        this.eventType = eventType;
    }
}
