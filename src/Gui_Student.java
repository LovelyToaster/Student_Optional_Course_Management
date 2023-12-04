import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Gui_Student {
    static final Gui gui = new Gui();
    static final Student stu = new Student();

    public void Student_Add_Frame(String user, String permissions) {
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
        ResultSet rs_faculties = stu.search_student("faculties", "null");
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
        ResultSet rs = stu.search_student("course", "null");
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
                String flag = stu.add_student(stu_no, stu_name, stu_faculties, optional_course, course_teacher);
                if (flag.equals("normal")) {
                    JOptionPane.showMessageDialog(frame, "添加成功!");
                    frame.dispose();
                    gui.Main_Frame(user, permissions);
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
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                gui.Main_Frame(user, permissions);
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

    public void Student_Management_Frame(String user, String permissions) {
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
            ResultSet rs = stu.view_student();
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
            JButton viewButton = new JButton("详细");
            viewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        JDialog stu_dialog = new JDialog();
                        JButton confirmButton = new JButton("确认");
                        stu_dialog.setSize(400, 300);
                        stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        stu_dialog.setLocationRelativeTo(null);
                        stu_dialog.setTitle("详细信息");

                        JTextField stu_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
                        JTextField stu_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
                        JTextField stu_faculties = new JTextField((String) table.getValueAt(selectedRow, 2), 80);

                        stu_no.setEditable(false);
                        stu_name.setEditable(false);
                        stu_faculties.setEditable(false);

                        String[] columnNames = {"课程名", "任课教师"};
                        JTable table = new JTable() {
                            public boolean isCellEditable(int row, int column) {
                                return false;
                            }
                        };
                        DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
                        stu_view.setColumnIdentifiers(columnNames);
                        table.getTableHeader().setReorderingAllowed(false);
                        ResultSet rs = stu.search_student("optional_course_sno", stu_no.getText());
                        try {
                            while (rs.next()) {
                                stu_view.addRow(stu.get_student(rs, "management"));
                            }
                        } catch (SQLException s) {
                            throw new RuntimeException(s);
                        }
                        JScrollPane optional_course_ScrollPane = new JScrollPane();
                        optional_course_ScrollPane.setViewportView(table);

                        JPanel panel = new JPanel(new GridLayout(4, 2));
                        JPanel optional_course_ScrollPane_panel = new JPanel(new GridLayout(1, 1));
                        JPanel CENTER_panel = new JPanel(new GridLayout(2, 1));
                        panel.add(new JLabel("学号"));
                        panel.add(stu_no);
                        panel.add(new JLabel("姓名"));
                        panel.add(stu_name);
                        panel.add(new JLabel("院系"));
                        panel.add(stu_faculties);
                        panel.add(new JLabel("选课情况"));
                        optional_course_ScrollPane_panel.add(optional_course_ScrollPane);

                        confirmButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                stu_dialog.dispose();
                            }
                        });
                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        buttonPanel.add(confirmButton);

                        CENTER_panel.add(panel);
                        CENTER_panel.add(optional_course_ScrollPane_panel);
                        stu_dialog.getContentPane().setLayout(new BorderLayout());
                        stu_dialog.getContentPane().add(CENTER_panel, BorderLayout.CENTER);
                        stu_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                        stu_dialog.setModal(true);
                        stu_dialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(frame, "请选择要查看的学生!");
                    }

                }
            });
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
                        JTextField stu_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
                        JTextField stu_faculties = new JTextField((String) table.getValueAt(selectedRow, 2), 80);
                        JTextField stu_optional_course = new JTextField((String) table.getValueAt(selectedRow, 3), 80);
                        stu_no.setEditable(false);

                        JPanel panel = new JPanel(new GridLayout(7, 2));
                        panel.add(new JLabel("学号"));
                        panel.add(stu_no);
                        panel.add(new JLabel("姓名"));
                        panel.add(stu_name);
                        panel.add(new JLabel("院系"));
                        panel.add(stu_faculties);
                        panel.add(new JLabel("选课情况"));
                        panel.add(stu_optional_course);

                        confirmButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String s_no = stu_no.getText();
                                String s_name = stu_name.getText();
                                String s_faculties = stu_faculties.getText();
                                String s_optional_course = stu_optional_course.getText();
                                int flag = stu.modify_student(s_no, s_name, s_faculties, s_optional_course);
                                if (flag == 1) {
                                    JOptionPane.showMessageDialog(frame, "修改失败,重复选课!");
                                } else {
                                    stu_view.setRowCount(0);
                                    stu_view.addRow(new Object[]{s_no, s_name, s_faculties, s_optional_course});
                                    stu_dialog.dispose();
                                }
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
                        int i = 0;
                        String student_name = JOptionPane.showInputDialog(frame, "请输入姓名", "输入", JOptionPane.QUESTION_MESSAGE);
                        if (student_name != null) {
                            ResultSet Student_info = stu.search_student("name", student_name);
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
                    ResultSet rs = stu.view_student();
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
            buttonPanel.add(viewButton);
            buttonPanel.add(searchButton);
            buttonPanel.add(modButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(refreshButton);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
        } else {
            ResultSet rs = stu.search_student("no", user);
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
            JTextField nameTextField = null;
            JTextField facultiesTextField = null;
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
            rs = stu.search_student("optional_course_sno", user);
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

            JButton optional_course_button = new JButton("修改");

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

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                gui.Main_Frame(user, permissions);
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

    public void Student_View_Frame(String user, String permissions) {
        // 创建主窗口
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        //创建显示区域
        String[] columnNames = {"学号", "姓名", "院系", "选课数量"};
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
                stu_view.addRow(stu.get_student(rs, "view"));
            }
        } catch (SQLException e) {
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
        JButton viewButton = new JButton("详细");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    JDialog stu_dialog = new JDialog();
                    JButton confirmButton = new JButton("确认");
                    stu_dialog.setSize(400, 300);
                    stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    stu_dialog.setLocationRelativeTo(null);
                    stu_dialog.setTitle("详细信息");

                    JTextField stu_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
                    JTextField stu_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
                    JTextField stu_faculties = new JTextField((String) table.getValueAt(selectedRow, 2), 80);

                    stu_no.setEditable(false);
                    stu_name.setEditable(false);
                    stu_faculties.setEditable(false);

                    String[] columnNames = {"课程名", "任课教师"};
                    JTable table = new JTable() {
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
                    stu_view.setColumnIdentifiers(columnNames);
                    table.getTableHeader().setReorderingAllowed(false);
                    ResultSet rs = stu.search_student("optional_course_sno", stu_no.getText());
                    try {
                        while (rs.next()) {
                            stu_view.addRow(stu.get_student(rs, "management"));
                        }
                    } catch (SQLException s) {
                        throw new RuntimeException(s);
                    }
                    JScrollPane optional_course_ScrollPane = new JScrollPane();
                    optional_course_ScrollPane.setViewportView(table);

                    JPanel panel = new JPanel(new GridLayout(4, 2));
                    JPanel optional_course_ScrollPane_panel = new JPanel(new GridLayout(1, 1));
                    JPanel CENTER_panel = new JPanel(new GridLayout(2, 1));
                    panel.add(new JLabel("学号"));
                    panel.add(stu_no);
                    panel.add(new JLabel("姓名"));
                    panel.add(stu_name);
                    panel.add(new JLabel("院系"));
                    panel.add(stu_faculties);
                    panel.add(new JLabel("选课情况"));
                    optional_course_ScrollPane_panel.add(optional_course_ScrollPane);

                    confirmButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            stu_dialog.dispose();
                        }
                    });
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    buttonPanel.add(confirmButton);

                    CENTER_panel.add(panel);
                    CENTER_panel.add(optional_course_ScrollPane_panel);
                    stu_dialog.getContentPane().setLayout(new BorderLayout());
                    stu_dialog.getContentPane().add(CENTER_panel, BorderLayout.CENTER);
                    stu_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                    stu_dialog.setModal(true);
                    stu_dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "请选择要查看的学生!");
                }

            }
        });

        // 将组件添加到面板中
        panel.add(titleLabel, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                gui.Main_Frame(user, permissions);
            }

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
