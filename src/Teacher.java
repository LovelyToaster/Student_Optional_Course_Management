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

    public Object[] get(ResultSet rs, String type) {
        try {
            if (type.equals("view"))
                return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Object[0];
    }

    public ResultSet search(Connection conn, String type) {
        String sql = null;
        if (type.equals("teacher_name"))
            sql = "select teacher_name from teacher";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
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
