package workbench.moudle;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class CustomModule extends WorkbenchModule {
    private AnchorPane rootPane = new AnchorPane();
    private String fxml;

    CustomModule(String name, MaterialDesignIcon icon, String fxml) {
        super(name, icon);
        this.fxml = fxml;
        rootPane.getChildren().add(new Label("加载中..."));
    }

    @Override
    public Node activate() {
        new Thread(() -> Platform.runLater(this::loadFxml)).start();
        return rootPane;
    }

    private void loadFxml() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxml));
            rootPane.getChildren().clear();
            rootPane.getChildren().add(parent);
            AnchorPane.setBottomAnchor(parent, 0.0);
            AnchorPane.setLeftAnchor(parent, 0.0);
            AnchorPane.setRightAnchor(parent, 0.0);
            AnchorPane.setTopAnchor(parent, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
