package com.tingco.codechallenge.elevator.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElevatorEvent {

    private int id;
    private EventType eventType;

    enum EventType {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ElevatorEvent event = (ElevatorEvent) obj;
        return id == event.id && eventType == event.eventType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + id;
        hash = 31 * hash + (eventType == null ? 0 : eventType.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return "ElevatorEvent{" +
                "id=" + id +
                ", eventType=" + eventType +
                '}';
    }
}
