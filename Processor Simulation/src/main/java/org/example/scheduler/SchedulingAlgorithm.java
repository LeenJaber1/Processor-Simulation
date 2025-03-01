package org.example.scheduler;

import org.example.task.Task;

import java.util.concurrent.BlockingQueue;

public interface SchedulingAlgorithm {
    void addTask(Task task, BlockingQueue<Task> tasks);

    boolean isBetterTask(Task task1, Task task2);
}
