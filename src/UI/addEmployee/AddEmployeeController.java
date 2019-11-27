package UI.addEmployee;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import data.Employee;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddEmployeeController implements Initializable {
    private final ToggleGroup genderGroup = new ToggleGroup();
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

    private void clearForm() {
        name.clear();
        age.clear();
        phoneNumber.clear();
        residence.clear();
        wage.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupForm();
        setupValidator();
    }

    private void setupValidator() {
        var EmptyValidator = new RequiredFieldValidator("不能为空");
        setEmptyValidator(id, EmptyValidator);
        setEmptyValidator(name, EmptyValidator);
        setEmptyValidator(age, EmptyValidator);
        setEmptyValidator(phoneNumber, EmptyValidator);
        setEmptyValidator(residence, EmptyValidator);
        setEmptyValidator(wage, EmptyValidator);
        var digitValidator = new RegexValidator("只能包含数字");
        digitValidator.setRegexPattern("^[0-9]*$");
        id.getValidators().add(digitValidator);
        age.getValidators().add(digitValidator);
        wage.getValidators().add(digitValidator);
        var phoneNumberValidator = new RegexValidator("不是合法的电话");
        phoneNumberValidator.setRegexPattern("^[1][0-9]{10}$");
        phoneNumber.getValidators().add(phoneNumberValidator);
    }

    private void setEmptyValidator(JFXTextField textField, RequiredFieldValidator validator) {
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                textField.validate();
            }
        });
    }

    private void setupForm() {
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        education.getItems().addAll(List.of(Employee.getEducations()).stream().map(Label::new).collect(Collectors.toList()));
        entryDate.setValue(LocalDate.now());
        addButton.disableProperty().bind(
                id.textProperty().isEmpty()
                        .or(name.textProperty().isEmpty()
                                .or(male.selectedProperty().or(female.selectedProperty()).not()
                                        .or(age.textProperty().isEmpty()
                                                .or(phoneNumber.textProperty().isEmpty()
                                                        .or(residence.textProperty().isEmpty()
                                                                .or(wage.textProperty().isEmpty()
                                                                )))))));
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
        String education;
        if (this.education.getValue() == null) {
            education = "";
        } else {
            education = this.education.getValue().getText();
        }
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
