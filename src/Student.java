
import java.sql.*;
import java.util.ArrayList;

public class Student {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_optional_course_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String user = "root";
    static final String password = "zyn20030527";

    public String add_student(String stu_no, String stu_name, String stu_faculties, ArrayList<String> optional_course, ArrayList<String> course_teacher) { // 添加学生
        if (stu_no.isEmpty() || stu_name.isEmpty()) {
            return "empty";
        }
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String insert_stu = "insert student set no = ?, name = ?, faculties = ?";
            PreparedStatement ps_insert_stu = conn.prepareStatement(insert_stu);
            ps_insert_stu.setString(1, stu_no);
            ps_insert_stu.setString(2, stu_name);
            ps_insert_stu.setString(3, stu_faculties);
            ps_insert_stu.executeUpdate();
            for (int i = 0; i < optional_course.size(); i++) {
                String insert_optional_course = "INSERT INTO optional_course (no, student_no, course_no) SELECT MAX(no) + 1, ?, (SELECT course_no FROM course WHERE course_name = ? and course_teacher = ?) FROM optional_course";
                PreparedStatement ps_insert_optional_course = conn.prepareStatement(insert_optional_course);
                ps_insert_optional_course.setString(1, stu_no);
                ps_insert_optional_course.setString(2, optional_course.get(i));
                ps_insert_optional_course.setString(3, course_teacher.get(i));
                ps_insert_optional_course.executeUpdate();
            }
            String insert_login = "insert login set username=?,userpassword='123456'";
            PreparedStatement ps_insert_login = conn.prepareStatement(insert_login);
            ps_insert_login.setString(1, stu_no);
            ps_insert_login.executeUpdate();
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

    public Object[] get_student(ResultSet rs, String type) { // 获取学生信息
        try {
            if (type.equals("view"))
                return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)};
            if (type.equals("add") || type.equals("management"))
                return new Object[]{rs.getString(1), rs.getString(2)};
            if (type.equals("null"))
                return new Object[]{"无选课记录"};
            return new Object[0];
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
                sql = "select course_name,course_teacher from course where course_no in (select course_no from optional_course where student_no = ?)";
            }
            if (type.equals("optional_course_sno_not"))
                sql = "select course_name,course_teacher from course where course_no not in (select course_no from optional_course where student_no = ?)";
            if (type.equals("course")) {
                sql = "select course_name,course_teacher from course";
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

    public String modify_student(String s_no, String s_name, String s_faculties, ArrayList<String> optional_course, ArrayList<String> course_teacher, String type) { // 修改学生信息
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            if (type.equals("add")) {
                for (int i = 0; i < optional_course.size(); i++) {
                    String insert_optional_course = "INSERT INTO optional_course (no, student_no, course_no) SELECT MAX(no) + 1, ?, (SELECT course_no FROM course WHERE course_name = ? and course_teacher = ?) FROM optional_course";
                    PreparedStatement ps_insert_optional_course = conn.prepareStatement(insert_optional_course);
                    ps_insert_optional_course.setString(1, s_no);
                    ps_insert_optional_course.setString(2, optional_course.get(i));
                    ps_insert_optional_course.setString(3, course_teacher.get(i));
                    ps_insert_optional_course.executeUpdate();
                }
                return "normal";
            } else if (type.equals("delete")) {
                for (String s : optional_course) {
                    String insert_optional_course = "delete from optional_course where student_no=? and course_no=(select course_no from course where course_name=?)";
                    PreparedStatement ps_insert_optional_course = conn.prepareStatement(insert_optional_course);
                    ps_insert_optional_course.setString(1, s_no);
                    ps_insert_optional_course.setString(2, s);
                    ps_insert_optional_course.executeUpdate();
                }
                return "normal";
            } else {
                String sql = "update student set name = ? , faculties = ? where no = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, s_name);
                ps.setString(2, s_faculties);
                ps.setString(3, s_no);
                ps.executeUpdate();
                return "normal";
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return "error";
        }
    }

    public void delete_student(String student_no) { // 删除学生信息
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, user, password);
            String stu_sql = "delete from student where no = ? ";
            String login_sql = "delete from login where username = ?";
            PreparedStatement ps_stu = conn.prepareStatement(stu_sql);
            PreparedStatement ps_login = conn.prepareStatement(login_sql);
            ps_stu.setString(1, student_no);
            ps_login.setString(1, student_no);
            ps_stu.executeUpdate();
            ps_login.executeUpdate();
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