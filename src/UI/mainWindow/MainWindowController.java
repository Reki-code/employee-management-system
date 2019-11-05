package UI.mainWindow;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainWindowController implements Initializable {
    public Pane contentPane;
    public JFXButton Dashboard;
    public JFXButton addbook;
    public JFXButton brrowbook;
    public JFXButton listbook;
    public JFXButton addmember;
    public JFXButton six;
    private List<JFXButton> buttonList = new ArrayList<>();
    private Map<JFXButton, Node> content = new HashMap<>();

    @FXML
    void menuButtonHandler(ActionEvent event) {
        var selected = PseudoClass.getPseudoClass("selected");
        for (var btn : buttonList) {
            if (event.getSource() == btn) {
                btn.pseudoClassStateChanged(selected, true);
                var contentPaneChildren = contentPane.getChildren();
                var node = content.get(btn);
                if (!contentPaneChildren.contains(node)) {
                    contentPaneChildren.clear();
                    contentPaneChildren.addAll(node);
                    new FadeIn(node).play();
                }
            } else {
                btn.pseudoClassStateChanged(selected, false);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonList.add(Dashboard);
        buttonList.add(addbook);
        buttonList.add(brrowbook);
        buttonList.add(listbook);
        buttonList.add(addmember);
        buttonList.add(six);
        Dashboard.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        try {
            content.put(Dashboard, FXMLLoader.load(getClass().getResource("../dashboard/Dashboard.fxml")));
            content.put(addbook, FXMLLoader.load(getClass().getResource("../dashboard/Dashboard.fxml")));
            content.put(brrowbook, FXMLLoader.load(getClass().getResource("../dashboard/Dashboard.fxml")));
            content.put(listbook, FXMLLoader.load(getClass().getResource("../dashboard/Dashboard.fxml")));
            content.put(addmember, FXMLLoader.load(getClass().getResource("../listUser/ListUser.fxml")));
            content.put(six, FXMLLoader.load(getClass().getResource("../dashboard/Dashboard.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentPane.getChildren().addAll(content.get(Dashboard));
    }

}
