package UI.addEmployee;

import com.jfoenix.controls.*;
import data.Employee;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddEmployeeController implements Initializable {
    final ToggleGroup genderGroup = new ToggleGroup();
    @FXML
    private JFXSpinner loading;
    @FXML
    private JFXComboBox<Label> education;
    @FXML
    private JFXRadioButton male;
    @FXML
    private JFXRadioButton female;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField age;
    @FXML
    private JFXTextField phoneNumber;
    @FXML
    private JFXTextField residence;
    @FXML
    private JFXTextField wage;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXDatePicker entryDate;
    private SimpleBooleanProperty buttonDisableProperty = new SimpleBooleanProperty(true);

    private void clearForm() {
        name.clear();
        age.clear();
        phoneNumber.clear();
        residence.clear();
        wage.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        education.getItems().addAll(List.of(Employee.getEducations()).stream().map(Label::new).collect(Collectors.toList()));

        addButton.disableProperty().bind(buttonDisableProperty);
        id.textProperty().addListener(((observable, oldValue, newValue) -> checkButtonDisable()));
        name.textProperty().addListener(((observable, oldValue, newValue) -> checkButtonDisable()));
        age.textProperty().addListener(((observable, oldValue, newValue) -> checkButtonDisable()));
        phoneNumber.textProperty().addListener(((observable, oldValue, newValue) -> checkButtonDisable()));
        residence.textProperty().addListener(((observable, oldValue, newValue) -> checkButtonDisable()));
    }

    private void checkButtonDisable() {
        if (id.getText().isEmpty() || name.getText().isEmpty() || age.getText().isEmpty() || phoneNumber.getText().isEmpty() || residence.getText().isEmpty()) {
            buttonDisableProperty.set(true);
        } else {
            buttonDisableProperty.set(false);
        }
    }

    @FXML
    private void submit() {
        loading.setVisible(true);
        new Thread(() -> {
            if (addEmployee()) {
                clearForm();
            } else {
                System.out.println("录入失败");
            }
            Platform.runLater(() -> loading.setVisible(false));
        }
        ).start();
    }

    private boolean addEmployee() {
        var genderSelected = (JFXRadioButton) genderGroup.getSelectedToggle();
        var gender = genderSelected.getText();
        var education = this.education.getValue().getText();
        var employee = new Employee(Integer.parseInt(id.getText()), name.getText(), gender, Integer.parseInt(age.getText()), phoneNumber.getText(), residence.getText(), education, Integer.parseInt(wage.getText()), entryDate.getValue());
        try {
            employee.insert();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
