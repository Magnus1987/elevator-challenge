package com.tingco.codechallenge.elevator.impl;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tingco.codechallenge.elevator.impl.ElevatorEvent.EventType.*;

@NoArgsConstructor
@Service
public class ElevatorControllerImpl implements ElevatorController {

    private List<Elevator> elevators = new ArrayList<>();
    private Queue<Integer> pendingRequests = new LinkedList<>();

    @Autowired
    private ScheduledExecutorService taskExecutor;

    @Autowired
    private EventBus eventBus;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Value("${com.tingco.elevator.minFloor}")
    private int minFloor;

    @Value("${com.tingco.elevator.maxFloor}")
    private int maxFloor;

    @Value("${com.tingco.elevator.elevatorSpeed}")
    private int elevatorSpeed;

    @Value("${com.tingco.elevator.pollSpeed}")
    private int pollSpeed;

    @PreDestroy
    public void tearDown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @PostConstruct
    public void setUpElevators() {
        // Setup each elevator to have a separate scheduler
        for (int i = 0; i < numberOfElevators; i++) {
            setupElevator(i);
        }
        // Use a single shared scheduler to handle elevator requests
        executorService.scheduleAtFixedRate(() -> pollElevatorRequest(), 1, pollSpeed, TimeUnit.SECONDS);
    }

    private void setupElevator(final int id) {
        ElevatorImpl basicElevator = new ElevatorImpl(id, minFloor, maxFloor, eventBus);
        elevators.add(basicElevator);
        taskExecutor.scheduleAtFixedRate(basicElevator, 1, elevatorSpeed, TimeUnit.SECONDS);
    }

    private void pollElevatorRequest() {
        if (!pendingRequests.isEmpty()) {
            Integer requestedFloor = pendingRequests.poll();
            requestElevator(requestedFloor);
        }
    }

    @Override
    public synchronized Elevator requestElevator(final int toFloor) {
        Optional<Elevator> availableElevator = elevators.stream()
                .filter(elevator -> !elevator.isBusy())
                .sorted(Comparator.comparingInt(e -> Math.abs(e.currentFloor() - toFloor)))
                .findFirst();

        if (availableElevator.isPresent()) {
            Elevator elevator = availableElevator.get();
            elevator.moveElevator(toFloor);
            eventBus.post(new ElevatorEventBuilder()
                    .setEventType(ASSIGNED)
                    .setId(elevator.getId())
                    .build());
            return elevator;
        } else {
            eventBus.post(new ElevatorEventBuilder()
                    .setEventType(PENDING_REQUEST)
                    .build());
            pendingRequests.add(toFloor);
            return null;
        }
    }

    @Override
    public List<Elevator> getElevators() {
        return elevators;
    }

    @Override
    public synchronized void releaseElevator(final Elevator elevator) {
        if (elevators.contains(elevator)) {
            elevator.reset();
            eventBus.post(new ElevatorEventBuilder()
                    .setEventType(RESETED)
                    .setId(elevator.getId())
                    .build());
        }
    }

    public Queue<Integer> getPendingRequests() {
        return pendingRequests;
    }

    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }
}
