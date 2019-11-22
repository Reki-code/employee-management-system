package UI.addEmployee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {
    public JFXTextField id;
    @FXML
    private JFXButton addButton;
    private SimpleBooleanProperty buttonDisableProperty = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.disableProperty().bind(buttonDisableProperty);
        buttonDisableProperty.set(true);
        id.textProperty().addListener(((observable, oldValue, newValue) -> checkButtonDisable()));
    }

    private void checkButtonDisable() {
        if (id.getText().isEmpty()) {
            buttonDisableProperty.set(true);
        } else {
            buttonDisableProperty.set(false);
        }
    }
}
