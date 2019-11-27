package UI.educationStatistics;

import data.Employee;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EducationStatisticsController implements Initializable {
    @FXML
    private BarChart<String, Long> barChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::setupBarChart).start();
    }

    private void setupBarChart() {
        XYChart.Series<String, Long> series = new XYChart.Series<>();
        Map<String, Long> generation = Employee.getEmployees().stream()
                .collect(Collectors.groupingBy(Employee::getEducation, Collectors.counting()));
        series.getData().addAll(generation.keySet().stream().map(k -> new XYChart.Data<>(k, generation.get(k))).collect(Collectors.toList()));
        Platform.runLater(() -> barChart.getData().add(series));
    }

}
