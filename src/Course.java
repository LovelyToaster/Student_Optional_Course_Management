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

    public String add(Connection conn, Object[] o) {
        String course_name = (String) o[0];
        String course_teacher = (String) o[1];
        if (course_name.isEmpty() || course_teacher.isEmpty())
            return "empty";
        try {
            String no_sql = "select course_no from course order by course_no";
            PreparedStatement ps_no = conn.prepareStatement(no_sql);
            ResultSet rs_no = ps_no.executeQuery();
            int no = 1;
            while (rs_no.next()) {
                if (no != rs_no.getInt(1))
                    break;
                no++;
            }
            String insert_sql = "insert into course (course_no,course_name,course_teacher) value(?,?,?)";
            PreparedStatement ps_insert = conn.prepareStatement(insert_sql);
            ps_insert.setInt(1, no);
            ps_insert.setString(2, course_name);
            ps_insert.setString(3, course_teacher);
            ps_insert.executeUpdate();
            return "normal";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "error";
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
            if (type.equals("no"))
                sql = "select * from  course where course_no=?";
            if (type.equals("name"))
                sql = "select * from  course where course_name=?";
            if (type.equals("teacher")) {
                if (course.equals("null"))
                    sql = "select course_teacher from course";
                else
                    sql = "select * from course where course_teacher=?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (!course.equals("null"))
                ps.setString(1, course);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String modify(Connection conn, Object[] o) {
        int course_no = Integer.parseInt(o[0].toString());
        String course_name = (String) o[1];
        String course_teacher = (String) o[2];
        try {
            String sql = "update course set course_name=?,course_teacher=? where course_no=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, course_name);
            ps.setString(2, course_teacher);
            ps.setInt(3, course_no);
            ps.executeUpdate();
            return "normal";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
