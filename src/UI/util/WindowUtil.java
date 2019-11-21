package UI.util;

import animatefx.animation.FadeIn;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import workbench.MainWorkbench;

import java.io.IOException;

public class WindowUtil {
    public static Stage createAuthenticationWindow() {
        var stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        Pane root = null;
        try {
            root = FXMLLoader.load(WindowUtil.class.getResource("/UI/SignInAndSignUpWindow/SignInAndSignUpWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("登录");
        assert root != null;
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        return stage;
    }

    public static Stage createMainWindow(int width, int height) {
        var workbench = new MainWorkbench().buildWorkbench();
        var scene = new Scene(workbench, width, height);
        var mainStage = new Stage();
        mainStage.setTitle("职工信息管理系统");
        mainStage.setScene(scene);
        new FadeIn(workbench).play();
        return mainStage;
    }

}
