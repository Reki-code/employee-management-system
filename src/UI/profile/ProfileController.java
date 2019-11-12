package UI.profile;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Circle avatar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var image = new Image("UI/profile/defaultAvatar.jpg");
        avatar.setFill(new ImagePattern(image));
    }
}
