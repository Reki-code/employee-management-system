package main.java.utils.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.text.Text;

public class ConfirmDialog extends JFXDialog {
    private JFXButton confirmButton = new JFXButton("确认");
    private JFXButton cancelButton = new JFXButton("取消");

    public ConfirmDialog(String title, String msg) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody(new Text(msg));
        content.setActions(confirmButton, cancelButton);
        setContent(content);
        setOverlayClose(false);
        confirmButton.setOnAction(event -> close());
        cancelButton.setOnAction(event -> close());
    }

    public ConfirmDialog setConfirmAction(Runnable action) {
        confirmButton.setOnAction(event -> {
            action.run();
            close();
        });
        return this;
    }

    public ConfirmDialog setCancelAction(Runnable action) {
        cancelButton.setOnAction(event -> {
            action.run();
            close();
        });
        return this;
    }

}
