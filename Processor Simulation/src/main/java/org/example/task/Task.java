package org.example.task;

import org.example.enums.SharedResource;
import org.example.enums.TaskState;

import java.util.UUID;

public class Task implements Comparable<Task> {
    private final SharedResource resource;
    private UUID id;
    private int burstTime;
    private int creationTime;
    private int priority;
    private int taskNum;
    private PersonalControlBlock pcb;


    public Task(int burstTime, int creationTime, int priority, SharedResource resource) {
        this.id = UUID.randomUUID();
        this.burstTime = burstTime;
        this.creationTime = creationTime;
        this.priority = priority;
        this.resource = resource;
        this.pcb = new PersonalControlBlock(this.id, TaskState.NEW, this.burstTime);
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

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public SharedResource getResource() {
        return resource;
    }

    public void saveState(TaskState taskState) {
        this.pcb.setTaskState(taskState);
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
