package UI.profile;

import data.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Label username;
    @FXML
    private Label phoneNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = User.getCurrentUser();
        if (currentUser != null) {
            username.setText(currentUser.getUsername());
            phoneNumber.setText(currentUser.getPhoneNumber());
        }
    }
}
