package org.example.scheduler;

import org.example.task.Task;

import java.util.concurrent.BlockingQueue;

public class PriorityScheduling implements SchedulingAlgorithm {

    @Override
    public void addTask(Task task, BlockingQueue<Task> tasks) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isBetterTask(Task task1, Task task2) {
        return task1.getPriority() > task2.getPriority();
    }
}
