package org.example.processor;

import org.example.enums.TaskState;
import org.example.scheduler.Scheduler;
import org.example.task.Task;
import org.example.utils.ResourceManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class Processor implements Runnable {
    private final ResourceManager resourceManager;
    private UUID id;
    private BlockingQueue<Task> tasks;
    private Task currentTask;
    private boolean running = true;
    private boolean free = true;
    private Scheduler scheduler;
    private Set<Task> blockedTasks = new HashSet<>();

    public Processor(BlockingQueue<Task> tasks, ResourceManager resourceManager, Scheduler scheduler) {
        this.id = UUID.randomUUID();
        this.tasks = tasks;
        this.resourceManager = resourceManager;
        this.scheduler = scheduler;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Task task = this.tasks.take();
                if (resourceManager.tryAcquire(task.getResource())) {
                    if (blockedTasks.contains(task)) {
                        blockedTasks.remove(task);
                    }
                    taskStarted(task);
                    while (task.getPcb().getRemainingTime() > 0) {
                        execute(task);
                        if (Thread.currentThread().isInterrupted()) {
                            taskInterrupted(task);
                            this.scheduler.getSchedulingAlgorithm().addTask(task, this.tasks);
                        }
                        if (task.getPcb().getRemainingTime() <= 0) {
                            taskFinished(task);
                        }
                    }
                    resourceManager.release(task.getResource());
                } else {
                    taskBlocked(task);
                    //check here
                    this.scheduler.getSchedulingAlgorithm().addTask(task, this.tasks);
                }
            } catch (InterruptedException e) {
                if (!running) {
                    shutDown();
                    return;
                }
            }
        }
    }

    private void taskStarted(Task task) {
        this.currentTask = task;
        task.saveState(TaskState.RUNNING);
        this.free = false;
        System.out.println("Task " + task.getTaskNum() + " is Running." + " Burst time left : " + task.getPcb().getRemainingTime());
    }

    private void taskFinished(Task task) {
        System.out.println("Task " + task.getTaskNum() + " has finished.");
        this.free = true;
        task.saveState(TaskState.TERMINATED);
    }

    private void execute(Task task) {
        try {
            int executionTime = task.getPcb().getRemainingTime();
            long startTime = System.currentTimeMillis();
            Thread.currentThread().sleep(executionTime * 1000);
            long endTime = System.currentTimeMillis();
            long actualSleepTime = endTime - startTime;
            task.getPcb().setRemainingTime(task.getPcb().getRemainingTime() - (int) actualSleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void taskBlocked(Task task) {
        if (!blockedTasks.contains(task)) {
            blockedTasks.add(task);
            System.out.println("Task " + task.getTaskNum() + " is waiting. Resources blocked");
            task.saveState(TaskState.BLOCKED);
            this.free = true;
        }
    }

    private void taskInterrupted(Task task) {
        System.out.println("Task " + task.getTaskNum() + " was interrupted. Saving state...");
        task.saveState(TaskState.READY);
        resourceManager.release(task.getResource());
        this.free = true;
    }

    private void shutDown() {
        System.out.println("Processor " + this.id.toString() + " is shutting down ..");
        Thread.currentThread().interrupt();
        this.running = false;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Processor processor = (Processor) o;
        return running == processor.running && free == processor.free && Objects.equals(id, processor.id) && Objects.equals(tasks, processor.tasks) && Objects.equals(resourceManager, processor.resourceManager) && Objects.equals(currentTask, processor.currentTask) && Objects.equals(scheduler, processor.scheduler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tasks, resourceManager, currentTask, running, free, scheduler);
    }
}
