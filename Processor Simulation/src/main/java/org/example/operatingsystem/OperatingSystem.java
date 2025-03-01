package org.example.operatingsystem;

import org.example.processor.Processor;
import org.example.utils.ResourceManager;
import org.example.scheduler.Scheduler;
import org.example.task.Task;

import java.util.concurrent.*;

public abstract class OperatingSystem implements Runnable{
    private String systemName;
    private BlockingQueue<Task> readyQueue = new LinkedBlockingQueue<>();
    private int numberOfProcessors;
    private ExecutorService processors;
    private Scheduler scheduler;
    private ResourceManager resourceManager = new ResourceManager();
    private int clockCycles;
    private volatile boolean isRunning = true;


    public OperatingSystem(String systemName, int numberOfProcessors, Scheduler scheduler, int clockCycles) {
        this.systemName = systemName;
        this.numberOfProcessors = numberOfProcessors;
        this.scheduler = scheduler;
        this.processors = Executors.newFixedThreadPool(this.numberOfProcessors);
        this.clockCycles = clockCycles;
    }

    @Override
    public void run() {
        turnOnProcessors();

        for (int i = 0; i < this.clockCycles && this.isRunning; i++) {
            System.out.println("Clock Cycle " + (i + 1) + "/" + this.clockCycles);

            // Accept new tasks during the cycle
            acceptTasks(i);
            try {
                Thread.sleep(1000); // Simulating one second per cycle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("System interrupted before completing all cycles.");
                break;
            }
        }

        turnOffSystem();
    }

    public void turnOffSystem() {
        System.out.println("Shutting down system...");
        this.isRunning = false;

        this.processors.shutdown();
        try {
            if (!this.processors.awaitTermination(5, TimeUnit.SECONDS)) {
                this.processors.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.processors.shutdownNow();
        }

        this.readyQueue.clear(); // Clear remaining tasks
        System.out.println("System shut down.");
    }

    public abstract void acceptTasks(int clockCycle);

    public void turnOnProcessors(){
        for (int i = 0; i < this.numberOfProcessors; i++) {
            this.processors.submit(new Processor(readyQueue, resourceManager));
        }
    }

    public abstract boolean performContextSwitching();

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

    public ExecutorService getProcessors() {
        return this.processors;
    }

    public void setProcessors(ExecutorService processors) {
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
}
