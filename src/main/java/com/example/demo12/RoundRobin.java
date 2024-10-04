package com.example.demo12;

public class RoundRobin {
    public static void run(int[] arrival, int[] burst, int[] completion, int[] waiting, int[] turnaround, int quantum) {
        int n = arrival.length;
        int[] remainingBurst = burst.clone();
        int[] completionTime = new int[n];
        boolean[] finished = new boolean[n];
        int currentTime = 0, completed = 0;
        while (completed < n) {
            boolean allIdle = true;
            for (int i = 0; i < n; i++) {
                if (!finished[i] && arrival[i] <= currentTime) {
                    if (remainingBurst[i] > quantum) {
                        currentTime += quantum;
                        remainingBurst[i] -= quantum;
                    } else {
                        currentTime += remainingBurst[i];
                        remainingBurst[i] = 0;
                        completion[i] = currentTime;
                        turnaround[i] = completion[i] - arrival[i];
                        waiting[i] = turnaround[i] - burst[i];
                        finished[i] = true;
                        completed++;
                    }
                    allIdle = false;
                }
            }
            if (allIdle) {
                currentTime++;
            }
        }
    }

    public static double calculateAverageWaiting(int[] waiting) {
        double totalWaiting = 0;
        for (int w : waiting) {
            totalWaiting += w;
        }
        return totalWaiting / waiting.length;
    }

    public static double calculateAverageTurnaround(int[] turnaround) {
        double totalTurnaround = 0;
        for (int t : turnaround) {
            totalTurnaround += t;
        }
        return totalTurnaround / turnaround.length;
    }
}
