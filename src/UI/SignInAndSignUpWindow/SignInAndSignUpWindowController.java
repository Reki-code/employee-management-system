package UI.SignInAndSignUpWindow;

import UI.dialog.InfoDialog;
import animatefx.animation.FadeIn;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import com.jfoenix.controls.*;
import data.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import workbench.MainWorkbench;

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
        new Thread(() -> Platform.runLater(() -> {
            if (user.signIn() == User.LoginStatus.SUCCESS) {
                startMainWindow();
            } else {
                var alert = new InfoDialog(Alert.AlertType.INFORMATION, "密码错误", new ButtonType("好的"));
                alert.showAndWait();
                signInBtn.setDisable(false);
                signInSpinner.setVisible(false);
            }
        })).start();
    }

    @FXML
    void signUp() {
        signUpBtn.setDisable(true);
        signUpSpinner.setVisible(true);
        User user = new User();
        user.setUsername(signUpUsername.getText());
        user.setPassword(signUpPassword.getText());
        user.setPhoneNumber(signUpPhone.getText());
        new Thread(() -> Platform.runLater(() -> {
            if (user.signUp()) {
                startMainWindow();
            } else {
                var alert = new InfoDialog(Alert.AlertType.INFORMATION, "注册失败", new ButtonType("好的"));
                alert.showAndWait();
                signUpBtn.setDisable(false);
                signUpSpinner.setVisible(false);
            }
        })).start();
    }

    private void startMainWindow() {
//        Parent mainWindow = FXMLLoader.load(getClass().getResource("../mainWindow/MainWindow.fxml"));
        var stage = (Stage) rootPane.getScene().getWindow();
        var workbench = MainWorkbench.buildWorkbench();
        var scene = new Scene(workbench, 1024, 720);
        var mainStage = new Stage();
        mainStage.setTitle("管理系统");
        mainStage.setScene(scene);
        mainStage.show();
        new FadeIn(workbench).play();

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

}
