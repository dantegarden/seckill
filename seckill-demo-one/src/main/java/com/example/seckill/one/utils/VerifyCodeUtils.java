package com.example.seckill.one.utils;

import com.example.seckill.one.vo.VerifyCodeVO;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyCodeUtils {

    private static char[] ops = new char[] {'+', '-', '*'};
    //宽度高度和页面保持一致
    private static final Integer WIDTH = 80;
    private static final Integer HEIGHT = 32;

    public static VerifyCodeVO createVerifyCode(){
        //在内存中创建图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 背景色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // 边框
        g.setColor(Color.black);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // 生成干扰点
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(WIDTH);
            int y = rdm.nextInt(HEIGHT);
            g.drawOval(x, y, 0, 0);
        }
        // 生成数学公式，并写入图片
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        int rnd = calc(verifyCode);
        //输出图片
        return new VerifyCodeVO().setCalc(rnd).setImage(image);
    }

    public static String generateVerifyCode(Random rdm) {
        //10以内3个数字加减乘
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
