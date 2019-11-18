package UI.SignInAndSignUpWindow;

import UI.dialog.InfoDialog;
import UI.util.WindowUtil;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import data.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignInAndSignUpWindowController {

    public JFXTextField loginUsername;
    public JFXPasswordField loginPassword;
    public JFXTextField signUpPhone;
    public JFXTextField signUpUsername;
    public JFXPasswordField signUpPassword;
    public JFXPasswordField signUpConfirmPassword;
    public AnchorPane slideAnchor;
    public AnchorPane rootPane;
    public JFXButton signInBtn;
    public JFXButton signUpBtn;
    public JFXSpinner signInSpinner;
    public JFXSpinner signUpSpinner;

    private SlideState slideState;
    private enum SlideState {
        LEFT,
        RIGHT
    }

    public SignInAndSignUpWindowController() {
        slideState = SlideState.RIGHT;
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

    @FXML
    void signUp() {
        signUpBtn.setDisable(true);
        signUpSpinner.setVisible(true);
        User user = new User();
        user.setUsername(signUpUsername.getText());
        user.setPassword(signUpPassword.getText());
        user.setPhoneNumber(signUpPhone.getText());
        new Thread(() -> {
            if (user.signUp()) {
                Platform.runLater(this::startMainWindow);
            } else {
                Platform.runLater(() -> {
                    var alert = new InfoDialog(Alert.AlertType.INFORMATION, "注册失败", new ButtonType("好的"));
                    alert.showAndWait();
                    signUpBtn.setDisable(false);
                    signUpSpinner.setVisible(false);
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
    void slide() {
        switch (slideState) {
            case LEFT:
                slideState = SlideState.RIGHT;
                slideAnchor.setLayoutX(565);
                new SlideInLeft(slideAnchor).play();
                break;
            case RIGHT:
                slideState = SlideState.LEFT;
                slideAnchor.setLayoutX(0);
                new SlideInRight(slideAnchor).play();
                break;
        }
    }

    @FXML
    private void onPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            switch (slideState) {
                case LEFT:
                    signUp();
                    break;
                case RIGHT:
                    signIn();
                    break;
            }
        }
    }
}
