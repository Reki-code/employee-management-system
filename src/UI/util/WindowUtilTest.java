package UI.util;

import javafx.application.Application;
import javafx.stage.Stage;

public class WindowUtilTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        WindowUtil.createAuthenticationWindow().show();
        primaryStage.hide();
    }
}
