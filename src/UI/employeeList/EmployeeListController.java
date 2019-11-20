package UI.employeeList;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import data.User;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class EmployeeListController implements Initializable {


    @FXML
    private JFXTreeTableView<EmployeeProperty> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, String> userNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, String> phoneNumberEditableColumn;
    @FXML
    private JFXTreeTableColumn<EmployeeProperty, Integer> idEditableColumn;
    @FXML
    private Label editableTreeTableViewCount;
    @FXML
    private JFXTextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::setupUserTableView).start();
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

    private void setupUserTableView() {
        setupCellValueFactory(userNameEditableColumn, EmployeeProperty::usernameProperty);
        setupCellValueFactory(phoneNumberEditableColumn, EmployeeProperty::phoneNumberProperty);
        setupCellValueFactory(idEditableColumn, p -> p.id.asObject());

        userNameEditableColumn.setCellFactory((TreeTableColumn<EmployeeProperty, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        userNameEditableColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<EmployeeProperty, String> t) -> {
            if (!t.getOldValue().equals(t.getNewValue())) {
                var userProperty = t.getRowValue().getValue();
                User.updateUserDetail("username", t.getNewValue(), userProperty.id.get());
                userProperty.username.set(t.getNewValue());
            }
        });
        phoneNumberEditableColumn.setCellFactory((TreeTableColumn<EmployeeProperty, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        phoneNumberEditableColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<EmployeeProperty, String> t) -> {
            if (!t.getOldValue().equals(t.getNewValue())) {
                var userProperty = t.getRowValue().getValue();
                User.updateUserDetail("phoneNumber", t.getNewValue(), userProperty.id.get());
                userProperty.phoneNumber.set(t.getNewValue());
            }
        });

        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        final ObservableList<EmployeeProperty> userData = getUserData();
        Platform.runLater(() -> {
            editableTreeTableView.setRoot(new RecursiveTreeItem<>(userData, RecursiveTreeObject::getChildren));
            editableTreeTableViewCount.textProperty()
                    .bind(Bindings.createStringBinding(() -> "( " + editableTreeTableView.getCurrentItemsCount() + " ) ",
                            editableTreeTableView.currentItemsCountProperty()));
        });
        searchField.textProperty()
                .addListener(setupSearchField(editableTreeTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<EmployeeProperty> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(userProp -> {
                    final EmployeeProperty userProperty = userProp.getValue();
                    return userProperty.username.get().contains(newVal)
                            || userProperty.phoneNumber.get().contains(newVal)
                            || Integer.toString(userProperty.id.get()).contains(newVal);
                });
    }

    private ObservableList<EmployeeProperty> getUserData() {
        final ObservableList<EmployeeProperty> userData = FXCollections.observableArrayList();
        User.getUsers().forEach(u -> userData.add(new EmployeeProperty(u.getUsername(), u.getPhoneNumber(), u.getId())));
        return userData;
    }

    private static final class EmployeeProperty extends RecursiveTreeObject<EmployeeProperty> {
        final SimpleIntegerProperty id;
        final StringProperty username;
        final StringProperty phoneNumber;

        EmployeeProperty(String username, String phoneNumber, int id) {
            this.username = new SimpleStringProperty(username);
            this.phoneNumber = new SimpleStringProperty(phoneNumber);
            this.id = new SimpleIntegerProperty(id);
        }

        StringProperty usernameProperty() {
            return username;
        }

        StringProperty phoneNumberProperty() {
            return phoneNumber;
        }

    }
}
