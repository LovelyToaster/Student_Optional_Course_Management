
import java.io.*;
import java.util.HashMap;
import java.util.Set;


public class Login {
    HashMap<String, String> login_map = new HashMap<>(); // 保存用户账号密码

    public int password_verify(String frame_name, String frame_password) { // 账号密码验证
        int i = 0;
        if (frame_name.equals("") || frame_password.equals("")) {
            return 2;
        }
        Set<String> user = login_map.keySet();
        String this_name = null;
        for (String s : user) {
            this_name = s;
            if (frame_name.equals(this_name)) {
                i = 1;
            }
        }
        if (i == 0) {
            return 1;
        }
        String this_password = login_map.get(this_name);
        if (!frame_password.equals(this_password)) { //验证密码
            return 1;
        }
        return 0;

    }

    public int user_register(String frame_new_name, String frame_new_password) { // 注册
        if (frame_new_name.equals("") || frame_new_password.equals(""))
            return 1;
        if (login_map.containsKey(frame_new_name)) { //判断重复
            return 2;
        }
        login_map.put(frame_new_name, frame_new_password); //前面用户名，后面密码
        try {
            user_in();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int password_restart(String user) {
        if (login_map.containsKey(user)) { //判断重复
            login_map.put(user, "123456");
            try {
                user_in();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 0;
        }
        if (user == null) {
            return 2;
        }
        return 1;
    }

    public int password_mod(String user, String password, String restart_password) {
        if (restart_password.equals("") || password.equals("")) {
            return 1;
        }
        if (login_map.get(user).equals(password)) {
            login_map.put(user, restart_password);
            try {
                user_in();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 0;
        }
        return 2;
    }

    public void user_in() throws IOException { // 用户密码写入文件
        FileOutputStream fos = new FileOutputStream("password");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(login_map);
    }

    public void user_out() throws IOException, ClassNotFoundException { // 用户密码写出文件
        File file = new File("password");
        if (!file.exists()) {
            user_in();
        }
        FileInputStream fis = new FileInputStream("password");
        ObjectInputStream ois = new ObjectInputStream(fis);
        login_map = (HashMap<String, String>) ois.readObject();

    }
}
