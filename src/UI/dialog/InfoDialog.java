package UI.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class InfoDialog extends Alert {

    public InfoDialog(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        getDialogPane().getStylesheets().add(
                getClass().getResource("InfoDialog.css").toExternalForm()
        );
        setHeaderText(null);
        initStyle(StageStyle.UNDECORATED);
    }
}
