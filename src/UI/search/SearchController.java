package UI.search;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import data.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchController implements Initializable {
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label gender;
    @FXML
    private Label age;
    @FXML
    private Label phone;
    @FXML
    private Label wage;
    @FXML
    private Label residence;
    @FXML
    private Label educationLabel;
    @FXML
    private Label entryDate;
    @FXML
    private JFXTextField wageUpper;
    @FXML
    private JFXTextField wageLower;
    @FXML
    private JFXListView<Card> listView;
    @FXML
    private StackPane rootPane;
    @FXML
    private Label editableTreeTableViewCount;
    @FXML
    private JFXComboBox<Label> education;
    private List<Employee> employeeData;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        education.getItems().addAll(List.of(Employee.getEducations()).stream().map(Label::new).collect(Collectors.toList()));
        education.getItems().add(new Label("不限"));
        fetchData();
    }

    private void fetchData() {
        employeeData = Employee.getEmployees();
    }

    @FXML
    public void refresh(ActionEvent event) {
        fetchData();
    }

    @FXML
    public void search(ActionEvent event) {
        listView.getItems().clear();
        listView.getItems().addAll(employeeData.stream().filter(this::filterEmployee).map(Card::new).collect(Collectors.toList()));
    }

    private boolean filterEmployee(Employee employee) {
        var employeeWage = employee.getWage();
        var wageCondition = getWageLower() <= employeeWage && employeeWage <= getWageUpper();
        if ("不限".equals(getEducation()) || getEducation().isEmpty()) {
            return wageCondition;
        } else {
            return wageCondition && getEducation().equals(employee.getEducation());
        }

    }

    private String getEducation() {
        return education.getValue().getText();
    }

    private int getWageLower() {
        final String wage = wageLower.getText();
        if (wage.isEmpty()) {
            return -1;
        } else {
            return Integer.parseInt(wage);
        }
    }

    private int getWageUpper() {
        final String wage = wageUpper.getText();
        if (wage.isEmpty()) {
            return Integer.MAX_VALUE;
        } else {
            return Integer.parseInt(wage);
        }
    }

    @FXML
    private void showDetail(MouseEvent event) {
        final Employee employee = listView.getSelectionModel().getSelectedItem().getEmployee();
        id.setText(String.valueOf(employee.getId()));
        name.setText(employee.getName());
        gender.setText(employee.getGender());
        age.setText(String.valueOf(employee.getAge()));
        wage.setText(String.valueOf(employee.getWage()));
        phone.setText(employee.getPhoneNumber());
        residence.setText(employee.getResidence());
        educationLabel.setText(employee.getEducation());
        entryDate.setText(employee.getEntryDate().toString());
    }

    class Card {
        Employee employee;

        public Card(Employee employee) {
            this.employee = employee;
        }

        public Employee getEmployee() {
            return employee;
        }

        @Override
        public String toString() {
            return String.format("职工编号:%d, 姓名:%s, 工资:%d, 学历:%s", employee.getId(), employee.getName(), employee.getWage(), employee.getEducation());
        }
    }

}
