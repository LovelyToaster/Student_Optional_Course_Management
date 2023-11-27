
import java.io.*;
import java.util.ArrayList;

public class Student {
    String no;
    String name;
    String sex;
    String institute;
    String dormitory;
    String dormitory_number;
    String phone;
    ArrayList<Student> student_manage = new ArrayList<>();

    public int add_student(Student stu) { // 添加学生
        if (stu.no.equals("") || stu.name.equals("") || stu.institute.equals("") || stu.dormitory.equals("") || stu.dormitory_number.equals("") || stu.phone.equals("")) {
            return 3;
        }
        for (Student s : student_manage) {
            if (stu.no.equals(s.no)) {
                return 1;
            }
            if (stu.phone.equals((s.phone))) {
                return 4;
            }
        }
        student_manage.add(stu);
        try {
            student_in();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public Object[] get_student(Student s) { // 获取学生信息
        return new Object[]{s.no, s.name, s.sex, s.institute, s.dormitory, s.dormitory_number, s.phone};
    }

    public Student search_student(String student_data, Student s, int i) { // 查找学生信息
        try {
            if (i == 0) {
                for (Student s_no : student_manage) {
                    if (student_data.equals(s_no.no)) {
                        return s_no;
                    }
                }
            }
            if (i == 1) {
                if (s.name.contains(student_data)) {
                    return s;
                }
            }
            if (i == 2) {
                if (student_data.equals(s.sex)) {
                    return s;
                }
            }
            if (i == 3) {
                if (student_data.equals(s.institute)) {
                    return s;
                }
            }
            if (i == 4) {
                if (student_data.equals(s.dormitory)) {
                    return s;
                }
            }
            if (i == 5) {
                if (s.dormitory_number.contains(student_data)) {
                    return s;
                }
            }
            if (i == 6) {
                if (s.phone.contains(student_data)) {
                    return s;
                }
            }
        }catch (NullPointerException ignored){
        }
        return null;
    }

    public int modify_student(String student_no, Student stu) { // 修改学生信息
        int i = 0;
        for (Student s : student_manage) {
            if (stu.phone.equals(s.phone) && !student_no.equals(s.no)) {
                return 4;
            }
        }
        for (Student s : student_manage) {
            if (student_no.equals(s.no))
                break;
            i++;
        }
        student_manage.set(i, stu);
        try {
            student_in();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void delete_student(String student_no) { // 删除学生信息
        int i = 0;
        for (Student s : student_manage) {
            if (student_no.equals(s.no))
                break;
            i++;
        }
        student_manage.remove(i);
        try {
            student_in();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void student_in() throws IOException { // 学生写入文件
        BufferedWriter bw = new BufferedWriter(new FileWriter("student"));
        for (Student s : student_manage) {
            bw.write(s.no);
            bw.newLine();
            bw.write(s.name);
            bw.newLine();
            bw.write(s.sex);
            bw.newLine();
            bw.write(s.institute);
            bw.newLine();
            bw.write(s.dormitory);
            bw.newLine();
            bw.write(s.dormitory_number);
            bw.newLine();
            bw.write(s.phone);
            bw.newLine();
        }
        bw.close();
    }

    public void student_out() throws IOException { // 学生写出文件
        File file = new File("student");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader("student"));
        String str;
        while ((str = br.readLine()) != null) {
            Student stu = new Student();
            stu.no = str;
            str = br.readLine();
            stu.name = str;
            str = br.readLine();
            stu.sex = str;
            str = br.readLine();
            stu.institute = str;
            str = br.readLine();
            stu.dormitory = str;
            str = br.readLine();
            stu.dormitory_number = str;
            str = br.readLine();
            stu.phone = str;
            student_manage.add(stu);
        }
        br.close();

    }

}