package org.example.scheduler;

import java.util.UUID;

public class Scheduler {
    private UUID id;
    private SchedulingAlgorithm schedulingAlgorithm;

    public Scheduler(SchedulingAlgorithm schedulingAlgorithm) {
        this.id = UUID.randomUUID();
        this.schedulingAlgorithm = schedulingAlgorithm;
    }

    public UUID getId() {
        return id;
    }


    public SchedulingAlgorithm getSchedulingAlgorithm() {
        return schedulingAlgorithm;
    }

    public void setSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm) {
        this.schedulingAlgorithm = schedulingAlgorithm;
    }
}
