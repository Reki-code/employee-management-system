package main.java.ui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import main.java.data.User;
import main.java.utils.dialog.ConfirmDialog;
import main.java.utils.dialog.PromptDialog;

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
    private ValidatorBase passwordStrength;
    private ValidatorBase newAndConfirmMatch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupValidator();
    }

    private void setupValidator() {
        var emptyValidator = new RequiredFieldValidator("不能为空");
        setEmptyValidator(oldPassword, emptyValidator);
        setEmptyValidator(newPassword, emptyValidator);
        setEmptyValidator(confirmPassword, emptyValidator);
        passwordStrength = new ValidatorBase("密码强度不足") {
            @Override
            protected void eval() {
                hasErrors.set(!(numberLabel.isStrikethrough() && letterLabel.isStrikethrough() && charsLabel.isStrikethrough()));
            }
        };
        newPassword.getValidators().add(passwordStrength);
        newPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            numberLabel.setStrikethrough(newValue.matches(".*\\d+.*"));
            letterLabel.setStrikethrough(newValue.matches(".*[a-zA-Z]+.*"));
            charsLabel.setStrikethrough(6 <= newValue.length());
            if (!newValue.isEmpty()) {
                newPassword.validate();
            }
        });
        newAndConfirmMatch = new ValidatorBase("确认密码和新密码不匹配") {
            @Override
            protected void eval() {
                hasErrors.set(!newPassword.getText().equals(confirmPassword.getText()));
            }
        };
        confirmPassword.getValidators().add(newAndConfirmMatch);
        confirmPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                confirmPassword.validate();
            }
        });
    }

    private void setEmptyValidator(JFXPasswordField textField, RequiredFieldValidator validator) {
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                textField.validate();
            }
        });
    }

    @FXML
    private void changePassword(ActionEvent event) {
        if (oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
            new PromptDialog("修改密码", "密码不能为空").show(rootPane);
        } else if (passwordStrength.hasErrorsProperty().getValue() || newAndConfirmMatch.hasErrorsProperty().getValue()) {
            new PromptDialog("修改密码", "密码强度不足").show(rootPane);
        } else {
            new ConfirmDialog("修改密码", "您确认要修改密码吗?")
                    .setConfirmAction(this::changePassword)
                    .show(rootPane);
        }
    }

    private void changePassword() {
        var currentUser = User.getCurrentUser();
        String tip;
        if (!currentUser.checkPassword(oldPassword.getText())) {
            tip = "密码错误";
        } else {
            if (User.updateUserDetail("password", newPassword.getText(), currentUser.getId())) {
                tip = "修改成功";
            } else {
                tip = "修改失败";
            }
        }
        new PromptDialog("修改密码", tip).show(rootPane);
    }

}
