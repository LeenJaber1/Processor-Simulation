package org.example.processor;

import org.example.utils.ResourceManager;
import org.example.enums.TaskState;
import org.example.task.Task;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class Processor implements Runnable{
    private UUID id;
    private BlockingQueue<Task> tasks;
    private final ResourceManager resourceManager;


    public Processor(BlockingQueue<Task> tasks, ResourceManager resourceManager) {
        this.id = UUID.randomUUID();
        this.tasks = tasks;
        this.resourceManager = resourceManager;
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
        while(true){
            try{
                Task task = this.tasks.take();
                if(resourceManager.tryAcquire(task.getResource())){
                    task.saveState(TaskState.RUNNING);
                    System.out.println("Task " + task.getTaskName() + " is Running." + "Burst time left : " + task.getPcb().getRemainingTime());
                    while (task.getPcb().getRemainingTime() > 0) {
                        int executionTime = task.getPcb().getRemainingTime();

                        long startTime = System.currentTimeMillis();
                        Thread.sleep(executionTime);
                        long endTime = System.currentTimeMillis();

                        long actualSleepTime = endTime - startTime;
                        task.getPcb().setRemainingTime(task.getPcb().getRemainingTime() - (int) actualSleepTime);

                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Task " + task.getTaskName() + " was interrupted. Saving state...");
                            task.saveState(TaskState.READY);
                            resourceManager.release(task.getResource());
                            //check here
                            tasks.put(task);
                            return;
                        }
                        if(task.getPcb().getRemainingTime() == 0){
                            System.out.println("Task " + task.getTaskName() + " has finished.");
                            task.saveState(TaskState.TERMINATED);
                        }
                    }
                    resourceManager.release(task.getResource());
                }
                else{
                    System.out.println("Task " + task.getTaskName() + " is waiting. Resources blocked");
                    task.saveState(TaskState.BLOCKED);
                    //check here
                    tasks.put(task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Ensure proper exit
                return;
            }
        }
    }

}
