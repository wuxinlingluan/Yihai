package com.github.yihai.common.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ${sheldon} on 2017/7/11.
 */

public class MD5Tools {
    static String ivstr = "#C2AE4D";
    static String key = "#C2AE4D";
    // 初始化向量，随机填充
    private static byte[] iv = ivstr.getBytes();
//	private static  String encryptKey="#C2AE4D";

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String getMD51(String val) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            byte[] m = md5.digest();    // 加密
            return getString(m);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        LogUtils.v("加密结果：" + sb.toString());
        return sb.toString();
    }

    /**
     * @param encryptString 须要加密的明文
     * @return 加密后的密文
     * @throws Exception
     */
    public static String encryptDES(String encryptString) throws Exception {
        LogUtils.d("DESkaishi=====" + encryptString);
        // 实例化IvParameterSpec对象，使用指定的初始化向量
        IvParameterSpec zeroIv = new IvParameterSpec(ivstr.getBytes("UTF-8"));
        // 实例化SecretKeySpec类，依据字节数组来构造SecretKey
        SecretKeySpec key = new SecretKeySpec(iv, "DES");
        // 创建password器
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 用秘钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        // 运行加密操作
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        LogUtils.d("DES加密====>" + Base64.encodeToString(encryptedData, Base64.DEFAULT));
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    /**
     * 解密
     *
     * @param encryptString
     * @return
     * @throws Exception
     */
    public static String decodeDES(String encryptString) throws Exception {
        byte[] string = Base64.decode(encryptString, Base64.DEFAULT);
        // 实例化IvParameterSpec对象，使用指定的初始化向量
        IvParameterSpec zeroIv = new IvParameterSpec(ivstr.getBytes());
        // 实例化SecretKeySpec类，依据字节数组来构造SecretKey
        SecretKeySpec key = new SecretKeySpec(iv, "DES");
        // 创建password器
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 用秘钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        // 运行解密操作
        byte[] encryptedData = cipher.doFinal(string);
        return new String(encryptedData, "utf-8");
    }

    // 解密数据
    public static String decrypt(String message) throws Exception {
        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    public static String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
//         cipher.doFinal(message.getBytes("UTF-8"));
        return toHexString(cipher.doFinal(message.getBytes("UTF-8")));
    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    /**
     * String 转ASCII
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

}

