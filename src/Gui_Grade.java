import java.sql.Connection;

public class Gui_Grade {
    static final Gui_Method guiMethod = new Gui_Method();

    public void Grade_View_Frame(Connection conn, String user, String permissions) {
        guiMethod.View_Frame(conn, Grade.class, user, permissions);
    }
}
