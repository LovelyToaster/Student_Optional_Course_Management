import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

public class Gui_Methods_Course extends Gui_Methods {
    public void Management_Search(JFrame frame, DefaultTableModel view, Connection conn) {
        Object[] o;
        Class<?> c = Course.class;
        String[] option = {"通过编号查询", "通过名称查询", "通过任课教师查询"};
        String message = (String) JOptionPane.showInputDialog(frame, "请选择查询方式", "提示", JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
        if (message != null && message.equals(option[0])) {
            String info = JOptionPane.showInputDialog(frame, "请输入编号", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "no", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[1])) {
            String info = JOptionPane.showInputDialog(frame, "请输入名称", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "name", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[2])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Course.class, "teacher", option_array);
            if (flag.equals("normal")) {
                String[] option_optional_course = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择任课教师", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                o = new Object[]{c, "teacher", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
    }

    public void Management_Modify(JFrame frame, JDialog dialog, JTable table, JButton confirmButton, Connection conn, int selectedRow) {
        ArrayList<String> option_array = new ArrayList<>();
        super.Search_AddOption(conn, Course.class, "teacher", option_array);
        String[] option_course_teacher = option_array.toArray(new String[0]);

        JTextField course_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
        JTextField course_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
        JComboBox<String> course_teacher = new JComboBox<>(option_course_teacher);

        course_no.setEditable(false);
        course_teacher.setSelectedItem(table.getValueAt(selectedRow, 2));

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("编号"));
        panel.add(course_no);
        panel.add(new JLabel("姓名"));
        panel.add(course_name);
        panel.add(new JLabel("任课教师"));
        panel.add(course_teacher);

        confirmButton.addActionListener(e -> {
            Course course = new Course();
            Object[] o = new Object[]{course_no.getText(), course_name.getText(), course_teacher.getSelectedItem()};
            String flag = course.modify(conn, o);
            if (flag.equals("normal")) {
                dialog.dispose();
                JOptionPane.showMessageDialog(frame, "修改成功");
            } else
                JOptionPane.showMessageDialog(frame, "修改失败！");
        });

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(confirmButton, BorderLayout.SOUTH);

        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
