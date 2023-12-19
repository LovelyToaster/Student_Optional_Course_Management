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

    public ResultSet search(Connection conn, String type, String stu) {
        try {
            String sql = null;
            if (type.equals("grade")) {
                sql = "select student_no,student.name,optional_course.course_no,course.course_name,grade from optional_course,student,course where student_no=student.no and optional_course.course_no=course.course_no and student.no=?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, stu);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet view(Connection conn) {
        try {
            String sql = "select student_no,student.name,optional_course.course_no,course.course_name,grade from optional_course,student,course where student_no=student.no and optional_course.course_no=course.course_no";
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
