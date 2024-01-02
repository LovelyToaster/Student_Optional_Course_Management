import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Grade {

    public Object[] get(ResultSet rs, String type) {
        try {
            if (type.equals("view"))
                return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
            return new Object[0];
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet search(Connection conn, String type, String info) {
        try {
            String sql = null;
            if (type.equals("grade") || type.equals("stu_no")) {
                sql = "select student_no,student.name,optional_course.course_no,course.course_name,grade from optional_course,student,course where student_no=student.no and optional_course.course_no=course.course_no and student.no=?";
            }
            if (type.equals("course_no")) {
                sql = "select student_no,student.name,optional_course.course_no,course.course_name,grade from optional_course,student,course where student_no=student.no and optional_course.course_no=course.course_no and course.course_no=?";
            }
            if (type.equals("stu_name")) {
                sql = "select student_no,student.name,optional_course.course_no,course.course_name,grade from optional_course,student,course where student_no=student.no and optional_course.course_no=course.course_no and student.name=?";
            }
            if (type.equals("course_name")) {
                sql = "select student_no,student.name,optional_course.course_no,course.course_name,grade from optional_course,student,course where student_no=student.no and optional_course.course_no=course.course_no and course.course_name=?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (!info.equals("null"))
                ps.setString(1, info);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet view(Connection conn) {
        try {
            String sql = "select student_no,student.name,optional_course.course_no,course.course_name,grade from optional_course,student,course where student_no=student.no and optional_course.course_no=course.course_no order by student_no";
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String modify(Connection conn, Object[] o) {
        String stu_no = (String) o[0];
        int course_no = Integer.parseInt(o[1].toString());
        int grade = Integer.parseInt(o[2].toString());
        try {
            String sql = "update optional_course set grade=? where student_no=? and course_no=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, grade);
            ps.setString(2, stu_no);
            ps.setInt(3, course_no);
            ps.executeUpdate();
            return "normal";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
