
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;

public class Gui {
    static final Gui_Method gui_method = new Gui_Method();
    static final Login login = new Login();

    public void Login_Frame(Connection conn) {
        Login login = new Login();
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
        loginButton.addActionListener(e -> {
            String frame_name = accountTextField.getText();
            String frame_password = String.valueOf(passwordField.getPassword());
            String permissions = login.password_verify(conn, frame_name, frame_password);
            switch (permissions) {
                case "normal_root" -> {
                    JOptionPane.showMessageDialog(frame, "登录成功，欢迎管理员" + frame_name + "登录系统");
                    frame.dispose();
                    Main_Frame(conn, frame_name, permissions);
                }
                case "normal_user" -> {
                    JOptionPane.showMessageDialog(frame, "登录成功，欢迎用户" + frame_name + "登录系统");
                    frame.dispose();
                    Main_Frame(conn, frame_name, permissions);
                }
                case "empty" -> JOptionPane.showMessageDialog(frame, "请输入用户名或密码");
                case "error" -> JOptionPane.showMessageDialog(frame, "用户名或密码错误");
            }
        });
        // 注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(e -> {
            String frame_new_name = accountTextField.getText();
            String frame_new_password = String.valueOf(passwordField.getPassword());
            int i = login.user_register(conn, frame_new_name, frame_new_password);
            if (i == 0) {
                JOptionPane.showMessageDialog(frame, "注册成功");
            } else if (i == -2) {
                JOptionPane.showMessageDialog(frame, "请输入用户名或密码");
            } else if (i == -1) {
                JOptionPane.showMessageDialog(frame, "用户名重复");
            }
        });
        JButton restartButton = new JButton("重置密码");
        restartButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "请联系管理员修改密码"));

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

    public void Main_Frame(Connection conn, String user, String permissions) {
        // 创建主窗口
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel;
        if (permissions.equals("normal_root")) {
            panel = new JPanel(new GridLayout(8, 1));
        } else {
            panel = new JPanel(new GridLayout(6, 1));
        }
        panel.setBackground(new Color(135, 206, 235));

        // 创建标题标签
        JLabel titleLabel = new JLabel("欢迎使用学生宿舍信息管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 25));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel;
        if (permissions.equals("normal_root")) {
            userLabel = new JLabel("登录管理员:" + user, SwingConstants.CENTER);
        } else {
            userLabel = new JLabel("登录用户:" + user, SwingConstants.CENTER);
        }
        userLabel.setFont(new Font("宋体", Font.BOLD, 20));
        userLabel.setForeground(Color.WHITE);

        // 创建按钮
        JButton addButton = new JButton("添加信息");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setFont(new Font("宋体", Font.BOLD, 20));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);

        JButton managementButton;
        JButton viewButton = new JButton("查看信息");

        if (permissions.equals("normal_root")) {
            managementButton = new JButton("管理信息");
        } else {
            managementButton = new JButton("管理个人信息");
        }
        managementButton.setBackground(new Color(70, 130, 180));
        managementButton.setFont(new Font("宋体", Font.BOLD, 20));
        managementButton.setForeground(Color.WHITE);
        managementButton.setFocusPainted(false);
        managementButton.setBorderPainted(false);

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

        JButton StudentPasswordButton = new JButton("重置学生密码");
        StudentPasswordButton.setBackground(new Color(70, 130, 180));
        StudentPasswordButton.setFont(new Font("宋体", Font.BOLD, 20));
        StudentPasswordButton.setForeground(Color.WHITE);
        StudentPasswordButton.setFocusPainted(false);
        StudentPasswordButton.setBorderPainted(false);

        JButton exitButton = new JButton("退出到登陆界面");
        exitButton.setBackground(new Color(70, 130, 180));
        exitButton.setFont(new Font("宋体", Font.BOLD, 20));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);

        // 添加按钮点击事件监听器
        addButton.addActionListener(e -> {
            frame.dispose();
            Add_Frame(conn, user, permissions);
        });

        managementButton.addActionListener(e -> {
            frame.dispose();
            Management_Frame(conn, user, permissions);
        });

        viewButton.addActionListener(e -> {
            frame.dispose();
            View_Frame(conn, user, permissions);
        });

        passwordmodButton.addActionListener(e -> {
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

            JPanel panel1 = new JPanel(new GridLayout(5, 2));
            panel1.add(new JLabel("当前登录用户"));
            panel1.add(user_frame);
            panel1.add(new JLabel("当前密码"));
            panel1.add(password_frame);
            panel1.add(new JLabel("新的密码"));
            panel1.add(restart_password_frame);

            confirmButton.addActionListener(e1 -> {
                String password = String.valueOf(password_frame.getPassword());
                String restart_password = String.valueOf(restart_password_frame.getPassword());
                String flag = login.password_mod(conn, user, password, restart_password);
                if (flag.equals("normal")) {
                    JOptionPane.showMessageDialog(frame, "密码修改成功!");
                    stu_dialog.dispose();
                } else if (flag.equals("empty")) {
                    JOptionPane.showMessageDialog(frame, "请输入完整信息!");
                } else {
                    JOptionPane.showMessageDialog(frame, "当前密码输入错误,请重新输入!");
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(confirmButton);

            stu_dialog.getContentPane().setLayout(new BorderLayout());
            stu_dialog.getContentPane().add(panel1, BorderLayout.CENTER);
            stu_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            stu_dialog.setModal(true);
            stu_dialog.setVisible(true);
        });
        StudentPasswordButton.addActionListener(e -> {
            JDialog stu_dialog = new JDialog();
            JButton confirmButton = new JButton("确认");
            stu_dialog.setSize(300, 200);
            stu_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            stu_dialog.setLocationRelativeTo(null);
            stu_dialog.setTitle("请输入信息");

            JTextField user_frame = new JTextField();
            JPanel panel1 = new JPanel(new GridLayout(3, 2));
            panel1.add(new JLabel("要重置密码的用户"));
            panel1.add(user_frame);

            confirmButton.addActionListener(e1 -> {
                String flag = login.password_restart(conn, user_frame.getText());
                if (flag.equals("normal")) {
                    JOptionPane.showMessageDialog(frame, "密码重置成功!默认密码为123456");
                    stu_dialog.dispose();
                } else if (flag.equals("empty")) {
                    JOptionPane.showMessageDialog(frame, "请输入完整信息!");
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(confirmButton);

            stu_dialog.getContentPane().setLayout(new BorderLayout());
            stu_dialog.getContentPane().add(panel1, BorderLayout.CENTER);
            stu_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            stu_dialog.setModal(true);
            stu_dialog.setVisible(true);
        });
        exitButton.addActionListener(e -> {
            frame.dispose();
            Login_Frame(conn);
        });

        // 将组件添加到面板中
        panel.add(titleLabel);
        panel.add(userLabel);
        if (permissions.equals("normal_root")) {
            panel.add(addButton);
        }
        panel.add(managementButton);
        panel.add(viewButton);
        panel.add(passwordmodButton);
        panel.add(StudentPasswordButton);
        panel.add(exitButton);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);
    }

    public void Add_Frame(Connection conn, String user, String permissions) {
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.setBackground(new Color(135, 206, 235));

        // 创建按钮
        JButton TeacherButton = new JButton("添加教师信息");
        TeacherButton.setBackground(new Color(70, 130, 180));
        TeacherButton.setFont(new Font("宋体", Font.BOLD, 20));
        TeacherButton.setForeground(Color.WHITE);
        TeacherButton.setFocusPainted(false);
        TeacherButton.setBorderPainted(false);

        JButton StudentButton = new JButton("添加学生信息");
        StudentButton.setBackground(new Color(70, 130, 180));
        StudentButton.setFont(new Font("宋体", Font.BOLD, 20));
        StudentButton.setForeground(Color.WHITE);
        StudentButton.setFocusPainted(false);
        StudentButton.setBorderPainted(false);

        JButton CourseButton = new JButton("添加课程信息");
        CourseButton.setBackground(new Color(70, 130, 180));
        CourseButton.setFont(new Font("宋体", Font.BOLD, 20));
        CourseButton.setForeground(Color.WHITE);
        CourseButton.setFocusPainted(false);
        CourseButton.setBorderPainted(false);

        TeacherButton.addActionListener(e -> {
            frame.dispose();
            gui_method.Add_Frame(conn, Teacher.class, user, permissions);
        });
        StudentButton.addActionListener(e -> {
            frame.dispose();
            gui_method.Add_Frame(conn, Student.class, user, permissions);
        });
        CourseButton.addActionListener(e -> {
            frame.dispose();
            gui_method.Add_Frame(conn, Course.class, user, permissions);
        });
        // 将组件添加到面板中
        panel.add(TeacherButton);
        panel.add(StudentButton);
        panel.add(CourseButton);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);

        addWindowListener(conn, frame, user, permissions);
    }

    public void Management_Frame(Connection conn, String user, String permissions) {
        if (permissions.equals("normal_root")) {
            JFrame frame = new JFrame("学生宿舍信息管理系统");
            frame.setSize(300, 400);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(5, 1));
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

            JButton CourseButton = new JButton("管理课程信息");
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

            TeacherButton.addActionListener(e -> {
                frame.dispose();
                gui_method.Management_Frame(conn, Teacher.class, user, permissions);
            });
            StudentButton.addActionListener(e -> {
                frame.dispose();
                gui_method.Management_Frame(conn, Student.class, user, permissions);
            });
            CourseButton.addActionListener(e -> {
                frame.dispose();
                gui_method.Management_Frame(conn, Course.class, user, permissions);
            });
            ScoreButton.addActionListener(e -> {
                frame.dispose();
                gui_method.Management_Frame(conn, Grade.class, user, permissions);
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

            addWindowListener(conn, frame, user, permissions);
        } else {
            gui_method.Management_Frame(conn, Student.class, user, permissions);
        }
    }

    public void View_Frame(Connection conn, String user, String permissions) {
        JFrame frame = new JFrame("学生宿舍信息管理系统");
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        if (permissions.equals("normal_root"))
            panel.setLayout(new GridLayout(4, 1));
        else
            panel.setLayout(new GridLayout(3, 1));
        panel.setBackground(new Color(135, 206, 235));

        // 创建按钮
        JButton TeacherButton = new JButton("查看教师信息");
        TeacherButton.setBackground(new Color(70, 130, 180));
        TeacherButton.setFont(new Font("宋体", Font.BOLD, 20));
        TeacherButton.setForeground(Color.WHITE);
        TeacherButton.setFocusPainted(false);
        TeacherButton.setBorderPainted(false);

        JButton StudentButton = new JButton("查看学生信息");
        StudentButton.setBackground(new Color(70, 130, 180));
        StudentButton.setFont(new Font("宋体", Font.BOLD, 20));
        StudentButton.setForeground(Color.WHITE);
        StudentButton.setFocusPainted(false);
        StudentButton.setBorderPainted(false);

        JButton CourseButton = new JButton("查看课程信息");
        CourseButton.setBackground(new Color(70, 130, 180));
        CourseButton.setFont(new Font("宋体", Font.BOLD, 20));
        CourseButton.setForeground(Color.WHITE);
        CourseButton.setFocusPainted(false);
        CourseButton.setBorderPainted(false);

        JButton ScoreButton;
        if (permissions.equals("normal_root")) {
            ScoreButton = new JButton("查看成绩信息");
        } else {
            ScoreButton = new JButton("查看个人成绩");
        }
        ScoreButton.setBackground(new Color(70, 130, 180));
        ScoreButton.setFont(new Font("宋体", Font.BOLD, 20));
        ScoreButton.setForeground(Color.WHITE);
        ScoreButton.setFocusPainted(false);
        ScoreButton.setBorderPainted(false);

        TeacherButton.addActionListener(e -> {
            frame.dispose();
            gui_method.View_Frame(conn, Teacher.class, user, permissions);
        });
        StudentButton.addActionListener(e -> {
            frame.dispose();
            gui_method.View_Frame(conn, Student.class, user, permissions);
        });
        CourseButton.addActionListener(e -> {
            frame.dispose();
            gui_method.View_Frame(conn, Course.class, user, permissions);
        });
        ScoreButton.addActionListener(e -> {
            frame.dispose();
            gui_method.View_Frame(conn, Grade.class, user, permissions);
        });

        // 将组件添加到面板中
        panel.add(TeacherButton);
        if (permissions.equals("normal_root"))
            panel.add(StudentButton);
        panel.add(CourseButton);
        panel.add(ScoreButton);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);

        addWindowListener(conn, frame, user, permissions);
    }

    public void addWindowListener(Connection conn, JFrame frame, String user, String permissions) {
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                Main_Frame(conn, user, permissions);
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