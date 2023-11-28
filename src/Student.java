
import java.sql.*;

public class Student {
    String name;
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_optional_course_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String user = "root";
    static final String password = "zyn20030527";

    public int add_student(Student stu) { // 添加学生

        return 0;
    }

    public Object[] get_student(ResultSet rs) { // 获取学生信息
        try {
            return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet search_student(String type, String stu) { // 查找学生信息
        try {
            String sql = null;
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            if (type.equals("no"))
                sql = "select * from student where no = ?";
            if (type.equals("name"))
                sql = "select * from student where name like ?";
            if (type.equals("faculties")) {
                if (stu.equals("null"))
                    sql = "select distinct faculties from student ";
                else
                    sql = "select * from student where faculties = ?";
            }
            if (type.equals("optional_course")) {
                if (stu.equals("null"))
                    sql = "select distinct optional_course from student ";
                else
                    sql = "select * from student where optional_course = ?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (type.equals("name"))
                ps.setString(1, "%" + stu + "%");
            else if (!stu.equals("null"))
                ps.setString(1, stu);
            return ps.executeQuery();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int modify_student(String s_no, String s_name, String s_faculties, String s_optional_course) { // 修改学生信息
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String optional_course = "select optional_course from student where no = ? and optional_course = ?";
            PreparedStatement optional_course_ps = conn.prepareStatement(optional_course);
            optional_course_ps.setString(1, s_no);
            optional_course_ps.setString(2, s_optional_course);
            ResultSet rs = optional_course_ps.executeQuery();
            while (rs.next()){
                if (!rs.getString(1).equals(s_optional_course))
                    return 1;
            }

            String sql = "update student set name = ? , faculties = ? , optional_course = ? where no = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s_name);
            ps.setString(2, s_faculties);
            ps.setString(3, s_optional_course);
            ps.setString(4, s_no);
            ps.executeUpdate();
            return 0;
        } catch (ClassNotFoundException |
                 SQLException e) {
            throw new RuntimeException(e);
        }

    } //修改学生信息

    public void delete_student(String student_no) { // 删除学生信息
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql = "delete from student where no = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, student_no);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet view_student() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql = "select * from student_optional_course_management.student";
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}