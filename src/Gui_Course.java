import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Gui_Course {
    static final Gui gui = new Gui();
    static final Course course = new Course();

    public void Course_View_Frame(Connection conn, String user, String permissions) {
        // 创建主窗口
        JFrame frame = new JFrame("学生选课信息管理系统");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        //创建显示区域
        String[] columnNames = {"课程号", "课程名", "任课教师", "选课人数"};
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
        stu_view.setColumnIdentifiers(columnNames);
        table.getTableHeader().setReorderingAllowed(false);
        ResultSet rs = course.view_course(conn);
        try {
            while (rs.next()) {
                stu_view.addRow(course.get_course(rs, "view"));
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
        JButton viewButton = getViewButton(conn, table, frame);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);

        // 将组件添加到面板中
        panel.add(titleLabel, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 将面板添加到主窗口中
        frame.add(panel, BorderLayout.CENTER);

        // 显示主窗口
        frame.setVisible(true);

        gui.addWindowListener(conn, frame, user, permissions);
    }

    public JButton getViewButton(Connection conn, JTable table, JFrame frame) {
        JButton viewButton = new JButton("详细");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    JDialog course_dialog = new JDialog();
                    JButton confirmButton = new JButton("确认");
                    course_dialog.setSize(400, 300);
                    course_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    course_dialog.setLocationRelativeTo(null);
                    course_dialog.setTitle("详细信息");

                    JTextField course_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
                    JTextField course_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
                    JTextField course_teacher = new JTextField((String) table.getValueAt(selectedRow, 2), 80);

                    course_no.setEditable(false);
                    course_name.setEditable(false);
                    course_teacher.setEditable(false);

                    String[] columnNames = {"学号", "学生姓名"};
                    JTable table = new JTable() {
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    DefaultTableModel course_view = (DefaultTableModel) table.getModel();
                    course_view.setColumnIdentifiers(columnNames);
                    table.getTableHeader().setReorderingAllowed(false);
                    ResultSet rs = course.search_course(conn, "optional_course_cno", course_no.getText());
                    try {
                        while (rs.next()) {
                            course_view.addRow(course.get_course(rs, "management"));
                        }
                    } catch (SQLException s) {
                        throw new RuntimeException(s);
                    }
                    JScrollPane optional_course_ScrollPane = new JScrollPane();
                    optional_course_ScrollPane.setViewportView(table);

                    JPanel panel = new JPanel(new GridLayout(4, 2));
                    JPanel optional_course_ScrollPane_panel = new JPanel(new GridLayout(1, 1));
                    JPanel CENTER_panel = new JPanel(new GridLayout(2, 1));
                    panel.add(new JLabel("课程号"));
                    panel.add(course_no);
                    panel.add(new JLabel("课程名"));
                    panel.add(course_name);
                    panel.add(new JLabel("任课教师"));
                    panel.add(course_teacher);
                    panel.add(new JLabel("选课情况"));
                    optional_course_ScrollPane_panel.add(optional_course_ScrollPane);

                    confirmButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            course_dialog.dispose();
                        }
                    });
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    buttonPanel.add(confirmButton);

                    CENTER_panel.add(panel);
                    CENTER_panel.add(optional_course_ScrollPane_panel);
                    course_dialog.getContentPane().setLayout(new BorderLayout());
                    course_dialog.getContentPane().add(CENTER_panel, BorderLayout.CENTER);
                    course_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                    course_dialog.setModal(true);
                    course_dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "请选择要查看的学生!");
                }

            }
        });
        return viewButton;
    }
}
