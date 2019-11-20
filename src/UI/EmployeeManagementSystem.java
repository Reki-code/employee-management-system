package UI;

import UI.util.WindowUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class EmployeeManagementSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        WindowUtil.createAuthenticationWindow().show();
        primaryStage.hide();
    }
}
