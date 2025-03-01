package org.example.operatingsystem;

import org.example.enums.SharedResource;
import org.example.processor.Processor;
import org.example.scheduler.Scheduler;
import org.example.task.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class WindowsOperatingSystem extends OperatingSystem {
    private File tasksFile;

    public WindowsOperatingSystem(String systemName, int numberOfProcessors, Scheduler scheduler, int clockCycles, String filePath, BlockingQueue<Task> readyQueue) {
        super(systemName, numberOfProcessors, scheduler, clockCycles, readyQueue);
        this.tasksFile = new File(filePath);
    }

    @Override
    public void saveTasks() {
        Scanner myReader = null;
        int taskNum = 1;
        try {
            myReader = new Scanner(this.tasksFile);
            while (myReader.hasNextLine()) {
                String taskInfo = myReader.nextLine();
                int[] info = getInfo(taskInfo);
                int burstTime = info[1];
                int cycle = info[0];
                int priority = info[2];
                SharedResource resource = getResource(taskInfo);
                Task task = new Task(burstTime, cycle, priority, resource);
                task.setTaskNum(taskNum);
                List<Task> tasks = this.getAllTasks().computeIfAbsent(cycle, k -> new ArrayList<>());
                tasks.add(task);
                taskNum++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File cant be read");
        }
    }

    private int[] getInfo(String taskInfo) {
        int[] info = new int[3];
        int blackCount = 0;
        String temp = "";
        for (int i = 0; i < taskInfo.length(); ) {
            if (taskInfo.charAt(i) != ' ' && Character.isDigit(taskInfo.charAt(i))) {
                temp += taskInfo.charAt(i);
            }
            if (taskInfo.charAt(i) == ' ') {
                info[blackCount] = Integer.valueOf(temp);
                blackCount++;
                temp = "";
            }
            i++;
            if (blackCount == 2) {
                break;
            }
        }
        return info;
    }

    private SharedResource getResource(String taskInfo) {
        String resource = "";
        for (int i = 0; i < taskInfo.length(); i++) {
            if (Character.isLetter(taskInfo.charAt(i))) {
                resource += taskInfo.charAt(i);
            }
        }
        SharedResource sharedResource = null;
        for (SharedResource resource1 : SharedResource.values()) {
            if (resource.toUpperCase().equals(resource1.name())) {
                sharedResource = resource1;
                break;
            }
        }
        return sharedResource;
    }

    @Override
    public void acceptTask(int clockCycle) {
        if (this.getAllTasks().containsKey(clockCycle)) {
            List<Task> tasks = this.getAllTasks().get(clockCycle);
            for (Task task : tasks) {
                this.getScheduler().getSchedulingAlgorithm().addTask(task, this.getReadyQueue());
                performContextSwitching(task);
            }
        }
    }

    @Override
    public void performContextSwitching(Task task) {
        for (Map.Entry<Processor, Thread> pair : this.getProcessors().entrySet()) {
            if (pair.getKey().isFree()) {
                return;
            }
        }
        for (Map.Entry<Processor, Thread> pair : this.getProcessors().entrySet()) {
            Task currentRunningTask = pair.getKey().getCurrentTask();
            if (this.getScheduler().getSchedulingAlgorithm().isBetterTask(task, currentRunningTask)) {
                pair.getValue().interrupt();
                break;
            }
        }
    }


}
