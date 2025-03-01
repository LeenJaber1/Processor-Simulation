package org.example;

import org.example.operatingsystem.OperatingSystem;
import org.example.operatingsystem.WindowsOperatingSystem;
import org.example.scheduler.PriorityScheduling;
import org.example.scheduler.Scheduler;
import org.example.scheduler.SchedulingAlgorithm;
import org.example.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {
    public static void main(String[] args) {
        String tasks = "C:\\Users\\user\\Desktop\\tasks.txt";
        SchedulingAlgorithm priorityAlgo = new PriorityScheduling();
        Scheduler scheduler = new Scheduler(priorityAlgo);
        BlockingQueue<Task> queue = new PriorityBlockingQueue<>();
        OperatingSystem windows = new WindowsOperatingSystem("Windows os", 2, scheduler, 10, tasks, queue);
        Simulation simulation = new Simulation(windows);
        simulation.startSimulation();
    }
}