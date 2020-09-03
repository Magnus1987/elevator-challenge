package com.tingco.codechallenge.elevator.model;

/**
 * Builds an {@link ElevatorEvent} following the builder pattern.
 */
public class ElevatorEventBuilder {
    private int id;
    private ElevatorEvent.EventType eventType;

    public ElevatorEventBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public ElevatorEventBuilder setEventType(final ElevatorEvent.EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public ElevatorEvent build() {
        return new ElevatorEvent(id, eventType);
    }
}
