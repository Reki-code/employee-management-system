package workbench;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import data.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import workbench.moudle.*;

import java.util.prefs.Preferences;

public class MainWorkbench {
    private Workbench workbench;
    private WorkbenchModule profileModule = new ProfileModule();
    private WorkbenchModule listUserModule = new ListUserModule();
    private WorkbenchModule employeeModule = new EmployeeModule();
    private WorkbenchModule addEmployeeModule = new AddEmployeeModule();
    private WorkbenchModule averageAnalysisModule = new AverageAnalysisModule();

    public Workbench buildWorkbench() {
        workbench = Workbench.builder(
                profileModule,
                listUserModule,
                employeeModule,
                addEmployeeModule,
                averageAnalysisModule
        )
                .modulesPerPage(6)
                .toolbarLeft(
                        employeeMenu(),
                        statisticsMenu(),
                        settingMenu()
                )
                .toolbarRight(
                        userMenu()
                )
                .build();
        workbench.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        return workbench;
    }

    private String username() {
        String username = "";
        if (User.getCurrentUser() != null) {
            username = User.getCurrentUser().getUsername();
        }
        return username;
    }

    private ToolbarItem employeeMenu() {
        var employeeInfo = new MenuItem("职工信息", new FontAwesomeIconView(FontAwesomeIcon.GROUP));
        employeeInfo.setOnAction(event -> workbench.openModule(employeeModule));
        var employeeAdd = new MenuItem("添加职工信息", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_PLUS));
        employeeAdd.setOnAction(event -> workbench.openModule(addEmployeeModule));
        var employeeDel = new MenuItem("删除职工信息", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_MINUS));

        return new ToolbarItem("职工", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_MULTIPLE),
                employeeInfo,
                employeeAdd,
                employeeDel
        );
    }

    private ToolbarItem statisticsMenu() {
        var averageStatistics = new MenuItem("平均值", new MaterialDesignIconView(MaterialDesignIcon.MARGIN));
        var ageStatistics = new MenuItem("年龄段", new MaterialDesignIconView(MaterialDesignIcon.MARGIN));
        var educationStatistics = new MenuItem("文化程度", new MaterialDesignIconView(MaterialDesignIcon.MARGIN));

        return new ToolbarItem("统计", new MaterialDesignIconView(MaterialDesignIcon.MARGIN),
                averageStatistics,
                ageStatistics,
                educationStatistics
        );
    }

    private ToolbarItem settingMenu() {
        var databaseSetting = new MenuItem("数据库设置", new MaterialDesignIconView(MaterialDesignIcon.DATABASE));
        databaseSetting.setOnAction(event -> workbench.showConfirmationDialog("title", "message", System.out::println));

        return new ToolbarItem("设置", new MaterialDesignIconView(MaterialDesignIcon.SETTINGS),
                databaseSetting
        );
    }

    private ToolbarItem userMenu() {
        var userInfo = new MenuItem("我的信息", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_CARD_DETAILS));
        userInfo.setOnAction(event -> workbench.openModule(profileModule));
        var changePassword = new MenuItem("修改密码", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_CARD_DETAILS));
        var logout = new MenuItem("退出");
        logout.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.LOGOUT_VARIANT));
        logout.setOnAction(event -> {
            var prefs = Preferences.userNodeForPackage(data.User.class);
            prefs.remove("username");
            System.exit(0);
        });

        return new ToolbarItem(username(), new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_CIRCLE),
                userInfo,
                changePassword,
                new SeparatorMenuItem(),
                logout
        );
    }
}
