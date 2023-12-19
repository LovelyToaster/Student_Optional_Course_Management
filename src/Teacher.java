import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher {
    public Object[] get(ResultSet rs, String type) {
        try {
            if (type.equals("view"))
                return new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Object[0];
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
