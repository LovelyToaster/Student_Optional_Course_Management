import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gui_Methods_Student extends Gui_Methods {
    public void Management_Search(JFrame frame, DefaultTableModel view, Connection conn) {
        Object[] o;
        Class<?> c = Student.class;
        String[] option = {"通过学号查询", "通过姓名查询", "通过院系查询", "通过选课情况查询"};
        String message = (String) JOptionPane.showInputDialog(frame, "请选择查询方式", "提示", JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
        if (message != null && message.equals(option[0])) {
            String info = JOptionPane.showInputDialog(frame, "请输入学号", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "no", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[1])) {
            String info = JOptionPane.showInputDialog(frame, "请输入姓名", "输入", JOptionPane.QUESTION_MESSAGE);
            o = new Object[]{c, "name", info};
            super.Search(frame, view, conn, o);
        }
        if (message != null && message.equals(option[2])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Student.class, "faculties", option_array);
            if (flag.equals("normal")) {
                String[] option_faculties = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择院系", "提示", JOptionPane.QUESTION_MESSAGE, null, option_faculties, option_faculties[0]);
                o = new Object[]{c, "faculties", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
        if (message != null && message.equals(option[3])) {
            ArrayList<String> option_array = new ArrayList<>();
            String flag = super.Search_AddOption(conn, Student.class, "optional_course", option_array);
            if (flag.equals("normal")) {
                String[] option_optional_course = option_array.toArray(new String[0]);
                String info = (String) JOptionPane.showInputDialog(frame, "请选择课程", "提示", JOptionPane.QUESTION_MESSAGE, null, option_optional_course, option_optional_course[0]);
                o = new Object[]{c, "optional_course", info};
                super.Search(frame, view, conn, o);
            } else {
                JOptionPane.showMessageDialog(frame, "没有查询到信息!");
            }
        }
    }

    public void Management_Modify(JFrame frame, JDialog dialog, JTable table, JButton confirmButton, Connection conn, int selectedRow) {
        JButton modButton = new JButton("修改");
        Class<?> c = Student.class;
        JTextField stu_no = new JTextField((String) table.getValueAt(selectedRow, 0), 80);
        JTextField stu_name = new JTextField((String) table.getValueAt(selectedRow, 1), 80);
        JTextField stu_faculties = new JTextField((String) table.getValueAt(selectedRow, 2), 80);
        stu_no.setEditable(false);

        String[] columnNames = {"课程名", "任课教师"};
        table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableModel stu_view = (DefaultTableModel) table.getModel();
        stu_view.setColumnIdentifiers(columnNames);
        table.getTableHeader().setReorderingAllowed(false);
        try {
            Method method_search = c.getMethod("search", Connection.class, String.class, String.class);
            Method method_get = c.getMethod("get", ResultSet.class, String.class);
            ResultSet rs = (ResultSet) method_search.invoke(c.getDeclaredConstructor().newInstance(), conn, "optional_course_no", stu_no.getText());
            if (rs.next()) {
                do {
                    stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "management"));
                } while (rs.next());
            } else
                stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "null"));
        } catch (SQLException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException s) {
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
                try {
                    Method method_search = c.getMethod("search", Connection.class, String.class, String.class);
                    Method method_get = c.getMethod("get", ResultSet.class, String.class);
                    ResultSet rs = (ResultSet) method_search.invoke(c.getDeclaredConstructor().newInstance(), conn, "optional_course_no", stu_no.getText());
                    if (rs.next()) {
                        do {
                            stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "management"));
                        } while (rs.next());
                    } else
                        stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "null"));
                } catch (SQLException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException s) {
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
                        try {
                            Method method_search = c.getMethod("search", Connection.class, String.class, String.class);
                            Method method_get = c.getMethod("get", ResultSet.class, String.class);
                            ResultSet rs = (ResultSet) method_search.invoke(c.getDeclaredConstructor().newInstance(), conn, "optional_course_no_not", stu_no.getText());
                            if (rs.next()) {
                                do {
                                    stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "management"));
                                } while (rs.next());
                            } else
                                stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "null"));
                        } catch (SQLException | NoSuchMethodException | InstantiationException |
                                 IllegalAccessException | InvocationTargetException s) {
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
                                try {
                                    Object[] o = {stu_no.getText(), "null", "null", optional_course, course_teacher};
                                    Method method_modify = c.getMethod("modify", Connection.class, String.class, Object[].class);
                                    String flag = (String) method_modify.invoke(c.getDeclaredConstructor().newInstance(), conn, "add", o);

                                    if (flag.equals("normal")) {
                                        JOptionPane.showMessageDialog(add, "添加成功!");
                                        stu_view.setRowCount(0);
                                        stu_view_oc.setRowCount(0);
                                        Method method_search = c.getMethod("search", Connection.class, String.class, String.class);
                                        Method method_get = c.getMethod("get", ResultSet.class, String.class);
                                        ResultSet rs = (ResultSet) method_search.invoke(c.getDeclaredConstructor().newInstance(), conn, "optional_course_no", stu_no.getText());
                                        if (rs.next()) {
                                            do {
                                                stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "management"));
                                                stu_view_oc.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "management"));
                                            } while (rs.next());
                                        } else {
                                            stu_view.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "null"));
                                            stu_view_oc.addRow((Object[]) method_get.invoke(c.getDeclaredConstructor().newInstance(), rs, "null"));
                                        }
                                        add.dispose();
                                    }
                                } catch (InvocationTargetException | NoSuchMethodException |
                                         IllegalAccessException | InstantiationException |
                                         SQLException ex) {
                                    throw new RuntimeException(ex);
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
                        Object[] o = {"null", "null", optional_course, null};
                        try {
                            Method method_modify = c.getMethod("modify", Connection.class, String.class, Object[].class);
                            String flag = (String) method_modify.invoke(c.getDeclaredConstructor().newInstance(), conn, "delete", o);
                            if (flag.equals("normal"))
                                JOptionPane.showMessageDialog(stu_dialog, "删除成功!");
                        } catch (InvocationTargetException | IllegalAccessException |
                                 NoSuchMethodException | InstantiationException ex) {
                            throw new RuntimeException(ex);
                        }
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
                Object[] o = {s_no, s_name, s_faculties, optional_course, course_teacher};
                try {
                    Method method_modify = c.getMethod("modify", Connection.class, String.class, Object[].class);
                    String flag = (String) method_modify.invoke(c.getDeclaredConstructor().newInstance(), conn, "null", o);
                    if (flag.equals("1")) {
                        JOptionPane.showMessageDialog(frame, "修改失败,重复选课!");
                    } else {
                        stu_view.setRowCount(0);
                        stu_view.addRow(new Object[]{s_no, s_name, s_faculties});
                        dialog.dispose();
                    }
                } catch (InvocationTargetException | IllegalAccessException |
                         NoSuchMethodException | InstantiationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(modButton);
        buttonPanel.add(confirmButton);

        JPanel center_panel = new JPanel(new GridLayout(2, 1));
        center_panel.add(panel);
        center_panel.add(optional_course_list);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(center_panel, BorderLayout.CENTER);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        dialog.setModal(true);
        dialog.setVisible(true);
    }

    public JButton getOptionalCourseButton(Connection conn, String user, DefaultTableModel stu_view) {
        Student stu = new Student();
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
                ResultSet rs = stu.search(conn, "optional_course_no", user);
                try {
                    if (rs.next()) {
                        do {
                            stu_view_oc.addRow(stu.get(rs, "management"));
                        } while (rs.next());
                    } else
                        stu_view_oc.addRow(stu.get(rs, "null"));
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
                        ResultSet rs = stu.search(conn, "optional_course_no_not", user);
                        try {
                            if (rs.next()) {
                                do {
                                    stu_view_add.addRow(stu.get(rs, "management"));
                                } while (rs.next());
                            } else
                                stu_view_add.addRow(stu.get(rs, "null"));
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
                                Object[] o = new Object[]{user, "null", "null", optional_course, course_teacher};
                                String flag = stu.modify(conn, "add", o);
                                if (flag.equals("normal")) {
                                    JOptionPane.showMessageDialog(add, "添加成功!");
                                    stu_view.setRowCount(0);
                                    stu_view_oc.setRowCount(0);
                                    ResultSet rs = stu.search(conn, "optional_course_no", user);
                                    try {
                                        if (rs.next()) {
                                            do {
                                                stu_view.addRow(stu.get(rs, "management"));
                                                stu_view_oc.addRow(stu.get(rs, "management"));
                                            } while (rs.next());
                                        } else {
                                            stu_view.addRow(stu.get(rs, "null"));
                                            stu_view_oc.addRow(stu.get(rs, "null"));
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
                        Object[] o = new Object[]{user, "null", "null", optional_course, null};
                        String flag = stu.modify(conn, "delete", o);
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
