package com.example.demo12;

public class Priority {

    public static void run(int[] arrival, int[] burst, int[] priority, int[] completion, int[] waiting, int[] turnaround) {
        int n = arrival.length;
        boolean[] finished = new boolean[n];
        int currentTime = 0, completed = 0;

        while (completed < n) {
            int highestPriority = -1;
            for (int i = 0; i < n; i++) {
                if (!finished[i] && arrival[i] <= currentTime && (highestPriority == -1 || priority[i] < priority[highestPriority])) {
                    highestPriority = i;
                }
            }

            if (highestPriority == -1) {
                currentTime++;  // If no process is available, increment time
            } else {
                completion[highestPriority] = currentTime + burst[highestPriority];
                turnaround[highestPriority] = completion[highestPriority] - arrival[highestPriority];
                waiting[highestPriority] = turnaround[highestPriority] - burst[highestPriority];
                currentTime = completion[highestPriority];
                finished[highestPriority] = true;
                completed++;
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
