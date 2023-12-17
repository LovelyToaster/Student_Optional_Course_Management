import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
    public void count(Connection conn) {
        try {
            String sql_no = "select course_no from course";
            PreparedStatement ps_no = conn.prepareStatement(sql_no);
            ResultSet rs_no = ps_no.executeQuery(sql_no);
            while (rs_no.next()) {
                String sql = "update course set course_students=(select count(distinct student_no) from optional_course where course_no=?) where course_no=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, rs_no.getString(1));
                ps.setString(2, rs_no.getString(1));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Object[] get_course(ResultSet rs, String type) {
        try {
            if (type.equals("view"))
                return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)};
            if (type.equals("management"))
                return new Object[]{rs.getString(1), rs.getString(2)};
            return new Object[0];
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] get(ResultSet rs, String type) {
        try {
            if (type.equals("view"))
                return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)};
            if (type.equals("management"))
                return new Object[]{rs.getString(1), rs.getString(2)};
            return new Object[0];
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet view(Connection conn) {
        try {
            count(conn);
            String sql = "select * from course";
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ResultSet search(Connection conn, String type, String course) {
        try {
            String sql = null;
            if (type.equals("optional_course_no")) {
                sql = "select student_no,name from optional_course,student where student_no=student.no and course_no=?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, course);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
