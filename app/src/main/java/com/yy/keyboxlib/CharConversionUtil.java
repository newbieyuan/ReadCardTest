package com.yy.keyboxlib;

import android.text.TextUtils;

/**
 * 创建人：   yy
 * 创建时间： 2018/8/2
 * 功能描述: 字符转换工具类
 * 版本号：
 */
public class CharConversionUtil {
    /**
     *  Int 转十六进制字符串，可指定长度，长度不够的在字符串前补0
     * @param data
     * @param length
     * @return
     */
    public static String intToHexString(int data, int length) {
        String hexString = Integer.toHexString(data).toUpperCase();
        while (length > hexString.length()) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    /**
     * 字节转10进制
     * @param b
     * @return
     */
    public static int byte2Int(byte b){
        int r = b & 0xFF;
        return r;
    }

    /**
     * 10进制转字节
     * @param i
     * @return
     */
    public static byte int2Byte(int i){
        byte r = (byte) i;
        return r;
    }

    /**
     * Byte数组转16进制字符串
     * @param b
     * @return
     */
    public static String byte2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            int intValues = byte2Int(b[i]);
            r += intToHexString(intValues,2).toUpperCase();
        }
        return r;
    }
    /**
     * Byte数组转16进制字符串
     * @param b
     * @return
     */
    public static String byte2HexString(byte b) {
        String r = "";
        int intValues = byte2Int(b);
        r = intToHexString(intValues,2).toUpperCase();
        return r;
    }

    /**
     * char 转换为 Byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 16进制字符串转 byte 数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))){
            return null;
        }
        else if (hex.length()%2 != 0){
            return null;
        }
        else{
            hex = hex.toUpperCase();
            int len = hex.length()/2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1]));
            }
            return b;
        }
    }

    /**
     * byte 数组转字符串
     */
    public static String bytes2String(byte[] b) throws Exception {
        String r = new String(b,"UTF-8");
        return r;
    }

    /**
     * 字符串转 byte 数组
     */
    public static byte[] string2Bytes(String s){
        byte[] r = s.getBytes();
        return r;
    }
    /**
     * 16进制字符串转字符串
     */
    public static String hex2String(String hex) throws Exception {
        String r = bytes2String(hexString2Bytes(hex));
        return r;
    }

    /**
     * 字符串转16进制字符串
     */
    public static String string2HexString(String s) throws Exception {
        String r = byte2HexString(string2Bytes(s));
        return r;
    }

    /** 十六进制字符串转十进制数组 */
    public static int[] hexStringToIntArray(String msg)
    {
        int[] Int_result = new int[msg.length() / 2];
        int i_pos = 0;
        for (int i = 0; i < msg.length(); i = i + 2) {
            Int_result[i_pos] = Integer.parseInt(msg.substring(i, i + 2), 16);
            i_pos++;
        }
        return Int_result;
    }

    /**
     * 十六进制字符串转十进制
     *
     * 例如 ： 01 02 --> 01* 1*2~8 + 02 * 0 * 256
     */
    public static int hexStringToInt(String msg)
    {
        int Int_result = 0;
        if (TextUtils.isEmpty(msg)){
            return Int_result;
        }
        Int_result = Integer.parseInt(msg,16);
        return Int_result;
    }

    /**
     * 将十六进制的字符串转换成二进制的字符串
     *
     * @param hexString
     * @return
     */
    public static String hexStrToBinaryStr(String hexString) {

        if (hexString == null || hexString.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        // 将每一个十六进制字符分别转换成一个四位的二进制字符
        for (int i = 0; i < hexString.length(); i++) {
            String indexStr = hexString.substring(i, i + 1);
            String binaryStr = Integer.toBinaryString(Integer.parseInt(indexStr, 16));
            while (binaryStr.length() < 4) {
                binaryStr = "0" + binaryStr;
            }
            sb.append(binaryStr);
        }

        return sb.toString();
    }

    /**
     * 二进制字符串转换为十六进制字符串
     * <p>
     * 二进制字符串位数必须满足是4的倍数
     *
     * @param binaryStr
     * @return
     */
    public static String binaryStrToHexStr(String binaryStr) {

        if (binaryStr == null || binaryStr.equals("") || binaryStr.length() % 4 != 0) {
            return null;
        }

        StringBuffer sbs = new StringBuffer();
        // 二进制字符串是4的倍数，所以四位二进制转换成一位十六进制
        for (int i = 0; i < binaryStr.length() / 4; i++) {
            String subStr = binaryStr.substring(i * 4, i * 4 + 4);
            String hexStr = Integer.toHexString(Integer.parseInt(subStr, 2));
            sbs.append(hexStr);
        }

        return sbs.toString();
    }

    /** Byte数组转换成对应的十进制数组 */
    public static int[] byteArrayToIntArray(byte[] ByteValue)
    {
        int[] intValues = new int[ByteValue.length];
        for (int i = 0; i < ByteValue.length; i++) {
            intValues[i] = ByteValue[i] & 0xFF;
        }
        return intValues;
    }
}
