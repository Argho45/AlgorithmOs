package com.example.demo12;

public class ProcessData {
    private int processId;
    private int arrivalTime;
    private int burstTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;

    public ProcessData(int processId, int arrivalTime, int burstTime, int completionTime, int waitingTime, int turnaroundTime) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.completionTime = completionTime;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
    }

    public int getProcessId() {
        return processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }
}
