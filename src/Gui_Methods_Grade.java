import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

public class Gui_Methods_Grade extends Gui_Methods {
    public void Management_Search(JFrame frame, DefaultTableModel view, Connection conn) {
        Object[] o;
        Class<?> c = Grade.class;
        String[] option = {"通过学号查询", "通过课程号查询", "通过学生姓名", "通过课程名查询"};
        String message = (String) JOptionPane.showInputDialog(frame, "请选择查询方式", "提示", JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
        if (message != null && message.equals(option[0])) {
            String info = JOptionPane.showInputDialog(frame, "通过学号查询", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "stu_no", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[1])) {
            String info = JOptionPane.showInputDialog(frame, "通过课程号查询", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "course_no", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[2])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Student.class, "name", option_array);
            if (flag.equals("normal")) {
                String[] option_optional_course = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择学生姓名", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                o = new Object[]{Grade.class, "stu_name", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
        if (message != null && message.equals(option[3])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Course.class, "name", option_array);
            if (flag.equals("normal")) {
                String[] option_optional_course = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择课程名", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                o = new Object[]{Grade.class, "course_name", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
    }

    public void Management_Modify(JFrame frame, JDialog dialog, JTable table, JButton confirmButton, Connection conn, int selectedRow) {
        JTextField student_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
        JTextField student_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
        JTextField course_no = new JTextField((String) table.getValueAt(selectedRow, 2), 80);
        JTextField course_name = new JTextField((String) table.getValueAt(selectedRow, 3), 80);
        JTextField gradeTextField = new JTextField((String) table.getValueAt(selectedRow, 4), 80);

        student_no.setEditable(false);
        student_name.setEditable(false);
        course_no.setEditable(false);
        course_name.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("学号"));
        panel.add(student_no);
        panel.add(new JLabel("姓名"));
        panel.add(student_name);
        panel.add(new JLabel("课程号"));
        panel.add(course_no);
        panel.add(new JLabel("课程名"));
        panel.add(course_name);
        panel.add(new JLabel("成绩"));
        panel.add(gradeTextField);

        confirmButton.addActionListener(e -> {
            Grade grade = new Grade();
            Object[] o = new Object[]{student_no.getText(), course_no.getText(), gradeTextField.getText()};
            String flag = grade.modify(conn, o);
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
