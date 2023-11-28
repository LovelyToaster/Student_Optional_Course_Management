
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
                sql = "select * from student_optional_course_management.student where no = ?";
            if (type.equals("name"))
                sql = "select * from student_optional_course_management.student where name like ?";
            if (type.equals("faculties")) {
                if (stu.equals("null"))
                    sql = "select distinct faculties from student_optional_course_management.student ";
                else
                    sql = "select * from student_optional_course_management.student where faculties = ?";
            }
            if (type.equals("optional_course")) {
                if (stu.equals("null"))
                    sql = "select distinct optional_course from student_optional_course_management.student ";
                else
                    sql = "select * from student_optional_course_management.student where optional_course = ?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (type.equals("name"))
                ps.setString(1, "%" + stu + "%");
            else if(!stu.equals("null"))
                ps.setString(1, stu);
            return ps.executeQuery();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int modify_student(String student_no, Student stu) { // 修改学生信息
        return 0;
    } //修改学生信息

    public void delete_student(String student_no) { // 删除学生信息
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql = "delete from student_optional_course_management.student where no = ? ";
            PreparedStatement ps= conn.prepareStatement(sql);
            ps.setString(1,student_no);
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