package UI.changePassword;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private StackPane rootPane;
    @FXML
    private Text letterLabel;
    @FXML
    private Text numberLabel;
    @FXML
    private Text charsLabel;
    @FXML
    private JFXPasswordField oldPassword;
    @FXML
    private JFXPasswordField newPassword;
    @FXML
    private JFXPasswordField confirmPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            numberLabel.setStrikethrough(newValue.matches(".*\\d+.*"));
            letterLabel.setStrikethrough(newValue.matches(".*[a-zA-Z]+.*"));
            charsLabel.setStrikethrough(6 <= newValue.length());
        });
    }

    @FXML
    private void changePassword(ActionEvent event) {
        var currentUser = User.getCurrentUser();
        System.out.println(currentUser);
        var pane = new Pane();
        var dialog = new JFXDialog();
        dialog.setOverlayClose(true);
        dialog.setContent(pane);
        String tip;
        if (!currentUser.checkPassword(oldPassword.getText())) {
            tip = "密码错误";
        } else {
            if (User.updateUserDetail("password", newPassword.getText(), currentUser.getId())) {
                tip = "修改成功";
                clearField();
            } else {
                tip = "密码错误";
            }
        }
        pane.getChildren().add(new Label(tip));
        dialog.show(rootPane);
    }

    private void clearField() {
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }
}
