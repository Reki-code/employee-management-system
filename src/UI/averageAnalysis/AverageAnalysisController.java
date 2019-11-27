package UI.averageAnalysis;

import data.Employee;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

public class AverageAnalysisController implements Initializable {
    @FXML
    private Label age;
    @FXML
    private Label wage;
    @FXML
    private Label seniority;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::calculateAverage).start();
    }

    private void calculateAverage() {
        var employees = Employee.getEmployees();
        var age = employees.stream().mapToInt(Employee::getAge).average().getAsDouble();
        var wage = employees.stream().mapToInt(Employee::getWage).average().getAsDouble();
        var seniority = employees.stream().mapToInt(e -> Period.between(e.getEntryDate(), LocalDate.now()).getYears()).average().getAsDouble();
        Platform.runLater(() -> {
            this.age.setText(String.valueOf(age));
            this.wage.setText(String.valueOf(wage));
            this.seniority.setText(String.valueOf(seniority));
        });
    }

    public void refresh(ActionEvent event) {
        new Thread(this::calculateAverage).start();
    }
}
