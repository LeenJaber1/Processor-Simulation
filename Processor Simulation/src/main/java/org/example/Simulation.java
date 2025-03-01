package org.example;

import org.example.operatingsystem.OperatingSystem;

public class Simulation {
    private OperatingSystem operatingSystem;

    public Simulation(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public void startSimulation() {
        Thread os = new Thread(this.operatingSystem);
        os.start();
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }
}
