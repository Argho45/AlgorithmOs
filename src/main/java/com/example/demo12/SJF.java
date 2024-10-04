package com.example.demo12;

public class SJF {
    public static void run(int[] arrival, int[] burst, int[] completion, int[] waiting, int[] turnaround) {
        int n = arrival.length;
        int[] remainingBurst = burst.clone();
        boolean[] finished = new boolean[n];
        int currentTime = 0, completed = 0;

        while (completed < n) {
            int shortest = -1;
            for (int i = 0; i < n; i++) {
                if (!finished[i] && arrival[i] <= currentTime && (shortest == -1 || remainingBurst[i] < remainingBurst[shortest])) {
                    shortest = i;
                }
            }
            if (shortest == -1) {
                currentTime++;
            } else {
                currentTime += remainingBurst[shortest];
                completion[shortest] = currentTime;
                turnaround[shortest] = completion[shortest] - arrival[shortest];
                waiting[shortest] = turnaround[shortest] - burst[shortest];
                finished[shortest] = true;
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
