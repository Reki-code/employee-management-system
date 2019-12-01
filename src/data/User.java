package data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    private int id;
    private String username = "";
    private String password = "";
    private String phoneNumber = "";
    private static DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
    private static User currentUser;
    private static final String TABLE_NAME = "_User";

    /**
     * 返回当前使用系统的用户
     *
     * @return 用户
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * 保存用户信息到数据库中
     *
     * @param column   数据库中对应的列名
     * @param newValue 要更新的值
     * @param id       编号
     * @return true 更新成功, false 更新失败
     */
    public static boolean updateUserDetail(String column, String newValue, int id) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + column + " = ? WHERE id = ?";
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newValue);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 用户名(username)是否被使用
     * @param name 用户名
     * @return true name已经被使用, false name没有被使用
     */
    public static boolean findUsername(String name) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从数据库中获取所有用户信息
     * @return 用户列表
     */
    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 检查密码
     *
     * @param password 密码
     * @return true 密码匹配, false 密码不匹配
     */
    public boolean checkPassword(String password) {
        return getPassword().equals(password);
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 使用用户名(username)和密码(password)登录
     * @return SUCCESS 登录成功, ERROR 登录失败
     */
    public LoginStatus signIn() {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";
        LoginStatus loginStatus = LoginStatus.ERROR;
        if (!username.isEmpty() || !password.isEmpty()) {
            try (var conn = databaseHandler.getConnection()){
                var preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, password);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    loginStatus = LoginStatus.SUCCESS;
                    setId(resultSet.getInt("id"));
                    setPhoneNumber(resultSet.getString("phoneNumber"));
                    currentUser = this;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                loginStatus = LoginStatus.ERROR;
            }
        }

        return loginStatus;
    }

    /**
     * 保存当前用户信息到数据库
     * @return true 保存成功, false 保存失败
     */
    public boolean save() {
        String sql = "UPDATE " + TABLE_NAME + " SET username = ?, password = ?, phoneNumber = ? WHERE id = ?";
        try (var conn = databaseHandler.getConnection()){
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setInt(4, id);
            var rowCount = preparedStatement.executeUpdate();
            if (rowCount > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 注册新用户, 数据库返回编号(id)
     *
     * @return true 注册成功, false 注册失败
     */
    public boolean signUp() {
        String sql = "INSERT INTO " + TABLE_NAME + "(username, password, phoneNumber) VALUES (?, ?, ?)";
        try (var conn = databaseHandler.getConnection()) {
            var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phoneNumber);
            var count = preparedStatement.executeUpdate();
            if (count > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    generatedKeys.next();
                    setId(generatedKeys.getInt("id"));
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete() {
        return delete(getId());
    }

    @Override
    public String toString() {
        return super.toString() + String.format("{id:%d, username:%s, password:%s, phoneNumber:%s}", getId(), getUsername(), getPassword(), getPhoneNumber());
    }

    void logOut() {
        currentUser = null;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var user = new User();
        System.out.println("用户名:");
        var name = scanner.nextLine();
        user.setUsername(name);
        System.out.println("密码");
        var password = scanner.nextLine();
        user.setPassword(password);
        System.out.println("1注册 2登录 3查找 4修改");
        var c = Integer.parseInt(scanner.nextLine());
        if (c == 1) {
            user.signUp();
            System.out.println(user.getId());
        } else if (c == 2) {
            System.out.println(user.signIn());
            System.out.println(user.getId());
        } else if (c == 3) {
            System.out.println(User.findUsername(user.username));
        } else if (c == 4) {
            user.signIn();
            System.out.println("新用户名");
            name = scanner.nextLine();
            System.out.println("新密码");
            password = scanner.nextLine();
            System.out.println("新电话");
            var phoneNumber = scanner.nextLine();
            user.setUsername(name);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);
            System.out.println(user.save());
            System.out.println(user.getId());
        }
        System.out.println(user);
    }

    public enum LoginStatus {
        SUCCESS,
        ERROR
    }
}
