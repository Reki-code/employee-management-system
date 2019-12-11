package main.java.ui;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import main.java.data.Employee;
import main.java.utils.EmployeeProperty;
import main.java.utils.dialog.ConfirmDialog;
import main.java.utils.dialog.PromptDialog;
import main.java.utils.editingcell.ComboBoxEditingCell;
import main.java.utils.editingcell.DatePickerEditingCell;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Function;

public class EmployeeListController implements Initializable {
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, Integer> idColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, String> nameColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, String> genderColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, Integer> ageColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, String> phoneNumberColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, String> residenceColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, String> educationColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, Integer> wageColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, LocalDate> entryDateColumn;
    @FXML
    private JFXTreeTableView<EmployeeProperty> editableTreeTableView;
    @FXML
    private Label editableTreeTableViewCount;
    @FXML
    private JFXTextField searchField;
    private ObservableList<EmployeeProperty> employeeData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::setupEmployeeTableView).start();
    }

    /**
     * 处理删除按钮点击信息
     */
    @FXML
    private void delEmployee() {
        if (!editableTreeTableView.getSelectionModel().isEmpty()) {
            new ConfirmDialog("删除", "确认要删除该职工的信息吗?")
                    .setConfirmAction(() -> delEmployee(getSelectedValue().toEmployee()))
                    .show(rootPane);
        }
    }

    /**
     * 删除指定的职工信息
     *
     * @param employee 要删除的职工
     */
    private void delEmployee(Employee employee) {
        try {
            if (employee.delete()) { //删除成功
                employeeData.removeIf(e -> e.getId() == employee.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("删除失败");
        }
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<EmployeeProperty, T> column, Function<EmployeeProperty, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<EmployeeProperty, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    /**
     * 设置职工信息表
     */
    private void setupEmployeeTableView() {
        setupCellValueFactory(idColumn, p -> p.idProperty().asObject());
        setupCellValueFactory(nameColumn, EmployeeProperty::nameProperty);
        setupCellValueFactory(genderColumn, EmployeeProperty::genderProperty);
        setupCellValueFactory(ageColumn, p -> p.ageProperty().asObject());
        setupCellValueFactory(phoneNumberColumn, EmployeeProperty::phoneNumberProperty);
        setupCellValueFactory(residenceColumn, EmployeeProperty::residenceProperty);
        setupCellValueFactory(educationColumn, EmployeeProperty::educationProperty);
        setupCellValueFactory(wageColumn, p -> p.wageProperty().asObject());
        setupCellValueFactory(entryDateColumn, EmployeeProperty::entryDateProperty);

        idColumn.setEditable(false);
        nameColumn.setCellFactory((TreeTableColumn<EmployeeProperty, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        nameColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setName(t.getNewValue());
            return employee;
        }));

        genderColumn.setCellFactory((TreeTableColumn<EmployeeProperty, String> param) ->
                new ComboBoxEditingCell("男", "女"));
        genderColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setGender(t.getNewValue());
            return employee;
        }));

        ageColumn.setCellFactory((TreeTableColumn<EmployeeProperty, Integer> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        ageColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setAge(t.getNewValue());
            return employee;
        }));

        phoneNumberColumn.setCellFactory((TreeTableColumn<EmployeeProperty, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        phoneNumberColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setPhoneNumber(t.getNewValue());
            return employee;
        }));

        residenceColumn.setCellFactory((TreeTableColumn<EmployeeProperty, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        residenceColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setResidence(t.getNewValue());
            return employee;
        }));

        educationColumn.setCellFactory((TreeTableColumn<EmployeeProperty, String> param) ->
                new ComboBoxEditingCell(Employee.getEducations()));
        educationColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setEducation(t.getNewValue());
            return employee;
        }));

        wageColumn.setCellFactory((TreeTableColumn<EmployeeProperty, Integer> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        wageColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setWage(t.getNewValue());
            return employee;
        }));

        entryDateColumn.setCellFactory((TreeTableColumn<EmployeeProperty, LocalDate> param) ->
                new DatePickerEditingCell());
        entryDateColumn.setOnEditCommit(t -> saveEmployeeToDatabase(t, employee -> {
            employee.setEntryDate(t.getNewValue());
            return employee;
        }));

        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        employeeData = getEmployeeData();
        Platform.runLater(() -> {
            editableTreeTableView.setRoot(new RecursiveTreeItem<>(employeeData, RecursiveTreeObject::getChildren));
            editableTreeTableViewCount.textProperty()
                    .bind(Bindings.createStringBinding(() -> "( " + editableTreeTableView.getCurrentItemsCount() + " ) ",
                            editableTreeTableView.currentItemsCountProperty()));
        });
        searchField.textProperty().addListener(setupSearchField(editableTreeTableView));
    }

    /**
     * 保存职工信息到数据库中
     *
     * @param t      表格修改事件
     * @param change 更改的信息
     * @param <T>    更改信息的类型
     */
    private <T> void saveEmployeeToDatabase(TreeTableColumn.CellEditEvent<EmployeeProperty, T> t, Function<Employee, Employee> change) {
        var currentRow = t.getRowValue().getValue().toEmployee();
        var newEmployee = change.apply(currentRow);
        try {
            newEmployee.save();
        } catch (SQLException e) {
            e.printStackTrace();
            saveFailure();
        }
    }

    private void saveFailure() {
        new PromptDialog("保存信息", "职工信息保存失败").show(rootPane);
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<EmployeeProperty> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(employeeProp -> {
                    final EmployeeProperty employeeProperty = employeeProp.getValue();
                    return Integer.toString(employeeProperty.getId()).contains(newVal)
                            || employeeProperty.getName().contains(newVal)
                            || employeeProperty.getGender().contains(newVal)
                            || Integer.toString(employeeProperty.getAge()).contains(newVal)
                            || employeeProperty.getPhoneNumber().contains(newVal)
                            || employeeProperty.getResidence().contains(newVal)
                            || employeeProperty.getEducation().contains(newVal)
                            || Integer.toString(employeeProperty.getWage()).contains(newVal)
                            || employeeProperty.getEntryDate().toString().contains(newVal);
                });
    }

    private EmployeeProperty getSelectedValue() {
        return editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
    }

    private ObservableList<EmployeeProperty> getEmployeeData() {
        final ObservableList<EmployeeProperty> employeeData = FXCollections.observableArrayList();
        Employee.getEmployees().forEach(e -> employeeData.add(new EmployeeProperty(e)));
        return employeeData;
    }

    /**
     * 处理刷新按钮点击事件
     */
    @FXML
    public void refresh() {
        new Thread(this::setupEmployeeTableView).start();
    }

}
