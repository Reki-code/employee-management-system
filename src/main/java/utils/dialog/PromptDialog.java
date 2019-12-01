package main.java.utils.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.text.Text;

public class PromptDialog extends JFXDialog {
    public PromptDialog(String title, String msg) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody(new Text(msg));
        JFXButton confirmButton = new JFXButton("确认");
        content.setActions(confirmButton);
        setContent(content);
        confirmButton.setOnAction(event -> close());
    }
}
