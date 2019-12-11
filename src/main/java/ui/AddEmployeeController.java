package main.java.ui;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import main.java.data.Employee;
import main.java.utils.dialog.PromptDialog;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddEmployeeController implements Initializable {
    private final ToggleGroup genderGroup = new ToggleGroup();
    @FXML
    private StackPane rootPane;
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

    /**
     * 设置验证器
     */
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

    /**
     * 设置表单
     */
    private void setupForm() {
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        education.getItems().addAll(List.of(Employee.getEducations()).stream().map(Label::new).collect(Collectors.toList()));
        entryDate.setValue(LocalDate.now());
        entryDate.setConverter(new StringConverter<>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        });
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

    /**
     * 处理录入按钮点击事件
     */
    @FXML
    private void submit() {
        loading.setVisible(true);
        new Thread(() -> {
            String tip = addEmployee();
            Platform.runLater(() -> {
                loading.setVisible(false);
                new PromptDialog("录入信息", tip).show(rootPane);
            });
        }
        ).start();
    }

    /**
     * 保存职工信息
     * 把表单当中的信息保存到数据库中
     *
     * @return true 保存成功, false 保存失败
     */
    private String addEmployee() {
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
        } catch (SQLIntegrityConstraintViolationException e) {
            return "职工编号重复";
        } catch (SQLException e) {
            e.printStackTrace();
            return "录入失败";
        }
        return "录入成功";
    }
}
