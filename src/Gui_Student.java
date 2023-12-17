import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gui_Student {
    static final Gui gui = new Gui();
    static final Gui_Method guiMethod = new Gui_Method();
    static final Student stu = new Student();

    public void Student_Add_Frame(Connection conn, String user, String permissions) {
        // 创建主窗口
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(135, 206, 235));

        // 创建标题标签
        JLabel titleLabel = new JLabel("添加学生信息", SwingConstants.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        // 创建输入框和标签
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.setBackground(new Color(135, 206, 235));

        JLabel noLabel = new JLabel("学号：");
        JLabel nameLabel = new JLabel("姓名：");
        JLabel facultiesLabel = new JLabel("院系：");
        JLabel optional_courseLabel = new JLabel("选课信息：");

        noLabel.setFont(new Font("宋体", Font.BOLD, 20));
        nameLabel.setFont(new Font("宋体", Font.BOLD, 20));
        facultiesLabel.setFont(new Font("宋体", Font.BOLD, 20));
        optional_courseLabel.setFont(new Font("宋体", Font.BOLD, 20));

        JTextField noTextField = new JTextField();
        JTextField nameTextField = new JTextField();

        //添加院系单选框
        JComboBox<String> facultiesComboBox = new JComboBox<>();
        ResultSet rs_faculties = stu.search_student(conn, "faculties", "null");
        try {
            while (rs_faculties.next()) {
                facultiesComboBox.addItem(rs_faculties.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //创建复选框
        String[] columnNames = {"课程名", "任课教师"};
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
        stu_view.setColumnIdentifiers(columnNames);
        table.getTableHeader().setReorderingAllowed(false);
        ResultSet rs = stu.search_student(conn, "course", "null");
        try {
            while (rs.next()) {
                stu_view.addRow(stu.get_student(rs, "add"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        // 组件添加到面板
        inputPanel.add(noLabel);
        inputPanel.add(noTextField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(facultiesLabel);
        inputPanel.add(facultiesComboBox);
        inputPanel.add(optional_courseLabel);

        JPanel panel_scrollPane = new JPanel(new GridLayout(1, 1));
        panel_scrollPane.add(scrollPane);

        // 创建添加按钮
        JButton addButton = new JButton("添加");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);

        // 添加按钮点击事件监听器
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();
                ArrayList<String> optional_course = new ArrayList<>();
                ArrayList<String> course_teacher = new ArrayList<>();
                for (int row : rows) {
                    optional_course.add(table.getValueAt(row, 0).toString());
                    course_teacher.add(table.getValueAt(row, 1).toString());
                }
                String stu_no = noTextField.getText();
                String stu_name = nameTextField.getText();
                String stu_faculties = (String) facultiesComboBox.getSelectedItem();
                String flag = stu.add_student(conn, stu_no, stu_name, stu_faculties, optional_course, course_teacher);
                if (flag.equals("normal")) {
                    JOptionPane.showMessageDialog(frame, "添加成功!");
                    frame.dispose();
                    gui.Main_Frame(conn, user, permissions);
                } else {
                    if (flag.equals("error"))
                        JOptionPane.showMessageDialog(frame, "添加失败!请检查数据");
                    if (flag.equals("empty"))
                        JOptionPane.showMessageDialog(frame, "添加失败!数据输入不完整");
                }
            }
        });

        // 将组件添加到面板中
        JPanel panel_center = new JPanel(new GridLayout(2, 1));
        panel_center.add(inputPanel);
        panel_center.add(panel_scrollPane);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(panel_center, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.SOUTH);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);

        gui.addWindowListener(conn, frame, user, permissions);
    }

    public void Student_Management_Frame(Connection conn, String user, String permissions) {
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        if (permissions.equals("normal_root"))
            frame.setSize(700, 700);
        else
            frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        if (permissions.equals("normal_root")) {
            // 创建表格模型
            String[] columnNames = {"学号", "姓名", "院系", "选课数量"};
            JTable table = new JTable() {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
            stu_view.setColumnIdentifiers(columnNames);
            table.getTableHeader().setReorderingAllowed(false);
            ResultSet rs = stu.view_student(conn);
            try {
                while (rs.next()) {
                    stu_view.addRow(stu.get_student(rs, "view"));
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
            //JButton viewButton = getViewButton(conn, table, frame);
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
                            stu.delete_student(conn, student_no);
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
                        JButton modButton = new JButton("修改");
                        stu_dialog.setSize(400, 300);
                        stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        stu_dialog.setLocationRelativeTo(null);
                        stu_dialog.setTitle("请输入信息");

                        JTextField stu_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
                        JTextField stu_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
                        JTextField stu_faculties = new JTextField((String) table.getValueAt(selectedRow, 2), 80);
                        stu_no.setEditable(false);

                        String[] columnNames = {"课程名", "任课教师"};
                        JTable table = new JTable() {
                            public boolean isCellEditable(int row, int column) {
                                return false;
                            }
                        };
                        DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
                        stu_view.setColumnIdentifiers(columnNames);
                        table.getTableHeader().setReorderingAllowed(false);
                        ResultSet rs = stu.search_student(conn, "optional_course_no", stu_no.getText());
                        try {
                            if (rs.next()) {
                                do {
                                    stu_view.addRow(stu.get_student(rs, "management"));
                                } while (rs.next());
                            } else
                                stu_view.addRow(stu.get_student(rs, "null"));
                        } catch (SQLException s) {
                            throw new RuntimeException(s);
                        }
                        JScrollPane optional_course_list = new JScrollPane();
                        optional_course_list.setViewportView(table);

                        JPanel panel = new JPanel(new GridLayout(4, 2));
                        panel.add(new JLabel("学号"));
                        panel.add(stu_no);
                        panel.add(new JLabel("姓名"));
                        panel.add(stu_name);
                        panel.add(new JLabel("院系"));
                        panel.add(stu_faculties);
                        panel.add(new JLabel("选课情况"));

                        modButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JDialog stu_dialog = new JDialog();
                                stu_dialog.setSize(400, 400);
                                stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                stu_dialog.setLocationRelativeTo(null);
                                stu_dialog.setTitle("选课信息修改");

                                String[] columnNames = {"课程名", "任课教师"};
                                JTable table = new JTable() {
                                    public boolean isCellEditable(int row, int column) {
                                        return false;
                                    }
                                };
                                DefaultTableModel stu_view_oc = (DefaultTableModel) table.getModel();
                                stu_view_oc.setColumnIdentifiers(columnNames);
                                table.getTableHeader().setReorderingAllowed(false);
                                ResultSet rs = stu.search_student(conn, "optional_course_no", stu_no.getText());
                                try {
                                    if (rs.next()) {
                                        do {
                                            stu_view_oc.addRow(stu.get_student(rs, "management"));
                                        } while (rs.next());
                                    } else
                                        stu_view_oc.addRow(stu.get_student(rs, "null"));
                                } catch (SQLException s) {
                                    throw new RuntimeException(s);
                                }

                                JButton addButton = new JButton("增加");
                                JButton deleteButton = new JButton("删除");
                                JButton confirmButton = new JButton("确认");

                                addButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JDialog add = new JDialog();
                                        add.setSize(400, 400);
                                        add.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                        add.setLocationRelativeTo(null);
                                        add.setTitle("请选择要添加的课程");

                                        JButton confirmButton = new JButton("确定");

                                        String[] columnNames = {"课程名", "任课教师"};
                                        JTable table = new JTable() {
                                            public boolean isCellEditable(int row, int column) {
                                                return false;
                                            }
                                        };
                                        DefaultTableModel stu_view_add = (DefaultTableModel) table.getModel();
                                        stu_view_add.setColumnIdentifiers(columnNames);
                                        table.getTableHeader().setReorderingAllowed(false);
                                        ResultSet rs = stu.search_student(conn, "optional_course_no_not", stu_no.getText());
                                        try {
                                            if (rs.next()) {
                                                do {
                                                    stu_view_add.addRow(stu.get_student(rs, "management"));
                                                } while (rs.next());
                                            } else
                                                stu_view_add.addRow(stu.get_student(rs, "null"));
                                        } catch (SQLException s) {
                                            throw new RuntimeException(s);
                                        }
                                        JScrollPane optional_course_list = new JScrollPane();
                                        optional_course_list.setViewportView(table);

                                        confirmButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                int[] rows = table.getSelectedRows();
                                                ArrayList<String> optional_course = new ArrayList<>();
                                                ArrayList<String> course_teacher = new ArrayList<>();
                                                for (int row : rows) {
                                                    optional_course.add(table.getValueAt(row, 0).toString());
                                                    course_teacher.add(table.getValueAt(row, 1).toString());
                                                }
                                                String flag = stu.modify_student(conn, stu_no.getText(), "null", "null", optional_course, course_teacher, "add");
                                                if (flag.equals("normal")) {
                                                    JOptionPane.showMessageDialog(add, "添加成功!");
                                                    stu_view.setRowCount(0);
                                                    stu_view_oc.setRowCount(0);
                                                    ResultSet rs = stu.search_student(conn, "optional_course_no", stu_no.getText());
                                                    try {
                                                        if (rs.next()) {
                                                            do {
                                                                stu_view.addRow(stu.get_student(rs, "management"));
                                                                stu_view_oc.addRow(stu.get_student(rs, "management"));
                                                            } while (rs.next());
                                                        } else {
                                                            stu_view.addRow(stu.get_student(rs, "null"));
                                                            stu_view_oc.addRow(stu.get_student(rs, "null"));
                                                        }
                                                    } catch (SQLException s) {
                                                        throw new RuntimeException(s);
                                                    }
                                                    add.dispose();
                                                }

                                            }
                                        });

                                        JPanel button = new JPanel();
                                        button.add(confirmButton);

                                        add.getContentPane().setLayout(new BorderLayout());
                                        add.add(optional_course_list, BorderLayout.CENTER);
                                        add.add(button, BorderLayout.SOUTH);

                                        add.setModal(true);
                                        add.setVisible(true);
                                    }
                                });

                                deleteButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int[] rows = table.getSelectedRows();
                                        ArrayList<String> optional_course = new ArrayList<>();
                                        for (int row : rows) {
                                            optional_course.add(table.getValueAt(row, 0).toString());
                                            stu_view.removeRow(row);
                                            stu_view_oc.removeRow(row);
                                        }
                                        String flag = stu.modify_student(conn, user, "null", "null", optional_course, null, "delete");
                                        if (flag.equals("normal"))
                                            JOptionPane.showMessageDialog(stu_dialog, "删除成功!");
                                    }
                                });

                                confirmButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        stu_dialog.dispose();
                                    }
                                });

                                JScrollPane optional_course_list = new JScrollPane();
                                optional_course_list.setViewportView(table);
                                JPanel button1 = new JPanel();
                                button1.add(addButton);
                                button1.add(deleteButton);

                                JPanel button2 = new JPanel();
                                button2.add(confirmButton);

                                JPanel all_button = new JPanel(new GridLayout(2, 1));
                                all_button.add(button1);
                                all_button.add(button2);

                                stu_dialog.getContentPane().setLayout(new BorderLayout());
                                stu_dialog.add(optional_course_list, BorderLayout.CENTER);
                                stu_dialog.add(all_button, BorderLayout.SOUTH);
                                stu_dialog.setModal(true);
                                stu_dialog.setVisible(true);
                            }
                        });
                        confirmButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String s_no = stu_no.getText();
                                String s_name = stu_name.getText();
                                String s_faculties = stu_faculties.getText();
                                ArrayList<String> optional_course = new ArrayList<>();
                                ArrayList<String> course_teacher = new ArrayList<>();
                                String flag = stu.modify_student(conn, s_no, s_name, s_faculties, optional_course, course_teacher, "null");
                                if (flag.equals("1")) {
                                    JOptionPane.showMessageDialog(frame, "修改失败,重复选课!");
                                } else {
                                    stu_view.setRowCount(0);
                                    stu_view.addRow(new Object[]{s_no, s_name, s_faculties});
                                    stu_dialog.dispose();
                                }
                            }
                        });
                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        panel.add(modButton);
                        buttonPanel.add(confirmButton);

                        JPanel center_panel = new JPanel(new GridLayout(2, 1));
                        center_panel.add(panel);
                        center_panel.add(optional_course_list);

                        stu_dialog.getContentPane().setLayout(new BorderLayout());
                        stu_dialog.getContentPane().add(center_panel, BorderLayout.CENTER);
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
                            ResultSet Student_info = stu.search_student(conn, "no", student_no);
                            try {
                                if (Student_info.next()) {
                                    stu_view.setRowCount(0);
                                    do {
                                        stu_view.addRow(stu.get_student(Student_info, "view"));
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
                        String student_name = JOptionPane.showInputDialog(frame, "请输入姓名", "输入", JOptionPane.QUESTION_MESSAGE);
                        if (student_name != null) {
                            ResultSet Student_info = stu.search_student(conn, "name", student_name);
                            try {
                                if (Student_info.next()) {
                                    stu_view.setRowCount(0);
                                    do {
                                        stu_view.addRow(stu.get_student(Student_info, "view"));
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
                        ArrayList<String> option_array = new ArrayList<>();
                        ResultSet rs = stu.search_student(conn, "faculties", "null");
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
                                    ResultSet Student_info = stu.search_student(conn, "faculties", student_faculties);
                                    stu_view.setRowCount(0);
                                    while (Student_info.next()) {
                                        stu_view.addRow(stu.get_student(Student_info, "view"));
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
                        ArrayList<String> option_array = new ArrayList<>();
                        ResultSet rs = stu.search_student(conn, "optional_course", "null");
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
                                    ResultSet Student_info = stu.search_student(conn, "optional_course", student_optional_course);
                                    stu_view.setRowCount(0);
                                    while (Student_info.next()) {
                                        stu_view.addRow(stu.get_student(Student_info, "view"));
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
                    ResultSet rs = stu.view_student(conn);
                    try {
                        while (rs.next()) {
                            stu_view.addRow(stu.get_student(rs, "view"));
                        }
                    } catch (SQLException s) {
                        throw new RuntimeException(s);
                    }
                }
            });
            JPanel buttonPanel = new JPanel();
            //buttonPanel.add(viewButton);
            buttonPanel.add(searchButton);
            buttonPanel.add(modButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(refreshButton);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
        } else {
            ResultSet rs = stu.search_student(conn, "no", user);
            try {
                rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBackground(new Color(135, 206, 235));

            JLabel titleLabel = new JLabel("个人信息", SwingConstants.CENTER);
            titleLabel.setFont(new Font("宋体", Font.BOLD, 30));
            titleLabel.setForeground(Color.WHITE);

            JPanel management = new JPanel(new GridLayout(4, 2));
            management.setBackground(new Color(135, 206, 235));

            JLabel noLabel = new JLabel("学号");
            JLabel nameLabel = new JLabel("姓名");
            JLabel facultiesLabel = new JLabel("院系");
            JLabel optional_courseLabel = new JLabel("选课信息：");

            JTextField noTextField = new JTextField(user);
            JTextField nameTextField;
            JTextField facultiesTextField;
            try {
                nameTextField = new JTextField(rs.getString(2));
                facultiesTextField = new JTextField(rs.getString(3));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            noTextField.setEditable(false);
            nameTextField.setEditable(false);
            facultiesTextField.setEditable(false);

            String[] columnNames = {"课程名", "任课教师"};
            JTable table = new JTable() {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
            stu_view.setColumnIdentifiers(columnNames);
            table.getTableHeader().setReorderingAllowed(false);
            rs = stu.search_student(conn, "optional_course_no", user);
            try {
                if (rs.next()) {
                    do {
                        stu_view.addRow(stu.get_student(rs, "management"));
                    } while (rs.next());
                } else
                    stu_view.addRow(stu.get_student(rs, "null"));
            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
            JScrollPane optional_course_list = new JScrollPane();
            optional_course_list.setViewportView(table);

            JButton optional_course_button = getOptionalCourseButton(conn, user, stu_view);

            management.add(noLabel);
            management.add(noTextField);
            management.add(nameLabel);
            management.add(nameTextField);
            management.add(facultiesLabel);
            management.add(facultiesTextField);
            management.add(optional_courseLabel);
            management.add(optional_course_button);

            JPanel center = new JPanel(new GridLayout(2, 1));
            JPanel optional_course_panel = new JPanel(new GridLayout(1, 1));
            optional_course_panel.add(optional_course_list);

            center.add(management);
            center.add(optional_course_panel);

            panel.add(titleLabel, BorderLayout.NORTH);
            panel.add(center, BorderLayout.CENTER);

            frame.add(panel, BorderLayout.CENTER);
        }
        frame.setVisible(true);

        gui.addWindowListener(conn, frame, user, permissions);
    }

    public void Student_View_Frame(Connection conn, String user, String permissions) {
        guiMethod.View_Frame(conn, Student.class, user, permissions);
    }

    public JButton getOptionalCourseButton(Connection conn, String user, DefaultTableModel stu_view) {
        JButton optional_course_button = new JButton("修改");

        optional_course_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog stu_dialog = new JDialog();
                stu_dialog.setSize(400, 400);
                stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                stu_dialog.setLocationRelativeTo(null);
                stu_dialog.setTitle("选课信息修改");

                String[] columnNames = {"课程名", "任课教师"};
                JTable table = new JTable() {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                DefaultTableModel stu_view_oc = (DefaultTableModel) table.getModel();
                stu_view_oc.setColumnIdentifiers(columnNames);
                table.getTableHeader().setReorderingAllowed(false);
                ResultSet rs = stu.search_student(conn, "optional_course_no", user);
                try {
                    if (rs.next()) {
                        do {
                            stu_view_oc.addRow(stu.get_student(rs, "management"));
                        } while (rs.next());
                    } else
                        stu_view_oc.addRow(stu.get_student(rs, "null"));
                } catch (SQLException s) {
                    throw new RuntimeException(s);
                }

                JButton addButton = new JButton("增加");
                JButton deleteButton = new JButton("删除");
                JButton confirmButton = new JButton("确认");

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JDialog add = new JDialog();
                        add.setSize(400, 400);
                        add.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        add.setLocationRelativeTo(null);
                        add.setTitle("请选择要添加的课程");

                        JButton confirmButton = new JButton("确定");

                        String[] columnNames = {"课程名", "任课教师"};
                        JTable table = new JTable() {
                            public boolean isCellEditable(int row, int column) {
                                return false;
                            }
                        };
                        DefaultTableModel stu_view_add = (DefaultTableModel) table.getModel();
                        stu_view_add.setColumnIdentifiers(columnNames);
                        table.getTableHeader().setReorderingAllowed(false);
                        ResultSet rs = stu.search_student(conn, "optional_course_no_not", user);
                        try {
                            if (rs.next()) {
                                do {
                                    stu_view_add.addRow(stu.get_student(rs, "management"));
                                } while (rs.next());
                            } else
                                stu_view_add.addRow(stu.get_student(rs, "null"));
                        } catch (SQLException s) {
                            throw new RuntimeException(s);
                        }
                        JScrollPane optional_course_list = new JScrollPane();
                        optional_course_list.setViewportView(table);

                        confirmButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int[] rows = table.getSelectedRows();
                                ArrayList<String> optional_course = new ArrayList<>();
                                ArrayList<String> course_teacher = new ArrayList<>();
                                for (int row : rows) {
                                    optional_course.add(table.getValueAt(row, 0).toString());
                                    course_teacher.add(table.getValueAt(row, 1).toString());
                                }
                                String flag = stu.modify_student(conn, user, "null", "null", optional_course, course_teacher, "add");
                                if (flag.equals("normal")) {
                                    JOptionPane.showMessageDialog(add, "添加成功!");
                                    stu_view.setRowCount(0);
                                    stu_view_oc.setRowCount(0);
                                    ResultSet rs = stu.search_student(conn, "optional_course_no", user);
                                    try {
                                        if (rs.next()) {
                                            do {
                                                stu_view.addRow(stu.get_student(rs, "management"));
                                                stu_view_oc.addRow(stu.get_student(rs, "management"));
                                            } while (rs.next());
                                        } else {
                                            stu_view.addRow(stu.get_student(rs, "null"));
                                            stu_view_oc.addRow(stu.get_student(rs, "null"));
                                        }
                                    } catch (SQLException s) {
                                        throw new RuntimeException(s);
                                    }
                                    add.dispose();
                                }

                            }
                        });

                        JPanel button = new JPanel();
                        button.add(confirmButton);

                        add.getContentPane().setLayout(new BorderLayout());
                        add.add(optional_course_list, BorderLayout.CENTER);
                        add.add(button, BorderLayout.SOUTH);

                        add.setModal(true);
                        add.setVisible(true);
                    }
                });

                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] rows = table.getSelectedRows();
                        ArrayList<String> optional_course = new ArrayList<>();
                        for (int row : rows) {
                            optional_course.add(table.getValueAt(row, 0).toString());
                            stu_view.removeRow(row);
                            stu_view_oc.removeRow(row);
                        }
                        String flag = stu.modify_student(conn, user, "null", "null", optional_course, null, "delete");
                        if (flag.equals("normal"))
                            JOptionPane.showMessageDialog(stu_dialog, "删除成功!");
                    }
                });

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        stu_dialog.dispose();
                    }
                });

                JScrollPane optional_course_list = new JScrollPane();
                optional_course_list.setViewportView(table);
                JPanel button1 = new JPanel();
                button1.add(addButton);
                button1.add(deleteButton);

                JPanel button2 = new JPanel();
                button2.add(confirmButton);

                JPanel all_button = new JPanel(new GridLayout(2, 1));
                all_button.add(button1);
                all_button.add(button2);

                stu_dialog.getContentPane().setLayout(new BorderLayout());
                stu_dialog.add(optional_course_list, BorderLayout.CENTER);
                stu_dialog.add(all_button, BorderLayout.SOUTH);
                stu_dialog.setModal(true);
                stu_dialog.setVisible(true);
            }
        });
        return optional_course_button;
    }
}
