package UI.swingWindow;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import workbench.moudle.ListUserModule;

import javax.swing.*;
import java.awt.*;

public class MainJFrame extends JFrame {
    private final JFXPanel jfxPanel = new JFXPanel();
    private final JPanel jPanel = new JPanel(new BorderLayout());
    private Group root = new Group();

    public MainJFrame() {
        super();
        initComponents();
    }

    private void initComponents() {

        new Thread(() -> {
            Platform.runLater(this::initFX);
        }).start();

        add(jPanel);
        setPreferredSize(new Dimension(1024, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    private void initFX() {
        System.out.println("init FX");
        var module = new ListUserModule();
        Scene scene = new Scene(root, 1024, 720);
        root.getChildren().add(module.activate());
        jfxPanel.setScene(scene);
    }
}
