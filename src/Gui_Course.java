import java.sql.Connection;

public class Gui_Course {
    static final Gui_Method guiMethod = new Gui_Method();

    public void Course_View_Frame(Connection conn, String user, String permissions) {
        guiMethod.View_Frame(conn, Course.class, user, permissions);
    }
}
