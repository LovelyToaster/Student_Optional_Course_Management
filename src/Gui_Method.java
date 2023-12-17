import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Gui_Method {
    static final Gui gui = new Gui();

    public void View_Frame(Connection conn, Class<?> c, String user, String permissions) {
        // 创建主窗口
        JFrame frame = new JFrame("学生选课信息管理系统");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        //创建显示区域
        String[] columnNames = null;
        if (c.getName().equals("Course"))
            columnNames = new String[]{"课程号", "课程名", "任课教师", "选课人数"};
        if (c.getName().equals("Student"))
            columnNames = new String[]{"学号", "姓名", "院系", "选课数量"};
        if (c.getName().equals("Grade"))
            columnNames = new String[]{"学号", "姓名", "课程号", "课程名", "成绩"};
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableModel view = (DefaultTableModel) table.getModel();
        view.setColumnIdentifiers(columnNames);
        table.getTableHeader().setReorderingAllowed(false);

        try {
            Method method_view;
            if (!c.getName().equals("Grade"))
                method_view = c.getMethod("view", Connection.class);
            else {
                method_view = c.getMethod("search", Connection.class, String.class);
            }
            Method method_get = c.getMethod("get", ResultSet.class, String.class);
            ResultSet rs = (ResultSet) method_view.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{conn, "grade"});
            while (rs.next()) {
                view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{rs, "view"}));
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(135, 206, 235));

        // 创建标题标签
        JLabel titleLabel = new JLabel("所有的学生宿舍信息", SwingConstants.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        //创建按钮
        if (!c.getName().equals("Grade")) {
            JButton viewButton = getViewButton(conn, c, table, frame);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(viewButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        // 将组件添加到面板中
        panel.add(titleLabel, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);

        gui.addWindowListener(conn, frame, user, permissions);
    }

    public JButton getViewButton(Connection conn, Class<?> c, JTable table, JFrame frame) {
        JButton viewButton = new JButton("详细");
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                JDialog dialog = new JDialog();
                JButton confirmButton = new JButton("确认");
                dialog.setSize(400, 300);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setLocationRelativeTo(null);
                dialog.setTitle("详细信息");

                JTextField no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
                JTextField name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
                JTextField teacher = new JTextField((String) table.getValueAt(selectedRow, 2), 80);

                no.setEditable(false);
                name.setEditable(false);
                teacher.setEditable(false);

                String[] columnNames = null;
                if (c.getName().equals("Course"))
                    columnNames = new String[]{"学号", "学生姓名"};
                if (c.getName().equals("Student"))
                    columnNames = new String[]{"课程名", "任课教师"};
                JTable table1 = new JTable() {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                DefaultTableModel course_view = (DefaultTableModel) table1.getModel();
                course_view.setColumnIdentifiers(columnNames);
                table1.getTableHeader().setReorderingAllowed(false);

                try {
                    Method method_search = c.getMethod("search", Connection.class, String.class, String.class);
                    Method method_get = c.getMethod("get", ResultSet.class, String.class);
                    ResultSet rs = (ResultSet) method_search.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{conn, "optional_course_no", no.getText()});
                    while (rs.next()) {
                        course_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{rs, "management"}));
                    }
                } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException s) {
                    throw new RuntimeException(s);
                }
                JScrollPane optional_course_ScrollPane = new JScrollPane();
                optional_course_ScrollPane.setViewportView(table1);

                JPanel panel = new JPanel(new GridLayout(4, 2));
                JPanel optional_course_ScrollPane_panel = new JPanel(new GridLayout(1, 1));
                JPanel CENTER_panel = new JPanel(new GridLayout(2, 1));

                String[] label_name = null;
                if (c.getName().equals("Student"))
                    label_name = new String[]{"学号", "姓名", "院系", "选课情况"};
                if (c.getName().equals("Course"))
                    label_name = new String[]{"课程号", "课程名", "任课教师", "选课情况"};
                if (label_name != null) {
                    panel.add(new JLabel(label_name[0]));
                    panel.add(no);
                    panel.add(new JLabel(label_name[1]));
                    panel.add(name);
                    panel.add(new JLabel(label_name[2]));
                    panel.add(teacher);
                    panel.add(new JLabel(label_name[3]));
                }
                optional_course_ScrollPane_panel.add(optional_course_ScrollPane);

                confirmButton.addActionListener(e1 -> dialog.dispose());
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(confirmButton);

                CENTER_panel.add(panel);
                CENTER_panel.add(optional_course_ScrollPane_panel);
                dialog.getContentPane().setLayout(new BorderLayout());
                dialog.getContentPane().add(CENTER_panel, BorderLayout.CENTER);
                dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                dialog.setModal(true);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "请选择要查看的对象!");
            }

        });
        return viewButton;
    }
}
