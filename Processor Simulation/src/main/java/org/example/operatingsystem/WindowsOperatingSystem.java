package org.example.operatingsystem;

import org.example.scheduler.Scheduler;

public class WindowsOperatingSystem extends OperatingSystem{
    public WindowsOperatingSystem(String systemName, int numberOfProcessors, Scheduler scheduler, int clockCycles) {
        super(systemName, numberOfProcessors, scheduler, clockCycles);
    }


    @Override
    public void acceptTasks(int clockCycle) {

    }

    @Override
    public boolean performContextSwitching() {
        return false;
    }
}
