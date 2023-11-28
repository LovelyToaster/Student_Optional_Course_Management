
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gui {
    public void Login_Frame(Student stu) throws IOException, ClassNotFoundException {
        Login login = new Login();
        login.user_out();
        JFrame frame = new JFrame("登录");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLocationRelativeTo(null); // 将窗口置于屏幕中央

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // 账号标签和文本框
        JLabel accountLabel = new JLabel("账号:");
        JTextField accountTextField = new JTextField(15);
        // 密码标签和文本框
        JLabel passwordLabel = new JLabel("密码:");
        JPasswordField passwordField = new JPasswordField(15);

        // 登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String frame_name = accountTextField.getText();
                String frame_password = String.valueOf(passwordField.getPassword());
                int i = login.password_verify(frame_name, frame_password);
                if (i == 0) {
                    JOptionPane.showMessageDialog(frame, "登录成功");
                    frame.dispose();
                    Main_Frame(stu, frame_name, login);
                } else if (i == 2) {
                    JOptionPane.showMessageDialog(frame, "请输入用户名或密码");
                } else {
                    JOptionPane.showMessageDialog(frame, "用户名或密码错误");
                }
            }
        });
        // 注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String frame_new_name = accountTextField.getText();
                String frame_new_password = String.valueOf(passwordField.getPassword());
                int i = login.user_register(frame_new_name, frame_new_password);
                if (i == 0) {
                    JOptionPane.showMessageDialog(frame, "注册成功");
                } else if (i == 1) {
                    JOptionPane.showMessageDialog(frame, "请输入用户名或密码");
                } else {
                    JOptionPane.showMessageDialog(frame, "用户名重复");
                }
            }
        });
        JButton restartButton = new JButton("重置密码");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = JOptionPane.showInputDialog(frame, "请输入要重置的用户名:", "输入", JOptionPane.QUESTION_MESSAGE);
                int i = login.password_restart(user);
                if (i == 0) {
                    JOptionPane.showMessageDialog(frame, "密码重置成功，默认密码为123456!");
                } else if (i == 1) {
                    JOptionPane.showMessageDialog(frame, "密码重置失败，未找到用户名!");
                }
            }
        });

        // 将组件添加到面板中
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(accountLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(accountTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        panel.add(passwordLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(20, 5, 5, 5); // 调整上方间距
        panel.add(loginButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(20, 5, 5, 5); // 调整上方间距
        panel.add(registerButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(20, 5, 5, 5); // 调整上方间距
        panel.add(restartButton, constraints);

        frame.add(panel);
        frame.setVisible(true);

    }

    public void Main_Frame(Student stu, String user, Login login) {
        // 创建主窗口
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.setBackground(new Color(135, 206, 235));

        // 创建标题标签
        JLabel titleLabel = new JLabel("欢迎使用学生宿舍信息管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);

        // 创建按钮
        JButton addButton = new JButton("添加学生信息");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setFont(new Font("宋体", Font.BOLD, 20));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);

        JButton managementButton = new JButton("管理学生信息");
        managementButton.setBackground(new Color(70, 130, 180));
        managementButton.setFont(new Font("宋体", Font.BOLD, 20));
        managementButton.setForeground(Color.WHITE);
        managementButton.setFocusPainted(false);
        managementButton.setBorderPainted(false);

        JButton viewButton = new JButton("查看学生信息");
        viewButton.setBackground(new Color(70, 130, 180));
        viewButton.setFont(new Font("宋体", Font.BOLD, 20));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setBorderPainted(false);

        JButton passwordmodButton = new JButton("修改密码");
        passwordmodButton.setBackground(new Color(70, 130, 180));
        passwordmodButton.setFont(new Font("宋体", Font.BOLD, 20));
        passwordmodButton.setForeground(Color.WHITE);
        passwordmodButton.setFocusPainted(false);
        passwordmodButton.setBorderPainted(false);

        JButton exitButton = new JButton("退出到登陆界面");
        exitButton.setBackground(new Color(70, 130, 180));
        exitButton.setFont(new Font("宋体", Font.BOLD, 20));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);

        // 添加按钮点击事件监听器
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Add_Frame(stu, user, login);
            }
        });

        managementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Management_Frame(stu, user, login);
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                View_Frame(stu, user, login);
            }
        });

        passwordmodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog stu_dialog = new JDialog();
                JButton confirmButton = new JButton("确认");
                stu_dialog.setSize(400, 300);
                stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                stu_dialog.setLocationRelativeTo(null);
                stu_dialog.setTitle("请输入信息");

                JTextField user_frame = new JTextField(user, 80);
                user_frame.setEditable(false);
                JPasswordField password_frame = new JPasswordField(80);
                JPasswordField restart_password_frame = new JPasswordField(80);

                JPanel panel = new JPanel(new GridLayout(5, 2));
                panel.add(new JLabel("当前登录用户"));
                panel.add(user_frame);
                panel.add(new JLabel("当前密码"));
                panel.add(password_frame);
                panel.add(new JLabel("新的密码"));
                panel.add(restart_password_frame);

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String password = String.valueOf(password_frame.getPassword());
                        String restart_password = String.valueOf(restart_password_frame.getPassword());
                        int i = login.password_mod(user, password, restart_password);
                        if (i == 0) {
                            JOptionPane.showMessageDialog(frame, "密码修改成功!");
                        } else if (i == 1) {
                            JOptionPane.showMessageDialog(frame, "请输入完整信息!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "当前密码输入错误,请重新输入!");
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
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                try {
                    Login_Frame(stu);
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 将组件添加到面板中
        panel.add(titleLabel);
        panel.add(addButton);
        panel.add(managementButton);
        panel.add(viewButton);
        panel.add(passwordmodButton);
        panel.add(exitButton);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);
    }

    public void Add_Frame(Student stu, String user, Login login) {
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
        JPanel inputPanel_lable = new JPanel();
        inputPanel_lable.setLayout(new GridLayout(10, 2));
        inputPanel_lable.setBackground(new Color(135, 206, 235));

        JPanel inputPanel_text = new JPanel();
        inputPanel_text.setLayout(new GridLayout(10, 2));
        inputPanel_text.setBackground(new Color(135, 206, 235));

        JLabel noLabel = new JLabel("学号：");
        JLabel nameLabel = new JLabel("姓名：");
        JLabel sexLabel = new JLabel("性别：");
        JLabel instituteLabel = new JLabel("院部：");
        JLabel dormitoryLabel = new JLabel("宿舍楼：");
        JLabel dormitory_numberLabel = new JLabel("宿舍号：");
        JLabel phoneLabel = new JLabel("电话：");

        noLabel.setFont(new Font("宋体", Font.BOLD, 20));
        nameLabel.setFont(new Font("宋体", Font.BOLD, 20));
        sexLabel.setFont(new Font("宋体", Font.BOLD, 20));
        instituteLabel.setFont(new Font("宋体", Font.BOLD, 20));
        dormitoryLabel.setFont(new Font("宋体", Font.BOLD, 20));
        dormitory_numberLabel.setFont(new Font("宋体", Font.BOLD, 20));
        phoneLabel.setFont(new Font("宋体", Font.BOLD, 20));

        JTextField noTextField = new JTextField();
        JTextField nameTextField = new JTextField();
        JTextField instituteTextField = new JTextField();
        JTextField dormitoryTextField = new JTextField();
        JTextField dormitory_numberTextField = new JTextField();
        JTextField phoneTextField = new JTextField();

        String[] sex = {"男", "女"};
        JComboBox sexComboBox = new JComboBox(sex);

        inputPanel_lable.add(noLabel);
        inputPanel_text.add(noTextField);
        inputPanel_lable.add(nameLabel);
        inputPanel_text.add(nameTextField);
        inputPanel_lable.add(sexLabel);
        inputPanel_text.add(sexComboBox);
        inputPanel_lable.add(instituteLabel);
        inputPanel_text.add(instituteTextField);
        inputPanel_lable.add(dormitoryLabel);
        inputPanel_text.add(dormitoryTextField);
        inputPanel_lable.add(dormitory_numberLabel);
        inputPanel_text.add(dormitory_numberTextField);
        inputPanel_lable.add(phoneLabel);
        inputPanel_text.add(phoneTextField);

        // 创建添加按钮
        JButton addButton = new JButton("添加");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);

        // 添加按钮点击事件监听器
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int j = 0;
                Student stu_data = new Student();
//                stu_data.no = noTextField.getText();
                stu_data.name = nameTextField.getText();
                int i = stu.add_student(stu_data);
                if (i == 0) {
                    int option = JOptionPane.showConfirmDialog(frame, "添加成功,要继续添加吗?", "提示", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        frame.dispose();
                        Add_Frame(stu, user, login);
                    } else {
                        frame.dispose();
                        Main_Frame(stu, user, login);
                    }
                } else if (i == 1) {
                    JOptionPane.showMessageDialog(frame, "添加失败,学号发生重复！");
                    return;
                } else if (i == 4) {
                    JOptionPane.showMessageDialog(frame, "添加失败,电话发生重复！");
                    return;
                } else {
                    JOptionPane.showMessageDialog(frame, "添加失败,信息未输入完整！");
                    return;
                }
            }
        });

        // 将组件添加到面板中
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(inputPanel_lable, BorderLayout.LINE_START);
        panel.add(inputPanel_text, BorderLayout.CENTER);
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
                Main_Frame(stu, user, login);
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

    public void Management_Frame(Student stu, String user, Login login) {
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.setBackground(new Color(135, 206, 235));

        // 创建按钮
        JButton TeacherButton = new JButton("管理教师信息");
        TeacherButton.setBackground(new Color(70, 130, 180));
        TeacherButton.setFont(new Font("宋体", Font.BOLD, 20));
        TeacherButton.setForeground(Color.WHITE);
        TeacherButton.setFocusPainted(false);
        TeacherButton.setBorderPainted(false);

        JButton StudentButton = new JButton("管理学生信息");
        StudentButton.setBackground(new Color(70, 130, 180));
        StudentButton.setFont(new Font("宋体", Font.BOLD, 20));
        StudentButton.setForeground(Color.WHITE);
        StudentButton.setFocusPainted(false);
        StudentButton.setBorderPainted(false);

        JButton CourseButton = new JButton("管理选课信息");
        CourseButton.setBackground(new Color(70, 130, 180));
        CourseButton.setFont(new Font("宋体", Font.BOLD, 20));
        CourseButton.setForeground(Color.WHITE);
        CourseButton.setFocusPainted(false);
        CourseButton.setBorderPainted(false);

        JButton ScoreButton = new JButton("管理成绩信息");
        ScoreButton.setBackground(new Color(70, 130, 180));
        ScoreButton.setFont(new Font("宋体", Font.BOLD, 20));
        ScoreButton.setForeground(Color.WHITE);
        ScoreButton.setFocusPainted(false);
        ScoreButton.setBorderPainted(false);

        StudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Gui_Student gui_student = new Gui_Student();
                gui_student.Student_Management_Frame(stu, user, login);
            }
        });

        // 将组件添加到面板中
        panel.add(TeacherButton);
        panel.add(StudentButton);
        panel.add(CourseButton);
        panel.add(ScoreButton);

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

    public void View_Frame(Student stu, String user, Login login) {
        // 创建主窗口
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        //创建显示区域
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

        // 将组件添加到面板中
        panel.add(titleLabel, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);

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
                Main_Frame(stu, user, login);
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