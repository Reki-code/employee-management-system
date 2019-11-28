package UI.employeeList;

import UI.dialog.ConfirmDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import data.Employee;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;

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

    @FXML
    private void delEmployee() {
        if (!editableTreeTableView.getSelectionModel().isEmpty()) {
            new ConfirmDialog("删除", "确认要删除该职工的信息吗?")
                    .setConfirmAction(() -> delEmployee(getSelectedValue().toEmployee()))
                    .setCancelAction(() -> {
                    })
                    .show(rootPane);
        }
    }

    private void delEmployee(Employee employee) {
        try {
            if (employee.delete()) {
                employeeData.removeIf(e -> e.id.get() == employee.getId());
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

    private void setupEmployeeTableView() {
        setupCellValueFactory(idColumn, p -> p.id.asObject());
        setupCellValueFactory(nameColumn, EmployeeProperty::nameProperty);
        setupCellValueFactory(genderColumn, EmployeeProperty::genderProperty);
        setupCellValueFactory(ageColumn, p -> p.age.asObject());
        setupCellValueFactory(phoneNumberColumn, EmployeeProperty::phoneNumberProperty);
        setupCellValueFactory(residenceColumn, EmployeeProperty::residenceProperty);
        setupCellValueFactory(educationColumn, EmployeeProperty::educationProperty);
        setupCellValueFactory(wageColumn, p -> p.wage.asObject());
        setupCellValueFactory(entryDateColumn, p -> p.entryDate);

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

        educationColumn.setCellFactory(((TreeTableColumn<EmployeeProperty, String> param) ->
                new ComboBoxEditingCell(Employee.getEducations())));
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

        entryDateColumn.setCellFactory(((TreeTableColumn<EmployeeProperty, LocalDate> param) ->
                new DatePickerEditingCell()));
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
        System.out.println("保存失败");
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<EmployeeProperty> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(employeeProp -> {
                    final EmployeeProperty employeeProperty = employeeProp.getValue();
                    return Integer.toString(employeeProperty.id.get()).contains(newVal)
                            || employeeProperty.name.get().contains(newVal)
                            || employeeProperty.gender.get().contains(newVal)
                            || Integer.toString(employeeProperty.age.get()).contains(newVal)
                            || employeeProperty.phoneNumber.get().contains(newVal)
                            || employeeProperty.residence.get().contains(newVal)
                            || employeeProperty.education.get().contains(newVal)
                            || Integer.toString(employeeProperty.wage.get()).contains(newVal)
                            || employeeProperty.entryDate.toString().contains(newVal);
                });
    }

    public EmployeeProperty getSelectedValue() {
        return editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
    }

    private ObservableList<EmployeeProperty> getEmployeeData() {
        final ObservableList<EmployeeProperty> employeeData = FXCollections.observableArrayList();
        Employee.getEmployees().forEach(e -> employeeData.add(new EmployeeProperty(e)));
        return employeeData;
    }

    @FXML
    public void refresh() {
        new Thread(this::setupEmployeeTableView).start();
    }

    static final class EmployeeProperty extends RecursiveTreeObject<EmployeeProperty> {
        final SimpleIntegerProperty id;
        final StringProperty name;
        final StringProperty gender;
        final SimpleIntegerProperty age;
        final StringProperty phoneNumber;
        final StringProperty residence;
        final StringProperty education;
        final SimpleIntegerProperty wage;
        final ObjectProperty<LocalDate> entryDate;

        EmployeeProperty(Employee employee) {
            id = new SimpleIntegerProperty(employee.getId());
            name = new SimpleStringProperty(employee.getName());
            gender = new SimpleStringProperty(employee.getGender());
            age = new SimpleIntegerProperty(employee.getAge());
            phoneNumber = new SimpleStringProperty(employee.getPhoneNumber());
            residence = new SimpleStringProperty(employee.getResidence());
            education = new SimpleStringProperty(employee.getEducation());
            wage = new SimpleIntegerProperty(employee.getWage());
            entryDate = new SimpleObjectProperty<>(employee.getEntryDate());
        }

        public Employee toEmployee() {
            return new Employee(id.get(), name.get(), gender.get(), age.get(), phoneNumber.get(), residence.get(), education.get(), wage.get(), entryDate.get());
        }

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public StringProperty nameProperty() {
            return name;
        }

        public String getGender() {
            return gender.get();
        }

        public void setGender(String gender) {
            this.gender.set(gender);
        }

        public StringProperty genderProperty() {
            return gender;
        }

        public int getAge() {
            return age.get();
        }

        public void setAge(int age) {
            this.age.set(age);
        }

        public SimpleIntegerProperty ageProperty() {
            return age;
        }

        public String getPhoneNumber() {
            return phoneNumber.get();
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber.set(phoneNumber);
        }

        public StringProperty phoneNumberProperty() {
            return phoneNumber;
        }

        public String getResidence() {
            return residence.get();
        }

        public void setResidence(String residence) {
            this.residence.set(residence);
        }

        public StringProperty residenceProperty() {
            return residence;
        }

        public String getEducation() {
            return education.get();
        }

        public void setEducation(String education) {
            this.education.set(education);
        }

        public StringProperty educationProperty() {
            return education;
        }

        public int getWage() {
            return wage.get();
        }

        public void setWage(int wage) {
            this.wage.set(wage);
        }

        public SimpleIntegerProperty wageProperty() {
            return wage;
        }

        public LocalDate getEntryDate() {
            return entryDate.get();
        }

        public void setEntryDate(LocalDate entryDate) {
            this.entryDate.set(entryDate);
        }

        public ObjectProperty<LocalDate> entryDateProperty() {
            return entryDate;
        }

        @Override
        public String toString() {
            return "EmployeeProperty{" +
                    "id=" + id +
                    ", name=" + name +
                    ", gender=" + gender +
                    ", age=" + age +
                    ", phoneNumber=" + phoneNumber +
                    ", residence=" + residence +
                    ", education=" + education +
                    ", wage=" + wage +
                    ", entryDate=" + entryDate +
                    '}';
        }
    }
}
