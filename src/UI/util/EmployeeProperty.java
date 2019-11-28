package UI.util;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import data.Employee;
import javafx.beans.property.*;

import java.time.LocalDate;

public class EmployeeProperty extends RecursiveTreeObject<EmployeeProperty> {
    private final SimpleIntegerProperty id;
    private final StringProperty name;
    private final StringProperty gender;
    private final SimpleIntegerProperty age;
    private final StringProperty phoneNumber;
    private final StringProperty residence;
    private final StringProperty education;
    private final SimpleIntegerProperty wage;
    private final ObjectProperty<LocalDate> entryDate;

    public EmployeeProperty(Employee employee) {
        id = new SimpleIntegerProperty(employee.getId());
        name = new SimpleStringProperty(employee.getName());
        gender = new SimpleStringProperty(employee.getGender());
        age = new SimpleIntegerProperty(employee.getAge());
        phoneNumber = new SimpleStringProperty(employee.getPhoneNumber());
        residence = new SimpleStringProperty(employee.getResidence());
        education = new SimpleStringProperty(employee.getEducation());
        wage = new SimpleIntegerProperty(employee.getWage());
        entryDate = new SimpleObjectProperty<>(employee.getEntryDate());
    }

    public Employee toEmployee() {
        return new Employee(id.get(), name.get(), gender.get(), age.get(), phoneNumber.get(), residence.get(), education.get(), wage.get(), entryDate.get());
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getResidence() {
        return residence.get();
    }

    public void setResidence(String residence) {
        this.residence.set(residence);
    }

    public StringProperty residenceProperty() {
        return residence;
    }

    public String getEducation() {
        return education.get();
    }

    public void setEducation(String education) {
        this.education.set(education);
    }

    public StringProperty educationProperty() {
        return education;
    }

    public int getWage() {
        return wage.get();
    }

    public void setWage(int wage) {
        this.wage.set(wage);
    }

    public SimpleIntegerProperty wageProperty() {
        return wage;
    }

    public LocalDate getEntryDate() {
        return entryDate.get();
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate.set(entryDate);
    }

    public ObjectProperty<LocalDate> entryDateProperty() {
        return entryDate;
    }

    @Override
    public String toString() {
        return "EmployeeProperty{" +
                "id=" + id +
                ", name=" + name +
                ", gender=" + gender +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                ", residence=" + residence +
                ", education=" + education +
                ", wage=" + wage +
                ", entryDate=" + entryDate +
                '}';
    }
}
