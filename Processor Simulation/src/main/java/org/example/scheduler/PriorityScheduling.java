package org.example.scheduler;

import org.example.task.Task;

import java.util.concurrent.BlockingQueue;

public class PriorityScheduling implements SchedulingAlgorithm{
    @Override
    public Task pickNextTask() {
        return null;
    }

    @Override
    public void addTask(Task task, BlockingQueue<Task> tasks) {

    }
}
