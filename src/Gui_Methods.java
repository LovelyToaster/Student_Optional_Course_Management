import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gui_Methods {
    public void Search(JFrame frame, DefaultTableModel view, Connection conn, Object[] o) {
        Class<?> c = (Class<?>) o[0];
        String type = (String) o[1];
        String info = (String) o[2];
        if (info != null) {
            try {
                Method method_search = c.getMethod("search", Connection.class, String.class, String.class);
                Method method_get = c.getMethod("get", ResultSet.class, String.class);
                ResultSet rs_info = (ResultSet) method_search.invoke(c.getDeclaredConstructor().newInstance(), conn, type, info);
                if (rs_info.next()) {
                    view.setRowCount(0);
                    do {
                        view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs_info, "view"));
                    } while (rs_info.next());
                } else {
                    JOptionPane.showMessageDialog(frame, "没有查询到信息!");
                }
            } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException s) {
                throw new RuntimeException(s);
            }
        }
    }

    public String Search_AddOption(Connection conn, Class<?> c, String type, ArrayList<String> option_array) {
        String flag = "error";
        try {
            Method method_search = c.getMethod("search", Connection.class, String.class, String.class);
            ResultSet rs = (ResultSet) method_search.invoke(c.getDeclaredConstructor().newInstance(), conn, type, "null");
            while (rs.next()) {
                flag = "normal";
                option_array.add(rs.getString(1));
            }
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException s) {
            throw new RuntimeException(s);
        }
        return flag;
    }

    public String[] Management_columnNames(String type) {
        String[] columnNames = null;
        switch (type) {
            case "Student" -> columnNames = new String[]{"学号", "姓名", "院系", "选课数量"};
            case "Teacher" ->
                    columnNames = new String[]{"序号", "姓名", "性别", "年龄", "学历", "职称", "毕业院校", "健康"};
            case "Course" -> columnNames = new String[]{"序号", "课程名字", "任课教师", "学生数量"};
        }
        return columnNames;
    }

}
