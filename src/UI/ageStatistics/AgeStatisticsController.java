package UI.ageStatistics;

import data.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AgeStatisticsController implements Initializable {
    @FXML
    private BarChart<String, Long> barChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::setupBarChart).start();
    }

    private void setupBarChart() {
        XYChart.Series<String, Long> series = new XYChart.Series<>();
        Map<String, Long> generation = Employee.getEmployees().stream()
                .collect(Collectors.groupingBy(this::getGeneration, Collectors.counting()));
        series.getData().addAll(generation.keySet().stream().map(k -> new XYChart.Data<>(k, generation.get(k))).collect(Collectors.toList()));
        barChart.getData().add(series);
    }

    private String getGeneration(Employee employee) {
        var age = employee.getAge();
        if (0 < age && age <= 20) {
            return "0-20";
        } else if (20 < age && age <= 40) {
            return "20-40";
        } else if (40 < age && age <= 60) {
            return "40-60";
        } else if (60 < age && age <= 80) {
            return "60-80";
        } else if (80 < age && age <= 100) {
            return "80-100";
        }
        return "未知";
    }

}
