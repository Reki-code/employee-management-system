package main.java.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.java.data.Employee;

import java.net.URL;
import java.text.DecimalFormat;
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
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");

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
            this.age.setText(decimalFormat.format(age));
            this.wage.setText(decimalFormat.format(wage));
            this.seniority.setText(decimalFormat.format(seniority));
        });
    }

    public void refresh(ActionEvent event) {
        new Thread(this::calculateAverage).start();
    }
}
