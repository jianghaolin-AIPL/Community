package com.nowcoder.community;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "e:/anzhuanglujing/wkhtmltopdf/bin/wkhtmltoimage https://www.nowcoder.com e:/JAVA/data/wk-images/2.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
