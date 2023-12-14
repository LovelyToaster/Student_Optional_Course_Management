
import java.sql.*;


public class Login {
    public String password_verify(Connection conn, String frame_name, String frame_password) { // 账号密码验证
        try {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int user_register(Connection conn, String frame_new_name, String frame_new_password) { // 注册
        try {
            if (frame_new_name.isEmpty() || frame_new_password.isEmpty())
                return -2;
            String sql = "insert login set username=?,userpassword=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, frame_new_name);
            ps.setString(2, frame_new_password);
            ps.executeUpdate();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public String password_mod(Connection conn, String stu_user, String stu_password, String stu_restart_password) {
        if (stu_password.isEmpty() || stu_restart_password.isEmpty())
            return "empty";
        try {
            String confirm_sql = "select userpassword from login where username=?";
            PreparedStatement confirm_ps = conn.prepareStatement(confirm_sql);
            confirm_ps.setString(1, stu_user);
            ResultSet confirm_rs = confirm_ps.executeQuery();
            confirm_rs.next();
            if (confirm_rs.getString(1).equals(stu_password)) {
                String change_sql = "update login set userpassword=? where username=?";
                PreparedStatement change_ps = conn.prepareStatement(change_sql);
                change_ps.setString(1, stu_restart_password);
                change_ps.setString(2, stu_user);
                change_ps.executeUpdate();
                return "normal";
            } else
                return "password_error";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
