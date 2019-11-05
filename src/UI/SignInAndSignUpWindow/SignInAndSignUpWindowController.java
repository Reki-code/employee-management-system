package UI.SignInAndSignUpWindow;

import UI.dialog.InfoDialog;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import com.jfoenix.controls.*;
import data.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInAndSignUpWindowController {

    public JFXTextField loginUsername;
    public JFXPasswordField loginPassword;
    public JFXTextField signUpPhone;
    public JFXTextField signUpUsername;
    public JFXPasswordField signUpPassword;
    public JFXPasswordField signUpConfirmPassword;
    public AnchorPane slideAnchor;
    public AnchorPane rootPane;

    private SlideState slideState;
    private enum SlideState {
        LEFT,
        RIGHT
    }

    public SignInAndSignUpWindowController() {
        slideState = SlideState.RIGHT;
    }

    @FXML
    void signIn() throws IOException {
        User user = new User();
        user.setUsername(loginUsername.getText());
        user.setPassword(loginPassword.getText());
        if (user.signIn() == User.LoginStatus.SUCCESS) {
            startMainWindow();
        } else {
            var alert = new InfoDialog(Alert.AlertType.INFORMATION, "密码错误", new ButtonType("好的"));
            alert.showAndWait();
        }
    }

    @FXML
    void signUp() throws IOException {
        User user = new User();
        user.setUsername(signUpUsername.getText());
        user.setPassword(signUpPassword.getText());
        user.setPhoneNumber(signUpPhone.getText());
        if (user.signUp()) {
            startMainWindow();
        } else {
            var alert = new InfoDialog(Alert.AlertType.INFORMATION, "注册失败", new ButtonType("好的"));
            alert.showAndWait();
        }
    }

    private void startMainWindow() throws IOException {
        Parent mainWindow = FXMLLoader.load(getClass().getResource("../mainWindow/MainWindow.fxml"));
        var stage = (Stage) rootPane.getScene().getWindow();
        var scene = new Scene(mainWindow);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
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
