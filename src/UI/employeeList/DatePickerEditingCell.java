package UI.employeeList;

import com.jfoenix.controls.JFXDatePicker;
import javafx.scene.control.TreeTableCell;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePickerEditingCell extends TreeTableCell<EmployeeListController.EmployeeProperty, LocalDate> {
    private JFXDatePicker datePicker;
    private StringConverter<LocalDate> datePickerConverter;

    DatePickerEditingCell() {
        datePickerConverter = new StringConverter<>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            datePicker = new JFXDatePicker(getItem());
            datePicker.setDefaultColor(Color.valueOf("#0076FF"));
            datePicker.setConverter(datePickerConverter);
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(datePicker.getValue().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getItem());
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                if (datePicker != null) {
                    setText(datePicker.getValue().toString());
                } else {
                    setText(getItem().toString());
                }
                setGraphic(null);
            }
        }
    }

}
