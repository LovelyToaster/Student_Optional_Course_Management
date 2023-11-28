import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gui_Student {
    public void Student_Management_Frame(Student stu, String user, Login login) {
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 创建表格模型
        String[] columnNames = {"学号", "姓名", "院系", "选课"};
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
        stu_view.setColumnIdentifiers(columnNames);
        table.getTableHeader().setReorderingAllowed(false);
        ResultSet rs = stu.view_student();
        try {
            while (rs.next()) {
                stu_view.addRow(stu.get_student(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 创建表格
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        JButton deleteButton = new JButton("删除");
        JButton modButton = new JButton("修改");
        JButton searchButton = new JButton("查询");
        JButton refreshButton = new JButton("刷新");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // 获取选中的行索引
                    String student_no = (String) table.getValueAt(selectedRow, 0);
                    // 显示删除成功的消息框
                    int i = JOptionPane.showConfirmDialog(frame, "你确定要删除该信息吗？", "注意", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (i == JOptionPane.YES_OPTION) {
                        // 执行删除学生宿舍操作的代码
                        stu.delete_student(student_no);
                        // 从表格模型中删除选中行
                        stu_view.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(frame, "删除成功!");
                    }
                } else {
                    // 如果没有选中任何行，显示提示消息框
                    JOptionPane.showMessageDialog(frame, "请选择要删除的学生!");
                }
            }
        });
        modButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    JDialog stu_dialog = new JDialog();
                    JButton confirmButton = new JButton("确认");
                    stu_dialog.setSize(400, 300);
                    stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    stu_dialog.setLocationRelativeTo(null);
                    stu_dialog.setTitle("请输入信息");

                    JTextField stu_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
                    stu_no.setEditable(false);
                    JTextField stu_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
                    JTextField stu_institute = new JTextField((String) table.getValueAt(selectedRow, 3), 80);
                    JTextField stu_dormitory = new JTextField((String) table.getValueAt(selectedRow, 4), 80);
                    JTextField stu_dormitory_number = new JTextField((String) table.getValueAt(selectedRow, 5), 80);
                    JTextField stu_phone = new JTextField((String) table.getValueAt(selectedRow, 6), 80);

                    String[] sex = {"男", "女"};
                    JComboBox sexComboBox = new JComboBox(sex);

                    JPanel panel = new JPanel(new GridLayout(7, 2));
                    panel.add(new JLabel("学号"));
                    panel.add(stu_no);
                    panel.add(new JLabel("姓名"));
                    panel.add(stu_name);
                    panel.add(new JLabel("性别"));
                    panel.add(sexComboBox);
                    panel.add(new JLabel("院部"));
                    panel.add(stu_institute);
                    panel.add(new JLabel("宿舍楼"));
                    panel.add(stu_dormitory);
                    panel.add(new JLabel("宿舍号"));
                    panel.add(stu_dormitory_number);
                    panel.add(new JLabel("电话"));
                    panel.add(stu_phone);

                    confirmButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Student s = new Student();
//                            s.no = stu_no.getText();
                            s.name = stu_name.getText();
//                            int i = stu.modify_student(s.no, s);
//                            if (i == 4) {
//                                JOptionPane.showMessageDialog(frame, "修改失败,电话发生重复！");
//                            } else {
////                                Student Student_info = stu.search_student(s.no, s, 0);
//                                stu_view.setRowCount(0);
////                                stu_view.addRow(stu.get_student(Student_info));
//                                stu_dialog.dispose();
//                            }
                        }
                    });
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    buttonPanel.add(confirmButton);

                    stu_dialog.getContentPane().setLayout(new BorderLayout());
                    stu_dialog.getContentPane().add(panel, BorderLayout.CENTER);
                    stu_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                    stu_dialog.setModal(true);
                    stu_dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "请选择要修改的学生!");
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] option = {"通过学号查询", "通过姓名查询", "通过院系查询", "通过选课情况查询"};
                String info = (String) JOptionPane.showInputDialog(frame, "请选择查询方式", "提示", JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
                if (info != null && info.equals(option[0])) {
                    String student_no = JOptionPane.showInputDialog(frame, "请输入学号", "输入", JOptionPane.QUESTION_MESSAGE);
                    if (student_no != null) {
                        ResultSet Student_info = stu.search_student("no", student_no);
                        try {
                            if (Student_info.next()) {
                                stu_view.setRowCount(0);
                                do {
                                    stu_view.addRow(stu.get_student(Student_info));
                                } while (Student_info.next());
                            } else {
                                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
                            }
                        } catch (SQLException s) {
                            throw new RuntimeException(s);
                        }
                    }
                }
                if (info != null && info.equals(option[1])) {
                    int i = 0;
                    String student_name = JOptionPane.showInputDialog(frame, "请输入姓名", "输入", JOptionPane.QUESTION_MESSAGE);
                    if (student_name != null) {
                        ResultSet Student_info = stu.search_student("name", student_name);
                        try {
                            if (Student_info.next()) {
                                stu_view.setRowCount(0);
                                do {
                                    stu_view.addRow(stu.get_student(Student_info));
                                } while (Student_info.next());
                            } else {
                                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
                            }
                        } catch (SQLException s) {
                            throw new RuntimeException(s);
                        }
                    }
                }
                if (info != null && info.equals(option[2])) {
                    int i = 0;
                    int j = 0;
                    ArrayList<String> option_array = new ArrayList<>();
                    ResultSet rs = stu.search_student("faculties", "null");
                    try {
                        while (rs.next()) {
                            i = 1;
                            option_array.add(rs.getString(1));
                        }
                    } catch (SQLException s) {
                        throw new RuntimeException(s);
                    }
                    if (i == 1) {
                        String[] option_faculties = option_array.toArray(new String[0]);
                        String student_faculties = (String) JOptionPane.showInputDialog(frame, "请选择院系", "提示", JOptionPane.QUESTION_MESSAGE, null, option_faculties, option_faculties[0]);
                        if (student_faculties != null) {
                            try {
                                ResultSet Student_info = stu.search_student("faculties", student_faculties);
                                stu_view.setRowCount(0);
                                while (Student_info.next()) {
                                    stu_view.addRow(stu.get_student(Student_info));
                                }
                            } catch (SQLException s) {
                                throw new RuntimeException(s);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "没有查询到信息!");
                    }
                }
                if (info != null && info.equals(option[3])) {
                    int i = 0;
                    int j = 0;
                    ArrayList<String> option_array = new ArrayList<>();
                    ResultSet rs = stu.search_student("optional_course", "null");
                    try {
                        while (rs.next()) {
                            i = 1;
                            option_array.add(rs.getString(1));
                        }
                    } catch (SQLException s) {
                        throw new RuntimeException(s);
                    }
                    if (i == 1) {
                        String[] option_optional_course = option_array.toArray(new String[0]);
                        String student_optional_course = (String) JOptionPane.showInputDialog(frame, "请选择课程", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                        if (student_optional_course != null) {
                            try {
                                ResultSet Student_info = stu.search_student("optional_course", student_optional_course);
                                stu_view.setRowCount(0);
                                while (Student_info.next()) {
                                    stu_view.addRow(stu.get_student(Student_info));
                                }
                            } catch (SQLException s) {
                                throw new RuntimeException(s);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "没有查询到信息!");
                    }
                }
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_view.setRowCount(0);
                ResultSet rs = stu.view_student();
                try {
                    while (rs.next()) {
                        stu_view.addRow(stu.get_student(rs));
                    }
                } catch (SQLException s) {
                    throw new RuntimeException(s);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(modButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                Gui gui = new Gui();
                gui.Main_Frame(stu, user, login);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }
}
