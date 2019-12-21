package main.java.utils.editingcell;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableCell;
import main.java.utils.EmployeeProperty;

public class ComboBoxEditingCell extends TreeTableCell<EmployeeProperty, String> {
    private JFXComboBox<String> comboBox;
    private ObservableList<String> data;

    public ComboBoxEditingCell(String... items) {
        data = FXCollections.observableArrayList(items);
//        comboBox = new JFXComboBox<>(main.java.data);
//        comboBox.setOnAction(event -> {
//            commitEdit(comboBox.getValue());
//        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                setText(getItem());
                if (comboBox != null) {
                    comboBox.setValue(getItem());
                }
                setGraphic(comboBox);
            } else {
                setText(getItem());
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            comboBox = new JFXComboBox<>(data);
            comboBox.setOnAction(event -> {
                commitEdit(comboBox.getValue());
            });
            setText(getItem());
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem());
        setGraphic(null);
    }

}

