package main.java.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import main.java.data.Employee;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EducationStatisticsController implements Initializable {
    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::setupPieChart).start();
    }

    private void setupPieChart() {
        var employees = Employee.getEmployees();
        Map<String, Long> generation = employees.stream()
                .collect(Collectors.groupingBy(Employee::getEducation, Collectors.counting()));
        var pieDate = generation.keySet().stream().map(k -> new PieChart.Data(k + ":" + generation.get(k), generation.get(k))).collect(Collectors.toList());
        Platform.runLater(() -> pieChart.getData().addAll(pieDate));
    }

}
