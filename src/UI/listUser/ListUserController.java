package UI.listUser;

import UI.dialog.ConfirmDialog;
import UI.dialog.PromptDialog;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

public class ListUserController implements Initializable{

    private final ObservableList<UserProperty> userData = FXCollections.observableArrayList();
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXPasswordField password;
    @FXML
    private AnchorPane addPane;
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
    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::setupUserTableView).start();
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

        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        final ObservableList<UserProperty> userData;
        userData = fetchUserData();
        Platform.runLater(() -> {
            editableTreeTableView.setRoot(new RecursiveTreeItem<>(userData, RecursiveTreeObject::getChildren));
            editableTreeTableViewCount.textProperty()
                    .bind(Bindings.createStringBinding(() -> "( " + editableTreeTableView.getCurrentItemsCount() + " ) ",
                            editableTreeTableView.currentItemsCountProperty()));
        });
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

    private ObservableList<UserProperty> fetchUserData() {
        User.getUsers().forEach(u -> userData.add(new UserProperty(u.getUsername(), u.getPhoneNumber(), u.getId())));
        return userData;
    }

    public void deleteUser(ActionEvent event) {
        if (!editableTreeTableView.getSelectionModel().isEmpty()) {
            var selectUser = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
            if (selectUser != null && !userData.isEmpty()) {
                new ConfirmDialog("管理员管理", String.format("确定要删除管理员: %s 吗?", selectUser.getUsername()))
                        .setConfirmAction(() -> {
                            deleteUser(selectUser);
                        })
                        .show(rootPane);
            }
        }
    }

    private void deleteUser(UserProperty user) {
        if (User.delete(user.getId())) {
            userData.remove(user);
        } else {
            new PromptDialog("管理员管理", "删除失败").show(rootPane);
        }
    }

    public void addUser(ActionEvent event) {
        if (User.findUsername(username.getText())) {
            new PromptDialog("添加管理员", "用户名已存在").show(rootPane);
        } else {
            var user = new User();
            user.setUsername(username.getText());
            user.setPhoneNumber(phone.getText());
            user.setPassword(password.getText());
            if (user.signUp()) {
                new PromptDialog("添加管理员", "添加成功").show(rootPane);
                userData.add(new UserProperty(user.getUsername(), user.getPhoneNumber(), user.getId()));
            }
            addPane.toBack();
        }
    }

    public void showAddUserPane(ActionEvent event) {
        addPane.toFront();
    }

    private static final class UserProperty extends RecursiveTreeObject<UserProperty> {
        final SimpleIntegerProperty id;
        final StringProperty username;
        final StringProperty phoneNumber;

        UserProperty(String username, String phoneNumber, int id) {
            this.username = new SimpleStringProperty(username);
            this.phoneNumber = new SimpleStringProperty(phoneNumber);
            this.id = new SimpleIntegerProperty(id);
        }

        public int getId() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public String getUsername() {
            return username.get();
        }

        public String getPhoneNumber() {
            return phoneNumber.get();
        }

        StringProperty usernameProperty() {
            return username;
        }

        StringProperty phoneNumberProperty() {
            return phoneNumber;
        }

    }
}
