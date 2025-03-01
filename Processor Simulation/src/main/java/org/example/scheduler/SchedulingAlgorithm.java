package org.example.scheduler;

import org.example.task.Task;

import java.util.concurrent.BlockingQueue;

public interface SchedulingAlgorithm {
    Task pickNextTask();
    void addTask(Task task, BlockingQueue<Task> tasks);
}
