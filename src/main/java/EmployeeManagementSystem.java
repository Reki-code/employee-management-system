package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.utils.WindowUtil;

public class EmployeeManagementSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        WindowUtil.createAuthenticationWindow().show();
        primaryStage.hide();
    }
}
