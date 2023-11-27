
import java.sql.*;

public class Student {
    String no;
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

    public ResultSet search_student(String type,String stu) { // 查找学生信息
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql = "select * from student_optional_course_management.student where ? = ?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,type);
            ps.setString(2,stu);
            return ps.executeQuery();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int modify_student(String student_no, Student stu) { // 修改学生信息
        return 0;
    }

    public void delete_student(String student_no) { // 删除学生信息
    }

    public ResultSet view_sql() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql = "select * from student_optional_course_management.student";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}