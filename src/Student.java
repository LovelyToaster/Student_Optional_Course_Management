
import java.sql.*;
import java.util.List;

public class Student {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_optional_course_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String user = "root";
    static final String password = "zyn20030527";

    public String add_student(String stu_no, String stu_name, String stu_faculties, List stu) { // 添加学生
        if (stu_no.isEmpty() || stu_name.isEmpty()) {
            return "empty";
        }
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String insert = "insert student set no = ?, name = ?, faculties = ?";
            PreparedStatement ps_insert_1 = conn.prepareStatement(insert);
            ps_insert_1.setString(1, stu_no);
            ps_insert_1.setString(2, stu_name);
            ps_insert_1.setString(3, stu_faculties);
            ps_insert_1.executeUpdate();
            for (Object o : stu) {
                insert = "INSERT INTO optional_course (no, student_no, course_no) SELECT MAX(no) + 1, ?, (SELECT course_no FROM course WHERE course_name = ? ) FROM optional_course";
                PreparedStatement ps_insert_2 = conn.prepareStatement(insert);
                ps_insert_2.setString(1, stu_no);
                ps_insert_2.setObject(2, o);
                ps_insert_2.executeUpdate();
            }
            count();
            return "normal";
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return "error";
        }
    }

    public void count() { //统计学生选课数量
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String sql_no = "select no from student";
            PreparedStatement ps_no = conn.prepareStatement(sql_no);
            ResultSet rs_no = ps_no.executeQuery();
            while (rs_no.next()) {
                String sql = "update student set optional_course_quantity=(select count(distinct no) from optional_course where student_no = ? ) where no = ? ";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, rs_no.getString(1));
                ps.setString(2, rs_no.getString(1));
                ps.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
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
                    sql = "select distinct faculties from faculties ";
                else
                    sql = "select * from student where faculties = ?";
            }
            if (type.equals("optional_course")) {
                if (stu.equals("null"))
                    sql = "select distinct course_name from course,optional_course where course.course_no=optional_course.course_no";
                else
                    sql = "select * from student where no = (select student_no from optional_course where course_no = (select course_no from course where course_name = ?))";
            }
            if (type.equals("optional_course_sno")) {
                sql = "select course_name from course where course_no in (select course_no from optional_course where student_no = ?)";
            }
            if (type.equals("course")) {
                sql = "select course_name from course";
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
            String optional_course = "select optional_course_quantity from student where no = ? and optional_course_quantity = ?";
            PreparedStatement optional_course_ps = conn.prepareStatement(optional_course);
            optional_course_ps.setString(1, s_no);
            optional_course_ps.setString(2, s_optional_course);
            ResultSet rs = optional_course_ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString(1).equals(s_optional_course))
                    return 1;
            }

            String sql = "update student set name = ? , faculties = ? , optional_course_quantity = ? where no = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s_name);
            ps.setString(2, s_faculties);
            ps.setString(3, s_optional_course);
            ps.setString(4, s_no);
            ps.executeUpdate();
            return 0;
        } catch (ClassNotFoundException | SQLException e) {
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
            count();
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