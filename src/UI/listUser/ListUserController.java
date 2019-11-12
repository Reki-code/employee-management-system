package UI.listUser;

import com.jfoenix.controls.*;
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

public class ListUserController implements Initializable{

    @FXML
    private JFXTreeTableView<UserProperty> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<UserProperty, String> userNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<UserProperty, String> phoneNumberEditableColumn;
    @FXML
    private JFXTreeTableColumn<UserProperty, Integer> idEditableColumn;
    @FXML
    private Label editableTreeTableViewCount;
    @FXML
    private JFXTextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> Platform.runLater(this::setupUserTableView)).start();
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<UserProperty, T> column, Function<UserProperty, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserProperty, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupUserTableView() {
        setupCellValueFactory(userNameEditableColumn, UserProperty::usernameProperty);
        setupCellValueFactory(phoneNumberEditableColumn, UserProperty::phoneNumberProperty);
        setupCellValueFactory(idEditableColumn, p -> p.id.asObject());

        userNameEditableColumn.setCellFactory((TreeTableColumn<UserProperty, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        userNameEditableColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<UserProperty, String> t) -> {
            if (!t.getOldValue().equals(t.getNewValue())) {
                var userProperty = t.getRowValue().getValue();
                User.updateUserDetail("username", t.getNewValue(), userProperty.id.get());
                userProperty.username.set(t.getNewValue());
            }
        });
        phoneNumberEditableColumn.setCellFactory((TreeTableColumn<UserProperty, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        phoneNumberEditableColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<UserProperty, String> t) -> {
            if (!t.getOldValue().equals(t.getNewValue())) {
                var userProperty = t.getRowValue().getValue();
                User.updateUserDetail("phoneNumber", t.getNewValue(), userProperty.id.get());
                userProperty.phoneNumber.set(t.getNewValue());
            }
        });

        final ObservableList<UserProperty> userData = getUserData();
        editableTreeTableView.setRoot(new RecursiveTreeItem<>(userData, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        editableTreeTableViewCount.textProperty()
                .bind(Bindings.createStringBinding(() -> "( " + editableTreeTableView.getCurrentItemsCount() + " ) ",
                        editableTreeTableView.currentItemsCountProperty()));
        searchField.textProperty()
                .addListener(setupSearchField(editableTreeTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<UserProperty> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(userProp -> {
                    final UserProperty userProperty = userProp.getValue();
                    return userProperty.username.get().contains(newVal)
                            || userProperty.phoneNumber.get().contains(newVal)
                            || Integer.toString(userProperty.id.get()).contains(newVal);
                });
    }

    private ObservableList<UserProperty> getUserData() {
        final ObservableList<UserProperty> userData = FXCollections.observableArrayList();
        User.getUsers().forEach( u -> userData.add(new UserProperty(u.getUsername(), u.getPhoneNumber(), u.getId())));
        return userData;
    }

    static final class UserProperty extends RecursiveTreeObject<UserProperty> {
        final SimpleIntegerProperty id;
        final StringProperty username;
        final StringProperty phoneNumber;

        UserProperty(String username, String phoneNumber, int id) {
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
