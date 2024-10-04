package com.example.demo12;

public class FCFS {

    public static void run(int[] arrival, int[] burst, int[] completion, int[] waiting, int[] turnaround) {
        completion[0] = arrival[0] + burst[0];
        turnaround[0] = completion[0] - arrival[0];
        waiting[0] = turnaround[0] - burst[0];

        for (int i = 1; i < arrival.length; i++) {

            if (arrival[i] > completion[i - 1]) {
                completion[i] = arrival[i] + burst[i];
            } else {

                completion[i] = completion[i - 1] + burst[i];
            }
            // Calculate turnaround and waiting times
            turnaround[i] = completion[i] - arrival[i];  // Turnaround time
            waiting[i] = turnaround[i] - burst[i];  // Waiting time
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
