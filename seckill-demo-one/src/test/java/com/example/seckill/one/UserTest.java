package com.example.seckill.one;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.utils.DBUtils;
import com.example.seckill.one.utils.MD5Utils;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserTest {

    private static final String urlString = "http://192.168.31.200:18080/seckill/login/doLogin";
    private static final String defaultPwd = "123456";
    private static final String defaultSalt = "1a2b3c";

    public void createUser(int count) throws Exception{
        List<User> users = new ArrayList<User>(count);
        //生成用户
        for(int i=0;i<count;i++) {
            User user = new User();
            user.setId(13000000000L+i);
            user.setLoginCount(1);
            user.setNickname("user"+i);
            user.setRegisterDate(new Date());
            user.setSalt(defaultSalt);
            user.setPassword(MD5Utils.inputPassToDbPass(defaultPwd, user.getSalt()));
            user.setMobile(String.valueOf(user.getId()));
            users.add(user);
        }
        System.out.println("create user");

        //插入数据库
        //updateDB(users);

        //登录，生成token

        File file = new File("/tokens.txt");
        System.out.println(file.getAbsolutePath());
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0;i<users.size();i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile="+user.getMobile()+"&password="+MD5Utils.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0 ,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println(String.format("create user (id : %s) token : %s", user.getId(), token));

            String row = user.getId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }


    public void updateDB(List<User> users){
        try{
            Connection conn = DBUtils.getConn();
            conn.setAutoCommit(false);
            String sql = "insert into tb_user(login_name, login_count, nickname, register_date, salt, password, mobile, id)values(?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for(int i=0; i<users.size(); i++){
                //插入数据库
                User user = users.get(i);
                pstmt.setString(1, user.getNickname());
                pstmt.setInt(2, user.getLoginCount());
                pstmt.setString(3, user.getNickname());
                pstmt.setTimestamp(4, new Timestamp(user.getRegisterDate().getTime()));
                pstmt.setString(5, user.getSalt());
                pstmt.setString(6, user.getPassword());
                pstmt.setString(7,user.getMobile());
                pstmt.setLong(8, user.getId());
                pstmt.addBatch();
                if((i+1) % 1000 == 0){
                    pstmt.executeBatch();
                    conn.commit();
                    pstmt.close();
                    conn.close();

                    System.out.println(String.format("插入user [%s-%s]", i-999, i+1));

                    conn = DBUtils.getConn();
                    conn.setAutoCommit(false);
                    pstmt = conn.prepareStatement(sql);
                }

            }

            System.out.println("insert to db");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void test() throws Exception {
        createUser(5000);
    }

    private static String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    private static String getTel() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }
    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

}
