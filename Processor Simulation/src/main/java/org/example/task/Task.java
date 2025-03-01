package org.example.task;

import org.example.enums.SharedResource;
import org.example.enums.TaskState;

import java.util.UUID;

public class Task {
    private UUID id;
    private int burstTime;
    private int creationTime;
    private int priority;
    private String taskName;
    private PersonalControlBlock pcb;
    private final SharedResource resource;


    public Task(int burstTime, int creationTime, int priority, SharedResource resource) {
        this.id = UUID.randomUUID();
        this.burstTime = burstTime;
        this.creationTime = creationTime;
        this.priority = priority;
        this.resource = resource;
        this.pcb = new PersonalControlBlock(this.id, TaskState.NEW , this.burstTime);
    }

    public PersonalControlBlock getPcb() {
        return pcb;
    }

    public void setPcb(PersonalControlBlock pcb) {
        this.pcb = pcb;
    }

    public UUID getId() {
        return id;
    }


    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(int creationTime) {
        this.creationTime = creationTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public SharedResource getResource() {
        return resource;
    }

    public void saveState(TaskState taskState){
        this.pcb.setTaskState(taskState);
    }
}
