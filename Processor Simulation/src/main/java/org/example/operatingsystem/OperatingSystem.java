package org.example.operatingsystem;

import org.example.processor.Processor;
import org.example.scheduler.Scheduler;
import org.example.task.Task;
import org.example.utils.ResourceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public abstract class OperatingSystem implements Runnable {
    private String systemName;
    private BlockingQueue<Task> readyQueue;
    private Map<Integer, List<Task>> allTasks = new HashMap<>();
    private int numberOfProcessors;
    private Map<Processor, Thread> processors = new HashMap<>();
    private Scheduler scheduler;
    private ResourceManager resourceManager = new ResourceManager();
    private int clockCycles;
    private volatile boolean isRunning = true;


    public OperatingSystem(String systemName, int numberOfProcessors, Scheduler scheduler, int clockCycles, BlockingQueue<Task> readyQueue) {
        this.systemName = systemName;
        this.numberOfProcessors = numberOfProcessors;
        this.scheduler = scheduler;
        this.clockCycles = clockCycles;
        this.readyQueue = readyQueue;
    }

    @Override
    public void run() {
        turnOnProcessors();
        saveTasks();
        for (int i = 1; i <= this.clockCycles && this.isRunning; i++) {
            System.out.println("Clock Cycle " + (i) + "/" + this.clockCycles);
            acceptTask(i);
            try {
                Thread.sleep(3000); // Simulating one second per cycle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("System interrupted before completing all cycles.");
                break;
            }
        }
        turnOffSystem();
        Thread.currentThread().interrupt();
    }

    public void turnOffSystem() {
        System.out.println("Shutting down system...");
        this.isRunning = false;
        for (Map.Entry<Processor, Thread> pair : this.processors.entrySet()) {
            pair.getKey().setRunning(false);
            pair.getValue().interrupt();
        }
        this.readyQueue.clear(); // Clear remaining tasks
        System.out.println("System shut down.");
    }

    public abstract void acceptTask(int clockCycle);

    public abstract void saveTasks();

    public void turnOnProcessors() {
        for (int i = 0; i < this.numberOfProcessors; i++) {
            Processor processor = new Processor(readyQueue, resourceManager, this.scheduler);
            Thread thread = new Thread(processor);
            thread.start();
            this.processors.put(processor, thread);
        }
    }

    public abstract void performContextSwitching(Task task);

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public BlockingQueue<Task> getReadyQueue() {
        return this.readyQueue;
    }

    public void setReadyQueue(BlockingQueue<Task> readyQueue) {
        this.readyQueue = readyQueue;
    }

    public int getNumberOfProcessors() {
        return this.numberOfProcessors;
    }

    public void setNumberOfProcessors(int numberOfProcessors) {
        this.numberOfProcessors = numberOfProcessors;
    }


    public Map<Processor, Thread> getProcessors() {
        return processors;
    }

    public void setProcessors(Map<Processor, Thread> processors) {
        this.processors = processors;
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public int getClockCycles() {
        return this.clockCycles;
    }

    public void setClockCycles(int clockCycles) {
        this.clockCycles = clockCycles;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public Map<Integer, List<Task>> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(Map<Integer, List<Task>> allTasks) {
        this.allTasks = allTasks;
    }
}
