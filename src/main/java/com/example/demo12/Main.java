package com.example.demo12;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    private Scene mainScene;
    private Scene algorithmPage;
    private Scene comparePage;
    private Scene chartPage;
    private Scene barChartPage;

    private double avgWaitingFCFS;
    private double avgWaitingSJF;
    private double avgWaitingSJFPreemptive;
    private double avgWaitingPriority;
    private double avgWaitingRR;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CPU Scheduling Simulator");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel = new Label("CPU Scheduling Simulator");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        TextField arrivalField = new TextField();
        arrivalField.setPromptText("Arrival Time (comma-separated)");
        arrivalField.setMaxWidth(300);

        TextField burstField = new TextField();
        burstField.setPromptText("Burst Time (comma-separated)");
        burstField.setMaxWidth(300);

        TextField priorityField = new TextField();
        priorityField.setPromptText("Priority (comma-separated)");
        priorityField.setMaxWidth(300);

        TextField quantumField = new TextField();
        quantumField.setPromptText("Time Quantum (for Round Robin)");
        quantumField.setMaxWidth(300);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 16px;");

        vbox.getChildren().addAll(titleLabel, arrivalField, burstField, priorityField, quantumField, submitButton);

        mainScene = new Scene(vbox, 500, 400);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        submitButton.setOnAction(event -> {
            String arrivalTimes = arrivalField.getText();
            String burstTimes = burstField.getText();
            String priorityTimes = priorityField.getText();
            String timeQuantum = quantumField.getText();

            showAlgorithmPage(primaryStage, arrivalTimes, burstTimes, priorityTimes, timeQuantum);
        });
    }

    private void showAlgorithmPage(Stage primaryStage, String arrivalTimes, String burstTimes, String priorityTimes, String timeQuantum) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));

        Label selectAlgorithmLabel = new Label("Select Scheduling Algorithm");
        selectAlgorithmLabel.setFont(new Font("Arial", 20));
        selectAlgorithmLabel.setTextFill(Color.WHITE);

        Button fcfsButton = new Button("FCFS");
        Button sjfButton = new Button("SJF Non-Preemptive");
        Button sjfPreemptiveButton = new Button("SJF Preemptive");
        Button priorityButton = new Button("Priority");
        Button roundRobinButton = new Button("Round Robin");
        Button compareButton = new Button("Compare Algorithms");
        Button backButton = new Button("Back");

        String buttonStyle = "-fx-background-color: darkblue; -fx-text-fill: white; -fx-font-size: 14px;";
        fcfsButton.setStyle(buttonStyle);
        sjfButton.setStyle(buttonStyle);
        sjfPreemptiveButton.setStyle(buttonStyle);
        priorityButton.setStyle(buttonStyle);
        roundRobinButton.setStyle(buttonStyle);
        compareButton.setStyle(buttonStyle);
        backButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white; -fx-font-size: 14px;");

        vbox.getChildren().addAll(selectAlgorithmLabel, fcfsButton, sjfButton, sjfPreemptiveButton, priorityButton, roundRobinButton, compareButton, backButton);

        algorithmPage = new Scene(vbox, 500, 400);
        primaryStage.setScene(algorithmPage);

        fcfsButton.setOnAction(e -> showResultPage(primaryStage, arrivalTimes, burstTimes, priorityTimes, "FCFS", timeQuantum));
        sjfButton.setOnAction(e -> showResultPage(primaryStage, arrivalTimes, burstTimes, priorityTimes, "SJF", timeQuantum));
        sjfPreemptiveButton.setOnAction(e -> showResultPage(primaryStage, arrivalTimes, burstTimes, priorityTimes, "SJFPreemptive", timeQuantum));
        priorityButton.setOnAction(e -> showResultPage(primaryStage, arrivalTimes, burstTimes, priorityTimes, "Priority", timeQuantum));
        roundRobinButton.setOnAction(e -> showResultPage(primaryStage, arrivalTimes, burstTimes, priorityTimes, "RR", timeQuantum));
        compareButton.setOnAction(e -> compareAlgorithms(primaryStage, arrivalTimes, burstTimes, priorityTimes, timeQuantum));
        backButton.setOnAction(e -> primaryStage.setScene(mainScene));
    }

    private void showResultPage(Stage primaryStage, String arrivalTimes, String burstTimes, String priorityTimes, String algorithm, String timeQuantum) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 20, 20, 20));
        vbox.setSpacing(10);

        TableView<ProcessData> table = new TableView<>();

        TableColumn<ProcessData, Integer> processColumn = new TableColumn<>("Process");
        processColumn.setCellValueFactory(new PropertyValueFactory<>("processId"));

        TableColumn<ProcessData, Integer> arrivalColumn = new TableColumn<>("Arrival Time");
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));

        TableColumn<ProcessData, Integer> burstColumn = new TableColumn<>("Burst Time");
        burstColumn.setCellValueFactory(new PropertyValueFactory<>("burstTime"));

        TableColumn<ProcessData, Integer> completionColumn = new TableColumn<>("Completion Time");
        completionColumn.setCellValueFactory(new PropertyValueFactory<>("completionTime"));

        TableColumn<ProcessData, Integer> waitingColumn = new TableColumn<>("Waiting Time");
        waitingColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        TableColumn<ProcessData, Integer> turnaroundColumn = new TableColumn<>("Turnaround Time");
        turnaroundColumn.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));

        table.getColumns().addAll(processColumn, arrivalColumn, burstColumn, completionColumn, waitingColumn, turnaroundColumn);

        int[] arrival = stringToIntArray(arrivalTimes);
        int[] burst = stringToIntArray(burstTimes);
        int[] priority = stringToIntArray(priorityTimes);
        int[] completion = new int[arrival.length];
        int[] waiting = new int[arrival.length];
        int[] turnaround = new int[arrival.length];

        if (algorithm.equals("FCFS")) {
            FCFS.run(arrival, burst, completion, waiting, turnaround);
        } else if (algorithm.equals("SJF")) {
            SJF.run(arrival, burst, completion, waiting, turnaround);
        } else if (algorithm.equals("SJFPreemptive")) {
            SJFPreemptive.run(arrival, burst, completion, waiting, turnaround);
        } else if (algorithm.equals("Priority")) {
            Priority.run(arrival, burst, priority, completion, waiting, turnaround);
        } else if (algorithm.equals("RR")) {
            int quantum = Integer.parseInt(timeQuantum);
            RoundRobin.run(arrival, burst, completion, waiting, turnaround, quantum);
        }

        for (int i = 0; i < arrival.length; i++) {
            table.getItems().add(new ProcessData(i + 1, arrival[i], burst[i], completion[i], waiting[i], turnaround[i]));
        }

        Label avgLabel = new Label(
                "Average Waiting Time: " + calculateAverageWaiting(waiting, algorithm) +
                        "\nAverage Turnaround Time: " + calculateAverageTurnaround(turnaround, algorithm)
        );

        Canvas ganttChart = new Canvas(400, 100);
        GraphicsContext gc = ganttChart.getGraphicsContext2D();
        drawGanttChart(gc, arrival, burst, completion, algorithm);

        Button backButton = new Button("Back to Algorithms");
        backButton.setOnAction(e -> primaryStage.setScene(algorithmPage));

        vbox.getChildren().addAll(table, avgLabel, ganttChart, backButton);

        Scene resultScene = new Scene(vbox, 600, 500);
        primaryStage.setScene(resultScene);
    }

    private void compareAlgorithms(Stage primaryStage, String arrivalTimes, String burstTimes, String priorityTimes, String timeQuantum) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 20, 20, 20));
        vbox.setSpacing(10);

        Label compareLabel = new Label("Comparison of Algorithms");

        int[] arrival = stringToIntArray(arrivalTimes);
        int[] burst = stringToIntArray(burstTimes);
        int[] priority = stringToIntArray(priorityTimes);

        int[] completionFCFS = new int[arrival.length];
        int[] waitingFCFS = new int[arrival.length];
        int[] turnaroundFCFS = new int[arrival.length];
        FCFS.run(arrival, burst, completionFCFS, waitingFCFS, turnaroundFCFS);
        avgWaitingFCFS = calculateAverageWaiting(waitingFCFS, "FCFS");

        int[] completionSJF = new int[arrival.length];
        int[] waitingSJF = new int[arrival.length];
        int[] turnaroundSJF = new int[arrival.length];
        SJF.run(arrival, burst, completionSJF, waitingSJF, turnaroundSJF);
        avgWaitingSJF = calculateAverageWaiting(waitingSJF, "SJF");

        int[] completionSJFPreemptive = new int[arrival.length];
        int[] waitingSJFPreemptive = new int[arrival.length];
        int[] turnaroundSJFPreemptive = new int[arrival.length];
        SJFPreemptive.run(arrival, burst, completionSJFPreemptive, waitingSJFPreemptive, turnaroundSJFPreemptive);
        avgWaitingSJFPreemptive = calculateAverageWaiting(waitingSJFPreemptive, "SJFPreemptive");

        int[] completionPriority = new int[arrival.length];
        int[] waitingPriority = new int[arrival.length];
        int[] turnaroundPriority = new int[arrival.length];
        Priority.run(arrival, burst, priority, completionPriority, waitingPriority, turnaroundPriority);
        avgWaitingPriority = calculateAverageWaiting(waitingPriority, "Priority");

        int[] completionRR = new int[arrival.length];
        int[] waitingRR = new int[arrival.length];
        int[] turnaroundRR = new int[arrival.length];
        int quantum = Integer.parseInt(timeQuantum);
        RoundRobin.run(arrival, burst, completionRR, waitingRR, turnaroundRR, quantum);
        avgWaitingRR = calculateAverageWaiting(waitingRR, "RR");

        double minWaitingTime = Math.min(Math.min(Math.min(Math.min(avgWaitingFCFS, avgWaitingSJF), avgWaitingSJFPreemptive), avgWaitingPriority), avgWaitingRR);
        String efficientAlgorithm = "";
        if (minWaitingTime == avgWaitingFCFS) efficientAlgorithm = "FCFS";
        else if (minWaitingTime == avgWaitingSJF) efficientAlgorithm = "SJF Non-Preemptive";
        else if (minWaitingTime == avgWaitingSJFPreemptive) efficientAlgorithm = "SJF Preemptive";
        else if (minWaitingTime == avgWaitingPriority) efficientAlgorithm = "Priority";
        else efficientAlgorithm = "Round Robin";

        TableView<AlgorithmComparison> comparisonTable = new TableView<>();

        TableColumn<AlgorithmComparison, String> algorithmColumn = new TableColumn<>("Algorithm");
        algorithmColumn.setCellValueFactory(new PropertyValueFactory<>("algorithmName"));

        TableColumn<AlgorithmComparison, Double> waitingColumn = new TableColumn<>("Avg Waiting Time");
        waitingColumn.setCellValueFactory(new PropertyValueFactory<>("avgWaitingTime"));

        TableColumn<AlgorithmComparison, Double> turnaroundColumn = new TableColumn<>("Avg Turnaround Time");
        turnaroundColumn.setCellValueFactory(new PropertyValueFactory<>("avgTurnaroundTime"));

        comparisonTable.getColumns().addAll(algorithmColumn, waitingColumn, turnaroundColumn);

        comparisonTable.getItems().addAll(
                new AlgorithmComparison("FCFS", avgWaitingFCFS, calculateAverageTurnaround(turnaroundFCFS, "FCFS")),
                new AlgorithmComparison("SJF Non-Preemptive", avgWaitingSJF, calculateAverageTurnaround(turnaroundSJF, "SJF")),
                new AlgorithmComparison("SJF Preemptive", avgWaitingSJFPreemptive, calculateAverageTurnaround(turnaroundSJFPreemptive, "SJFPreemptive")),
                new AlgorithmComparison("Priority", avgWaitingPriority, calculateAverageTurnaround(turnaroundPriority, "Priority")),
                new AlgorithmComparison("Round Robin", avgWaitingRR, calculateAverageTurnaround(turnaroundRR, "RR"))
        );

        Label efficientLabel = new Label("Most Efficient Algorithm: " + efficientAlgorithm);
        efficientLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        efficientLabel.setTextFill(Color.GREEN);

        Button chartButton = new Button("Show Chart");
        chartButton.setOnAction(e -> showChartPage(primaryStage));

        Button backButton = new Button("Back to Algorithms");
        backButton.setOnAction(e -> primaryStage.setScene(algorithmPage));

        vbox.getChildren().addAll(compareLabel, comparisonTable, efficientLabel, chartButton, backButton);

        comparePage = new Scene(vbox, 600, 500);
        primaryStage.setScene(comparePage);
    }

    private void showChartPage(Stage primaryStage) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 20, 20, 20));
        vbox.setSpacing(10);

        Label efficiencyExplanation = new Label("Algorithm Efficiency (Smaller Waiting Time = Better Efficiency)");
        efficiencyExplanation.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        efficiencyExplanation.setTextFill(Color.DARKBLUE);

        double totalWaiting = avgWaitingFCFS + avgWaitingSJF + avgWaitingSJFPreemptive + avgWaitingPriority + avgWaitingRR;

        PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("FCFS (" + String.format("%.2f", (avgWaitingFCFS / totalWaiting) * 100) + "%)", avgWaitingFCFS));
        pieChart.getData().add(new PieChart.Data("SJF (" + String.format("%.2f", (avgWaitingSJF / totalWaiting) * 100) + "%)", avgWaitingSJF));
        pieChart.getData().add(new PieChart.Data("SJF Preemptive (" + String.format("%.2f", (avgWaitingSJFPreemptive / totalWaiting) * 100) + "%)", avgWaitingSJFPreemptive));
        pieChart.getData().add(new PieChart.Data("Priority (" + String.format("%.2f", (avgWaitingPriority / totalWaiting) * 100) + "%)", avgWaitingPriority));
        pieChart.getData().add(new PieChart.Data("Round Robin (" + String.format("%.2f", (avgWaitingRR / totalWaiting) * 100) + "%)", avgWaitingRR));

        Button showBarChartButton = new Button("Show Bar Chart");
        showBarChartButton.setOnAction(e -> showBarChartPage(primaryStage));

        Button backButton = new Button("Back to Compare Page");
        backButton.setOnAction(e -> primaryStage.setScene(comparePage));

        vbox.getChildren().addAll(efficiencyExplanation, pieChart, showBarChartButton, backButton);

        chartPage = new Scene(vbox, 800, 600);
        primaryStage.setScene(chartPage);
    }

    private void showBarChartPage(Stage primaryStage) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, 20, 20, 20));
        vbox.setSpacing(10);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Algorithm");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Average Waiting Time");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Average Waiting Time Comparison");

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.getData().add(new XYChart.Data<>("FCFS", avgWaitingFCFS));
        dataSeries.getData().add(new XYChart.Data<>("SJF", avgWaitingSJF));
        dataSeries.getData().add(new XYChart.Data<>("SJF Preemptive", avgWaitingSJFPreemptive));
        dataSeries.getData().add(new XYChart.Data<>("Priority", avgWaitingPriority));
        dataSeries.getData().add(new XYChart.Data<>("Round Robin", avgWaitingRR));

        barChart.getData().add(dataSeries);

        Button backButton = new Button("Back to Pie Chart");
        backButton.setStyle("-fx-background-color: darkred; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setOnAction(e -> primaryStage.setScene(chartPage));

        vbox.getChildren().addAll(barChart, backButton);

        barChartPage = new Scene(vbox, 800, 600);
        primaryStage.setScene(barChartPage);
    }

    private double calculateAverageWaiting(int[] waiting, String algorithm) {
        double totalWaiting = 0;
        for (int w : waiting) {
            totalWaiting += w;
        }
        return totalWaiting / waiting.length;
    }

    private double calculateAverageTurnaround(int[] turnaround, String algorithm) {
        double totalTurnaround = 0;
        for (int t : turnaround) {
            totalTurnaround += t;
        }
        return totalTurnaround / turnaround.length;
    }

    private void drawGanttChart(GraphicsContext gc, int[] arrival, int[] burst, int[] completion, String algorithm) {
        int xPosition = 10;
        int height = 30;
        int yPosition = 20;
        int widthFactor = 15;

        for (int i = 0; i < arrival.length; i++) {
            gc.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            int width = burst[i] * widthFactor;
            gc.fillRect(xPosition, yPosition, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(xPosition, yPosition, width, height);
            gc.strokeText("P" + (i + 1), xPosition + width / 2 - 10, yPosition + height / 2 + 5);
            xPosition += width + 10;
        }
    }

    private int[] stringToIntArray(String input) {
        String[] parts = input.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
