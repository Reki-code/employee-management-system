package main.java.workbench;

import com.dlsc.workbenchfx.Workbench;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWorkbenchTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Workbench workbench = new MainWorkbench().buildWorkbench();
        primaryStage.setTitle("职工信息管理系统");
        primaryStage.setScene(new Scene(workbench, 1024, 720));
        primaryStage.show();
    }
}
