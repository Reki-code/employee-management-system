package workbench;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import workbench.moudle.ListUserModule;
import workbench.moudle.ProfileModule;

public class MainWorkbench extends Application {

    @Override
    public void start(Stage primaryStage) {
        Workbench workbench = buildWorkbench();
        primaryStage.setTitle("管理系统");
        primaryStage.setScene(new Scene(workbench, 800, 600));
        primaryStage.show();
    }

    public static Workbench buildWorkbench() {
        var workbench = Workbench.builder(
                new ProfileModule(),
                new ListUserModule(),
                new ListUserModule(),
                new ListUserModule(),
                new ListUserModule(),
                new ListUserModule()
        )
                .modulesPerPage(6)
//                .navigationDrawerItems(
//                        new MenuItem("item1"),
//                        new MenuItem("item2")
//                )
                .toolbarLeft(new ToolbarItem("管理系统"))
                .toolbarRight(
                        new ToolbarItem("right"),
                        new ToolbarItem("EMOTICON", new MaterialDesignIconView(MaterialDesignIcon.EMOTICON)),
                        new ToolbarItem("EXIT", new MaterialDesignIconView(MaterialDesignIcon.EXIT_TO_APP), event -> System.exit(0)),
                        new ToolbarItem("ACCOUNT", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT),
                                new MenuItem("",
                                        new VBox(
                                                new JFXButton("退出", new MaterialDesignIconView(MaterialDesignIcon.EXIT_TO_APP)),
                                                new JFXButton("退出", new MaterialDesignIconView(MaterialDesignIcon.EXIT_TO_APP)),
                                                new JFXButton("退出", new MaterialDesignIconView(MaterialDesignIcon.EXIT_TO_APP))
                                        )
                                ))
                )
                .build();
        workbench.getStylesheets().add(MainWorkbench.class.getResource("main.css").toExternalForm());
        return workbench;
    }
}
