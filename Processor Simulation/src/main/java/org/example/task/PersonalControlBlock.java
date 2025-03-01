package org.example.task;

import org.example.enums.TaskState;

import java.util.UUID;

public class PersonalControlBlock {
    private UUID id;
    private TaskState taskState;
    private int remainingTime;

    public PersonalControlBlock(UUID id, TaskState taskState, int remainingTime) {
        this.id = id;
        this.taskState = taskState;
        this.remainingTime = remainingTime;
    }

    public UUID getId() {
        return id;
    }


    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

}
