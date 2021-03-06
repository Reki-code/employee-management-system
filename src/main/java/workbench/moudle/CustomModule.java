package main.java.workbench.moudle;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CustomModule extends WorkbenchModule {
    private AnchorPane rootPane = new AnchorPane();
    private String fxml;

    CustomModule(String name, MaterialDesignIcon icon, String fxml) {
        super(name, icon);
        this.fxml = fxml;
        loading();
    }

    CustomModule(String name, FontAwesomeIcon icon, String fxml) {
        super(name, icon);
        this.fxml = fxml;
        loading();
    }

    private void loading() {
        var loading = new VBox(new JFXSpinner(), new Label("加载中..."));
        rootPane.getChildren().add(loading);
    }

    @Override
    public void init(Workbench workbench) {
        super.init(workbench);
        new Thread(() -> {
            var content = loadFxml();
            if (content == null) {
                content = new BorderPane();
                var label = new Label("加载失败!");
                content.getChildren().add(label);
            }
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setTopAnchor(content, 0.0);
            Pane finalContent = content;
            Platform.runLater(() -> {
                rootPane.getChildren().clear();
                rootPane.getChildren().add(finalContent);
            });
        }).start();
    }

    @Override
    public Node activate() {
        return rootPane;
    }

    private Pane loadFxml() {
        Pane parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public String getFxml() {
        return fxml;
    }
}
