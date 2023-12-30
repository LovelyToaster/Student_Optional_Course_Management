import java.sql.Connection;

public class Gui_Teacher {
    static final Gui_Method guiMethod = new Gui_Method();

    public void Teacher_Add_Frame(Connection conn, String user, String permissions) {
        guiMethod.Add_Frame(conn, Teacher.class, user, permissions);
    }

    public void Teacher_View_Frame(Connection conn, String user, String permissions) {
        guiMethod.View_Frame(conn, Teacher.class, user, permissions);
    }
}
