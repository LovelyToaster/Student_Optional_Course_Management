
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Set;


public class Login {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_optional_course_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String user = "root";
    static final String password = "zyn20030527";
    HashMap<String, String> login_map = new HashMap<>(); // 保存用户账号密码

    public String password_verify(String frame_name, String frame_password) { // 账号密码验证
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql = "select userpassword,permissions from login where username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, frame_name);
            ResultSet rs = ps.executeQuery();
            if (frame_name.isEmpty() || frame_password.isEmpty())
                return "empty";
            if (rs.next()) {
                if (rs.getString(1).equals(frame_password) && rs.getString(2).equals("1"))
                    return "normal_root";
                else if (rs.getString(1).equals(frame_password) && rs.getString(2).equals("0"))
                    return "normal_user";
                else
                    return "error";
            }
            return "error";
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int user_register(String frame_new_name, String frame_new_password) { // 注册
        try {
            if (frame_new_name.isEmpty() || frame_new_password.isEmpty())
                return -2;
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql = "insert login set username=?,userpassword=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, frame_new_name);
            ps.setString(2, frame_new_password);
            ps.executeUpdate();
            return 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int password_mod(String user, String password, String restart_password) {

        return 0;
    }


}
