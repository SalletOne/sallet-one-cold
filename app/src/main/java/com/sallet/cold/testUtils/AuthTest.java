package com.sallet.cold.testUtils;

import org.junit.Test;

public class AuthTest {
    private static String secret="AKZ27P5Q6MGBH6NF";
    @Test
    public void genSecretTest() {//
        secret = GoogleAuthenticator.generateSecretKey();
        String qrcode = GoogleAuthenticator.getQRBarcode("cxx987@foxmail.com", secret);
        System.out.println("qrcode:" + qrcode + ",key:" + secret);
    }
    /**
     */
    @Test
    public void verifyTest() {
        long code = 61096;
        long t = System.currentTimeMillis();
        boolean r = GoogleAuthenticator.check_code(secret, code, t);
        System.out.println("？" + r);
    }
}
