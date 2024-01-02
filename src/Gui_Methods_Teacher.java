import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

public class Gui_Methods_Teacher extends Gui_Methods {
    public void Management_Search(JFrame frame, DefaultTableModel view, Connection conn) {
        Object[] o;
        Class<?> c = Teacher.class;
        String[] option = {"通过编号查询", "通过姓名查询", "通过性别查询", "通过学历查询", "通过职称查询", "通过毕业院系查询", "通过健康程度查询"};
        String message = (String) JOptionPane.showInputDialog(frame, "请选择查询方式", "提示", JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
        if (message != null && message.equals(option[0])) {
            String info = JOptionPane.showInputDialog(frame, "请输入编号", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "no", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[1])) {
            String info = JOptionPane.showInputDialog(frame, "请输入姓名", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "name", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[2])) {
            String[] option_sex = new String[]{"男", "女"};
            String info = (String) JOptionPane.showInputDialog(frame, "请选择性别", "提示", JOptionPane.QUESTION_MESSAGE, null, option_sex, option_sex[0]);
            o = new Object[]{c, "sex", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[3])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Teacher.class, "degree", option_array);
            if (flag.equals("normal")) {
                String[] option_optional_course = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择学历", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                o = new Object[]{c, "degree", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
        if (message != null && message.equals(option[4])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Teacher.class, "job", option_array);
            if (flag.equals("normal")) {
                String[] option_optional_course = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择职称", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                o = new Object[]{c, "job", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
        if (message != null && message.equals(option[5])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Teacher.class, "graduate_institutions", option_array);
            if (flag.equals("normal")) {
                String[] option_optional_course = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择毕业院校", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                o = new Object[]{c, "graduate_institutions", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
        if (message != null && message.equals(option[6])) {
            String[] option_sex = new String[]{"健康", "不健康"};
            String info = (String) JOptionPane.showInputDialog(frame, "请选择健康程度", "提示", JOptionPane.QUESTION_MESSAGE, null, option_sex, option_sex[0]);
            o = new Object[]{c, "health", info};
            super.Search(frame, view, conn, o);
        }
    }

    public void Management_Modify(JFrame frame, JDialog dialog, JTable table, JButton confirmButton, Connection conn, int selectedRow) {
        String[] sex = {"男", "女"};
        String[] health = {"健康", "不健康"};
        JTextField teacher_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
        JTextField teacher_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
        JComboBox<String> teacher_sex = new JComboBox<>(sex);
        JTextField teacher_age = new JTextField((String) table.getValueAt(selectedRow, 3), 80);
        JTextField teacher_degree = new JTextField((String) table.getValueAt(selectedRow, 4), 80);
        JTextField teacher_job = new JTextField((String) table.getValueAt(selectedRow, 5), 80);
        JTextField teacher_graduate_institutions = new JTextField((String) table.getValueAt(selectedRow, 6), 80);
        JComboBox<String> teacher_health = new JComboBox<>(health);

        teacher_no.setEditable(false);
        teacher_sex.setSelectedItem(table.getValueAt(selectedRow, 2));
        teacher_health.setSelectedItem(table.getValueAt(selectedRow, 7));

        JPanel panel = new JPanel(new GridLayout(9, 2));
        panel.add(new JLabel("编号"));
        panel.add(teacher_no);
        panel.add(new JLabel("姓名"));
        panel.add(teacher_name);
        panel.add(new JLabel("性别"));
        panel.add(teacher_sex);
        panel.add(new JLabel("年龄"));
        panel.add(teacher_age);
        panel.add(new JLabel("学历"));
        panel.add(teacher_degree);
        panel.add(new JLabel("职称"));
        panel.add(teacher_job);
        panel.add(new JLabel("毕业院校"));
        panel.add(teacher_graduate_institutions);
        panel.add(new JLabel("健康"));
        panel.add(teacher_health);

        confirmButton.addActionListener(e -> {
            Teacher teacher = new Teacher();
            Object[] o = new Object[]{teacher_no.getText(), teacher_name.getText(), teacher_sex.getSelectedItem(), teacher_age.getText(), teacher_degree.getText(), teacher_job.getText(), teacher_graduate_institutions.getText(), teacher_health.getSelectedItem()};
            String flag = teacher.modify(conn, o);
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