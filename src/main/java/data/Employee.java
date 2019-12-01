package main.java.data;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private static final String TABLE_NAME = "Employee";
    private static String[] educations = {"小学", "初中", "高中", "专科", "本科", "研究生"};
    private static DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
    private int id;
    private String name;
    private String gender;
    private int age;
    private String phoneNumber;
    private String residence;
    private String education;
    private int wage;
    private LocalDate entryDate;

    public Employee(int id, String name, String gender, int age, String phoneNumber, String residence, String education, int wage, LocalDate entryDate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.residence = residence;
        this.education = education;
        this.wage = wage;
        this.entryDate = entryDate;
    }

    /**
     * 删除指定职工
     *
     * @param id 职工编号
     * @return true 删除成功, false 没有该职工
     * @throws SQLException 数据库删除错误
     */
    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return 0 < preparedStatement.executeUpdate();
        }
    }

    /**
     * 从数据库中获取所有职工信息
     *
     * @return 职工列表
     */
    public static List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getInt("id");
                var name = resultSet.getString("name");
                var gender = resultSet.getString("gender");
                var age = resultSet.getInt("age");
                var phoneNumber = resultSet.getString("phoneNumber");
                var residence = resultSet.getString("residence");
                var education = resultSet.getString("education");
                var wage = resultSet.getInt("wage");
                var entryDate = resultSet.getDate("entryDate").toLocalDate();
                var employee = new Employee(id, name, gender, age, phoneNumber, residence, education, wage, entryDate);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static String[] getEducations() {
        return educations;
    }

    /**
     * 向数据库中插入职工
     *
     * @return true 插入成功, false 插入失败
     * @throws SQLException 数据库插入错误
     */
    public boolean insert() throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + "(id, name, gender, age, phoneNumber, residence, education, wage, entryDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(6, residence);
            preparedStatement.setString(7, education);
            preparedStatement.setInt(8, wage);
            preparedStatement.setDate(9, Date.valueOf(entryDate));
            return 0 < preparedStatement.executeUpdate();
        }
    }

    /**
     * 从数据库中删除职工信息
     *
     * @return true 删除成功, false 删除失败
     * @throws SQLException 数据库删除错误
     */
    public boolean delete() throws SQLException {
        return delete(getId());
    }

    /**
     * 保存职工信息到数据库
     *
     * @return true 保存成功, false 保存失败
     * @throws SQLException 数据库保存错误
     */
    public boolean save() throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, gender = ?, age = ?, phoneNumber = ?, residence = ?, education = ?, wage = ?, entryDate = ? WHERE id = ?";
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, gender);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, residence);
            preparedStatement.setString(6, education);
            preparedStatement.setInt(7, wage);
            preparedStatement.setDate(8, Date.valueOf(entryDate));
            preparedStatement.setInt(9, id);
            return 0 < preparedStatement.executeUpdate();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", residence='" + residence + '\'' +
                ", education='" + education + '\'' +
                ", wage=" + wage +
                ", entryDate=" + entryDate +
                '}';
    }

}
