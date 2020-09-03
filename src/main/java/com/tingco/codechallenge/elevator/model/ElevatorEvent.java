package com.tingco.codechallenge.elevator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElevatorEvent {

    private final int id;
    private final EventType eventType;

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
