import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher {
    public String add(Connection conn, Object[] o) {
        for (Object object : o) {
            if (object.equals(""))
                return "empty";
        }
        try {
            String no_sql = "select teacher_no from teacher order by teacher_no";
            PreparedStatement ps_no = conn.prepareStatement(no_sql);
            ResultSet rs_no = ps_no.executeQuery();
            int no = 1;
            while (rs_no.next()) {
                if (no != rs_no.getInt(1))
                    break;
                no++;
            }
            String insert_sql = "insert into teacher (teacher_no,teacher_name,teacher_sex,teacher_age,teacher_degree,teacher_job,teacher_graduate_institutions,teacher_health) value(?,?,?,?,?,?,?,?)";
            PreparedStatement ps_insert = conn.prepareStatement(insert_sql);
            ps_insert.setInt(1, no);
            for (int i = 0; i < o.length; i++) {
                ps_insert.setObject(i + 2, o[i]);
            }
            ps_insert.executeUpdate();
            return "normal";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "error";
        }
    }

    public void delete(Connection conn, String teacher_no) {
        try {
            String sql = "delete from teacher where teacher_no=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, teacher_no);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] get(ResultSet rs, String type) {
        try {
            if (type.equals("view"))
                return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Object[0];
    }

    public String modify(Connection conn, Object[] o) {
        int teacher_no = Integer.parseInt(o[0].toString());
        String teacher_name = (String) o[1];
        String teacher_sex = (String) o[2];
        int teacher_age = Integer.parseInt(o[3].toString());
        String teacher_degree = (String) o[4];
        String teacher_job = (String) o[5];
        String teacher_graduate_institutions = (String) o[6];
        String teacher_health = (String) o[7];
        try {
            String sql = "update teacher set teacher_name=?,teacher_sex=?,teacher_age=?,teacher_degree=?,teacher_job=?,teacher_graduate_institutions=?,teacher_health=? where teacher_no=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, teacher_name);
            ps.setString(2, teacher_sex);
            ps.setInt(3, teacher_age);
            ps.setString(4, teacher_degree);
            ps.setString(5, teacher_job);
            ps.setString(6, teacher_graduate_institutions);
            ps.setString(7, teacher_health);
            ps.setInt(8, teacher_no);
            ps.executeUpdate();
            return "normal";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "error";
        }
    }

    public ResultSet search(Connection conn, String type, String teacher) {
        String sql = null;
        if (type.equals("teacher_name"))
            sql = "select teacher_name from teacher";
        if (type.equals("no"))
            sql = "select * from teacher where teacher_no=?";
        if (type.equals("name"))
            sql = "select * from teacher where teacher_name=?";
        if (type.equals("sex"))
            sql = "select * from teacher where teacher_sex=?";
        if (type.equals("degree")) {
            if (teacher.equals("null"))
                sql = "select distinct teacher_degree from teacher";
            else
                sql = "select * from teacher where teacher_degree=?";
        }
        if (type.equals("job")) {
            if (teacher.equals("null"))
                sql = "select distinct teacher_job from teacher";
            else
                sql = "select * from teacher where teacher_job=?";
        }
        if (type.equals("graduate_institutions")) {
            if (teacher.equals("null"))
                sql = "select distinct teacher_graduate_institutions from teacher";
            else
                sql = "select * from teacher where teacher_graduate_institutions=?";
        }
        if (type.equals("health"))
            sql = "select * from teacher where teacher_health=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if (!teacher.equals("null"))
                ps.setString(1, teacher);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ResultSet view(Connection conn) {
        try {
            String sql = "select * from teacher";
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
