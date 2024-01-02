import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Gui_Method {
    static final Gui gui = new Gui();
    static final Gui_Methods gui_methods = new Gui_Methods();
    static final Gui_Methods_Student gui_methods_student = new Gui_Methods_Student();
    static final Gui_Methods_Teacher gui_methods_teacher = new Gui_Methods_Teacher();
    static final Gui_Methods_Course gui_methods_course = new Gui_Methods_Course();
    static final Gui_Methods_Grade gui_methods_grade = new Gui_Methods_Grade();

    public void Add_Frame(Connection conn, Class<?> c, String user, String permissions) {
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
        JLabel titleLabel = new JLabel();
        if (c.getName().equals("Student"))
            titleLabel = new JLabel("添加学生信息", SwingConstants.CENTER);
        if (c.getName().equals("Course"))
            titleLabel = new JLabel("添加课程信息", SwingConstants.CENTER);
        if (c.getName().equals("Teacher"))
            titleLabel = new JLabel("添加教师信息", SwingConstants.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        // 创建输入框和标签
        JPanel inputPanel = new JPanel();
        if (c.getName().equals("Student"))
            inputPanel.setLayout(new GridLayout(4, 2));
        if (c.getName().equals("Course"))
            inputPanel.setLayout(new GridLayout(7, 2));
        if (c.getName().equals("Teacher"))
            inputPanel.setLayout(new GridLayout(9, 2));
        inputPanel.setBackground(new Color(135, 206, 235));

        //字体
        Font font = new Font("宋体", Font.BOLD, 20);

        //学生添加
        JLabel noLabel;
        JLabel nameLabel;
        JLabel facultiesLabel;
        JLabel optional_courseLabel;
        JTextField noTextField;
        JTextField nameTextField;
        JComboBox<String> facultiesComboBox;
        JTable table;
        JPanel panel_scrollPane = null;
        if (c.getName().equals("Student")) {
            noLabel = new JLabel("学号：");
            nameLabel = new JLabel("姓名：");
            facultiesLabel = new JLabel("院系：");
            optional_courseLabel = new JLabel("选课信息：");

            noLabel.setFont(font);
            nameLabel.setFont(font);
            facultiesLabel.setFont(font);
            optional_courseLabel.setFont(font);

            noTextField = new JTextField();
            nameTextField = new JTextField();

            Student stu = new Student();
            //添加院系单选框
            facultiesComboBox = new JComboBox<>();
            ResultSet rs_faculties = stu.search(conn, "faculties", "null");
            try {
                while (rs_faculties.next()) {
                    facultiesComboBox.addItem(rs_faculties.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //创建复选框
            String[] columnNames = {"课程名", "任课教师"};
            table = new JTable() {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
            stu_view.setColumnIdentifiers(columnNames);
            table.getTableHeader().setReorderingAllowed(false);
            ResultSet rs = stu.search(conn, "course", "null");
            try {
                while (rs.next()) {
                    stu_view.addRow(stu.get(rs, "add"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(table);

            panel_scrollPane = new JPanel(new GridLayout(1, 1));
            panel_scrollPane.add(scrollPane);

            // 组件添加到面板
            inputPanel.add(noLabel);
            inputPanel.add(noTextField);
            inputPanel.add(nameLabel);
            inputPanel.add(nameTextField);
            inputPanel.add(facultiesLabel);
            inputPanel.add(facultiesComboBox);
            inputPanel.add(optional_courseLabel);
        } else {
            nameTextField = null;
            noTextField = null;
            facultiesComboBox = null;
            table = null;
        }

        //课程添加
        JLabel course_nameLabel;
        JLabel course_TeacherLabel;
        JTextField course_nameTextField;
        JComboBox<String> teacherComboBox;
        if (c.getName().equals("Course")) {
            course_nameLabel = new JLabel("课程名字");
            course_TeacherLabel = new JLabel("任课教师");
            course_nameLabel.setFont(font);
            course_TeacherLabel.setFont(font);

            course_nameTextField = new JTextField();

            Teacher teacher = new Teacher();
            //添加院系单选框
            teacherComboBox = new JComboBox<>();
            ResultSet rs_teacher = teacher.search(conn, "teacher_name", "null");
            try {
                while (rs_teacher.next()) {
                    teacherComboBox.addItem(rs_teacher.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            inputPanel.add(course_nameLabel);
            inputPanel.add(course_nameTextField);
            inputPanel.add(course_TeacherLabel);
            inputPanel.add(teacherComboBox);
        } else {
            teacherComboBox = null;
            course_nameTextField = null;
        }

        //教师添加
        JLabel teacher_nameLabel;
        JLabel teacher_sexLabel;
        JLabel teacher_ageLabel;
        JLabel teacher_degreeLabel;
        JLabel teacher_jobLabel;
        JLabel teacher_graduate_institutionsLabel;
        JLabel teacher_healthLabel;
        JTextField teacher_nameTextField;
        JTextField teacher_ageTextField;
        JTextField teacher_degreeTextField;
        JTextField teacher_jobTextField;
        JTextField teacher_graduate_institutionsTextField;
        JComboBox<String> teacher_sexComboBox;
        JComboBox<String> teacher_healthComboBox;
        if (c.getName().equals("Teacher")) {
            teacher_nameLabel = new JLabel("教师姓名");
            teacher_sexLabel = new JLabel("教师性别");
            teacher_ageLabel = new JLabel("教师年龄");
            teacher_degreeLabel = new JLabel("教师学历");
            teacher_jobLabel = new JLabel("教师职位");
            teacher_graduate_institutionsLabel = new JLabel("教师毕业院校");
            teacher_healthLabel = new JLabel("教师健康状况");

            teacher_nameTextField = new JTextField();
            teacher_ageTextField = new JTextField();
            teacher_degreeTextField = new JTextField();
            teacher_jobTextField = new JTextField();
            teacher_graduate_institutionsTextField = new JTextField();

            String[] sex = {"男", "女"};
            String[] health = {"健康", "不健康"};
            teacher_sexComboBox = new JComboBox<>(sex);
            teacher_healthComboBox = new JComboBox<>(health);

            teacher_nameLabel.setFont(font);
            teacher_sexLabel.setFont(font);
            teacher_ageLabel.setFont(font);
            teacher_degreeLabel.setFont(font);
            teacher_jobLabel.setFont(font);
            teacher_graduate_institutionsLabel.setFont(font);
            teacher_healthLabel.setFont(font);

            inputPanel.add(teacher_nameLabel);
            inputPanel.add(teacher_nameTextField);
            inputPanel.add(teacher_sexLabel);
            inputPanel.add(teacher_sexComboBox);
            inputPanel.add(teacher_ageLabel);
            inputPanel.add(teacher_ageTextField);
            inputPanel.add(teacher_degreeLabel);
            inputPanel.add(teacher_degreeTextField);
            inputPanel.add(teacher_jobLabel);
            inputPanel.add(teacher_jobTextField);
            inputPanel.add(teacher_graduate_institutionsLabel);
            inputPanel.add(teacher_graduate_institutionsTextField);
            inputPanel.add(teacher_healthLabel);
            inputPanel.add(teacher_healthComboBox);
        } else {
            teacher_healthComboBox = null;
            teacher_jobTextField = null;
            teacher_graduate_institutionsTextField = null;
            teacher_degreeTextField = null;
            teacher_ageTextField = null;
            teacher_sexComboBox = null;
            teacher_nameTextField = null;
        }

        // 创建添加按钮
        JButton addButton = new JButton("添加");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);

        // 添加按钮点击事件监听器
        addButton.addActionListener(e ->
        {
            Object[] o = null;
            if (c.getName().equals("Student")) {
                int[] rows = Objects.requireNonNull(table).getSelectedRows();
                ArrayList<String> optional_course = new ArrayList<>();
                ArrayList<String> course_teacher = new ArrayList<>();
                for (int row : rows) {
                    optional_course.add(table.getValueAt(row, 0).toString());
                    course_teacher.add(table.getValueAt(row, 1).toString());
                }
                String no = noTextField.getText();
                String name = nameTextField.getText();
                String faculties = (String) facultiesComboBox.getSelectedItem();
                o = new Object[]{no, name, faculties, optional_course, course_teacher};
            }
            if (c.getName().equals("Course")) {
                String course_name = Objects.requireNonNull(course_nameTextField).getText();
                String course_teacher = (String) teacherComboBox.getSelectedItem();
                o = new Object[]{course_name, course_teacher};
            }
            if (c.getName().equals("Teacher")) {
                String teacher_name = Objects.requireNonNull(teacher_nameTextField).getText();
                String teacher_sex = (String) teacher_sexComboBox.getSelectedItem();
                String teacher_age = teacher_ageTextField.getText();
                String teacher_degree = teacher_degreeTextField.getText();
                String teacher_job = teacher_jobTextField.getText();
                String teacher_graduate_institutions = teacher_graduate_institutionsTextField.getText();
                String teacher_health = (String) teacher_healthComboBox.getSelectedItem();
                o = new Object[]{teacher_name, teacher_sex, teacher_age, teacher_degree, teacher_job, teacher_graduate_institutions, teacher_health};
            }
            if (o != null) {
                try {
                    Method method_add = c.getMethod("add", Connection.class, Object[].class);
                    String flag = (String) method_add.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{conn, o});
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
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 将组件添加到面板中
        JPanel panel_center = null;
        if (c.getName().equals("Student")) {
            panel_center = new JPanel(new GridLayout(2, 1));
            panel_center.add(inputPanel);
            panel_center.add(panel_scrollPane);
        }
        panel.add(titleLabel, BorderLayout.NORTH);
        if (c.getName().equals("Student"))
            panel.add(panel_center, BorderLayout.CENTER);
        else
            panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.SOUTH);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);

        addWindowListener(conn, frame, user, permissions);
    }

    public void Management_Frame(Connection conn, Class<?> c, String user, String permissions) {
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        if (permissions.equals("normal_root"))
            frame.setSize(700, 700);
        else
            frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        if (permissions.equals("normal_root")) {
            // 创建表格模型
            String[] columnNames = gui_methods.Management_columnNames(c.getName());
            JTable table = new JTable() {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel view = (DefaultTableModel) table.getModel();
            view.setColumnIdentifiers(columnNames);
            table.getTableHeader().setReorderingAllowed(false);
            try {
                Method method_view = c.getMethod("view", Connection.class);
                ResultSet rs = (ResultSet) method_view.invoke(c.getDeclaredConstructor().newInstance(), conn);
                while (rs.next()) {
                    Method method_get = c.getMethod("get", ResultSet.class, String.class);
                    view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "view"));
                }
            } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
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
                        String no = (String) table.getValueAt(selectedRow, 0);
                        // 显示删除成功的消息框
                        int i = JOptionPane.showConfirmDialog(frame, "你确定要删除该信息吗？", "注意", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (i == JOptionPane.YES_OPTION) {
                            // 执行删除学生宿舍操作的代码
                            try {
                                Method method_delete = c.getMethod("delete", Connection.class, String.class);
                                method_delete.invoke(c.getDeclaredConstructor().newInstance(), conn, no);
                            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException |
                                     InstantiationException ex) {
                                throw new RuntimeException(ex);
                            }
                            // 从表格模型中删除选中行
                            view.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(frame, "删除成功!");
                        }
                    } else {
                        // 如果没有选中任何行，显示提示消息框
                        JOptionPane.showMessageDialog(frame, "请选择要删除的信息!");
                    }
                }
            });
            modButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        JDialog dialog = new JDialog();
                        JButton confirmButton = new JButton("确认");
                        dialog.setSize(400, 300);
                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        dialog.setLocationRelativeTo(null);
                        dialog.setTitle("请输入信息");
                        switch (c.getName()) {
                            case "Student" ->
                                    gui_methods_student.Management_Modify(frame, dialog, table, confirmButton, conn, selectedRow);
                            case "Teacher" ->
                                    gui_methods_teacher.Management_Modify(frame, dialog, table, confirmButton, conn, selectedRow);
                            case "Course" ->
                                    gui_methods_course.Management_Modify(frame, dialog, table, confirmButton, conn, selectedRow);
                            case "Grade" ->
                                    gui_methods_grade.Management_Modify(frame, dialog, table, confirmButton, conn, selectedRow);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "请选择要修改的学生!");
                    }
                }
            });
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (c.getName()) {
                        case "Student" -> gui_methods_student.Management_Search(frame, view, conn);
                        case "Teacher" -> gui_methods_teacher.Management_Search(frame, view, conn);
                        case "Course" -> gui_methods_course.Management_Search(frame, view, conn);
                        case "Grade" -> gui_methods_grade.Management_Search(frame, view, conn);
                    }
                }
            });
            refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    view.setRowCount(0);
                    try {
                        Method method_view = c.getMethod("view", Connection.class);
                        Method method_get = c.getMethod("get", ResultSet.class, String.class);
                        ResultSet rs = (ResultSet) method_view.invoke(c.getDeclaredConstructor().newInstance(), conn);
                        while (rs.next()) {
                            view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "view"));
                        }
                    } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException s) {
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
//            ResultSet rs = stu.search_student(conn, "no", user);
//            try {
//                rs.next();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }

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
//            try {
//                nameTextField = new JTextField(rs.getString(2));
//                facultiesTextField = new JTextField(rs.getString(3));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
            noTextField.setEditable(false);
//            nameTextField.setEditable(false);
//            facultiesTextField.setEditable(false);

            String[] columnNames = {"课程名", "任课教师"};
            JTable table = new JTable() {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
            stu_view.setColumnIdentifiers(columnNames);
            table.getTableHeader().setReorderingAllowed(false);
//            rs = stu.search_student(conn, "optional_course_no", user);
//            try {
//                if (rs.next()) {
//                    do {
//                        stu_view.addRow(stu.get_student(rs, "management"));
//                    } while (rs.next());
//                } else
//                    stu_view.addRow(stu.get_student(rs, "null"));
//            } catch (SQLException s) {
//                throw new RuntimeException(s);
//            }
            JScrollPane optional_course_list = new JScrollPane();
            optional_course_list.setViewportView(table);

//            JButton optional_course_button = getOptionalCourseButton(conn, user, stu_view);

            management.add(noLabel);
            management.add(noTextField);
            management.add(nameLabel);
//            management.add(nameTextField);
            management.add(facultiesLabel);
//            management.add(facultiesTextField);
            management.add(optional_courseLabel);
//            management.add(optional_course_button);

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

        addWindowListener(conn, frame, user, permissions);
    }

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
        if (c.getName().equals("Teacher"))
            columnNames = new String[]{"序号", "姓名", "性别", "年龄", "学历", "职称", "毕业院校", "健康状况"};
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
            Method method_get = c.getMethod("get", ResultSet.class, String.class);
            ResultSet rs = null;
            if (c.getName().equals("Grade") && permissions.equals("normal_root")) {
                method_view = c.getMethod("view", Connection.class);
                rs = (ResultSet) method_view.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{conn});
            } else if (c.getName().equals("Grade") && permissions.equals("normal_user")) {
                method_view = c.getMethod("search", Connection.class, String.class, String.class);
                rs = (ResultSet) method_view.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{conn, "grade", user});
            } else {
                method_view = c.getMethod("view", Connection.class);
                rs = (ResultSet) method_view.invoke(c.getDeclaredConstructor().newInstance(), new Object[]{conn});
            }
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
        if (!c.getName().equals("Grade") && !c.getName().equals("Teacher") && !permissions.equals("normal_user")) {
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

        addWindowListener(conn, frame, user, permissions);
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

    public void addWindowListener(Connection conn, JFrame frame, String user, String permissions) {
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                gui.Main_Frame(conn, user, permissions);
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
