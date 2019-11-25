package workbench.moudle;

import UI.employeeList.EmployeeListController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class EmployeeModule extends CustomModule {
    private EmployeeListController employeeListController;
    public EmployeeModule() {
        super("职工信息", FontAwesomeIcon.GROUP, "/UI/employeeList/EmployeeList.fxml");
    }

    @Override
    protected Pane loadFxml() {
        Pane parent = null;
        try {
            var loader = new FXMLLoader(getClass().getResource(getFxml()));
            parent = loader.load();
            employeeListController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }
}
