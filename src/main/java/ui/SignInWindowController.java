package main.java.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.data.User;
import main.java.utils.WindowUtil;
import main.java.utils.dialog.InfoDialog;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SignInWindowController implements Initializable {
    @FXML
    private JFXTextField loginUsername;
    @FXML
    private JFXPasswordField loginPassword;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXButton signInBtn;
    @FXML
    private JFXSpinner signInSpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var prefs = Preferences.userNodeForPackage(main.java.data.User.class);
        loginUsername.setText(prefs.get("username", ""));
    }

    @FXML
    void signIn() {
        signInBtn.setDisable(true);
        signInSpinner.setVisible(true);
        User user = new User();
        user.setUsername(loginUsername.getText());
        user.setPassword(loginPassword.getText());
        new Thread(() -> {
            if (user.signIn() == User.LoginStatus.SUCCESS) {
                Platform.runLater(this::startMainWindow);
                var prefs = Preferences.userNodeForPackage(main.java.data.User.class);
                prefs.put("username", user.getUsername());
            } else {
                Platform.runLater(() -> {
                    var alert = new InfoDialog(Alert.AlertType.INFORMATION, "密码错误", new ButtonType("好的"));
                    alert.showAndWait();
                    signInBtn.setDisable(false);
                    signInSpinner.setVisible(false);
                });
            }
        }).start();
    }

    private void startMainWindow() {
        WindowUtil.createMainWindow(1024, 720).show();

        var stage = (Stage) rootPane.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void onPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            signIn();
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

}
