package UI.search;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import data.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchController implements Initializable {
    @FXML
    private JFXTreeTableView TreeTableView;
    @FXML
    private JFXTreeTableColumn idColumn;
    @FXML
    private JFXTreeTableColumn nameColumn;
    @FXML
    private JFXTreeTableColumn wageColumn;
    @FXML
    private JFXTreeTableColumn educationColumn;
    @FXML
    private JFXTextField wageLower;
    @FXML
    private JFXTextField wageUpper;
    @FXML
    private JFXComboBox<Label> education;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        education.getItems().addAll(List.of(Employee.getEducations()).stream().map(Label::new).collect(Collectors.toList()));
        education.getItems().add(new Label("不限"));
        setupTreeTable();
    }

    private void setupTreeTable() {

    }

    @FXML
    public void searchEmployee(ActionEvent event) {
        System.out.println(wageLower.getText());
        System.out.println(wageUpper.getText());
        var education = this.education.getValue().getText();
        System.out.println(education);
    }

}
