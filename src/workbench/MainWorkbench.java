package workbench;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import data.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import workbench.moudle.*;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MainWorkbench {
    private Workbench workbench;
    private WorkbenchModule listUserModule = new ListUserModule();
    private EmployeeModule employeeModule = new EmployeeModule();
    private WorkbenchModule addEmployeeModule = new AddEmployeeModule();
    private WorkbenchModule averageAnalysisModule = new AverageAnalysisModule();
    private WorkbenchModule ageStatisticsModule = new AgeStatisticsModule();
    private WorkbenchModule educationStatisticsModule = new EducationStatisticsModule();
    private WorkbenchModule searchEmployeeModule = new SearchEmployeeModule();
    private WorkbenchModule changePasswordModule = new ChangePasswordModule();
    private WorkbenchModule databaseSettingModule = new DatabaseSettingModule();

    public Workbench buildWorkbench() {
        workbench = Workbench.builder(
                listUserModule,
                employeeModule,
                addEmployeeModule,
                averageAnalysisModule,
                ageStatisticsModule,
                searchEmployeeModule,
                changePasswordModule,
                educationStatisticsModule,
                databaseSettingModule
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
        var searchEmployee = new MenuItem("查询职工信息", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_SEARCH));
        searchEmployee.setOnAction(event -> workbench.openModule(searchEmployeeModule));

        return new ToolbarItem("职工", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_MULTIPLE),
                employeeInfo,
                employeeAdd,
                searchEmployee
        );
    }

    private ToolbarItem statisticsMenu() {
        var averageStatistics = new MenuItem("平均值", new MaterialDesignIconView(MaterialDesignIcon.MARGIN));
        averageStatistics.setOnAction(event -> workbench.openModule(averageAnalysisModule));
        var ageStatistics = new MenuItem("年龄段", new MaterialDesignIconView(MaterialDesignIcon.MARGIN));
        ageStatistics.setOnAction(event -> workbench.openModule(ageStatisticsModule));
        var educationStatistics = new MenuItem("文化程度", new MaterialDesignIconView(MaterialDesignIcon.MARGIN));
        educationStatistics.setOnAction(event -> workbench.openModule(educationStatisticsModule));

        return new ToolbarItem("统计", new MaterialDesignIconView(MaterialDesignIcon.MARGIN),
                averageStatistics,
                ageStatistics,
                educationStatistics
        );
    }

    private ToolbarItem settingMenu() {
        var databaseSetting = new MenuItem("数据库设置", new MaterialDesignIconView(MaterialDesignIcon.DATABASE));
        databaseSetting.setOnAction(event -> workbench.openModule(databaseSettingModule));

        return new ToolbarItem("设置", new MaterialDesignIconView(MaterialDesignIcon.SETTINGS),
                databaseSetting
        );
    }

    private ToolbarItem userMenu() {
        var userInfo = new MenuItem("我的信息", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_CARD_DETAILS));
        userInfo.setOnAction(event -> workbench.showDialog(profileDetail()));
        var changePassword = new MenuItem("修改密码", new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_SETTINGS_VARIANT));
        changePassword.setOnAction(event -> workbench.openModule(changePasswordModule));
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

    private WorkbenchDialog databaseSetting() {
        Node databaseSetting = null;
        try {
            databaseSetting = FXMLLoader.load(getClass().getResource("/UI/databaseSetting/DatabaseSetting.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WorkbenchDialog.builder(
                "数据库设置", databaseSetting)
                .blocking(true)
                .build();
    }

    private WorkbenchDialog profileDetail() {
        Node profile = null;
        try {
            profile = FXMLLoader.load(getClass().getResource("/UI/profile/Profile.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WorkbenchDialog.builder(
                "个人信息", profile)
                .blocking(false)
                .build();
    }

}
