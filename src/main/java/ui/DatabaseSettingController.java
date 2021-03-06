package main.java.ui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import main.java.data.DatabaseHandler;
import main.java.utils.dialog.PromptDialog;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class DatabaseSettingController implements Initializable {
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXTextField hostname;
    @FXML
    private JFXTextField port;
    @FXML
    private JFXTextField user;
    @FXML
    private JFXPasswordField pass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var prefs = Preferences.userNodeForPackage(DatabaseHandler.class);
        hostname.setText(prefs.get("host_name", DatabaseHandler.getDefaultHostName()));
        port.setText(prefs.get("host_port", DatabaseHandler.getDefaultHostPort()));
        user.setText(prefs.get("user", DatabaseHandler.getDefaultUser()));
        pass.setText(prefs.get("pass", DatabaseHandler.getDefaultPass()));
    }

    /**
     * 处理设置按钮点击事件
     */
    @FXML
    public void set() {
        var host = this.hostname.getText();
        var port = this.port.getText();
        var user = this.user.getText();
        var pass = this.pass.getText();
        saveToPreference(host, port, user, pass);
        new PromptDialog("数据库设置", "保存成功, 重启后生效").show(rootPane);
    }

    /**
     * 处理重置按钮点击事件
     */
    @FXML
    public void reset() {
        var host = DatabaseHandler.getDefaultHostName();
        var port = DatabaseHandler.getDefaultHostPort();
        var user = DatabaseHandler.getDefaultUser();
        var pass = DatabaseHandler.getDefaultPass();
        this.hostname.setText(host);
        this.port.setText(port);
        this.user.setText(user);
        this.pass.setText(pass);
        saveToPreference(host, port, user, pass);
        new PromptDialog("数据库设置", "重置成功, 重启后生效").show(rootPane);
    }

    private void saveToPreference(String host, String port, String user, String pass) {
        var prefs = Preferences.userNodeForPackage(DatabaseHandler.class);
        prefs.put("host_name", host);
        prefs.put("host_port", port);
        prefs.put("user", user);
        prefs.put("pass", pass);
    }
}
