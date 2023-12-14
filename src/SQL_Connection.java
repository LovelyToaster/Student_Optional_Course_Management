import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL_Connection {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://113.31.125.148:3306/student_optional_course_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String user = "root";
    static final String password = "zyn20030527";

    public Connection sql_connection() {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
