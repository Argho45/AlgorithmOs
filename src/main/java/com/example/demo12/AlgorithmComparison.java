package com.example.demo12;

public class AlgorithmComparison {
    private String algorithmName;
    private double avgWaitingTime;
    private double avgTurnaroundTime;

    public AlgorithmComparison(String algorithmName, double avgWaitingTime, double avgTurnaroundTime) {
        this.algorithmName = algorithmName;
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public double getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public void setAvgWaitingTime(double avgWaitingTime) {
        this.avgWaitingTime = avgWaitingTime;
    }

    public double getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }

    public void setAvgTurnaroundTime(double avgTurnaroundTime) {
        this.avgTurnaroundTime = avgTurnaroundTime;
    }
}
