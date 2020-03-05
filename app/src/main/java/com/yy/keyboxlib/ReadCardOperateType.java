package com.yy.keyboxlib;

/**
 * 钥匙柜通讯类型
 */
public class ReadCardOperateType {

    /**
     * 蓝灯开
     * Blue light on
     */
    public static String blueLightOpen="11";
    /**
     * 蓝灯关
     * Blue light off
     */
    public static String blueLightClose="12";
    /**
     * 电磁锁,锁定
     * electromagnetic Lock
     */
    public static String electromagneticLock="14";
    /**
     * 电磁锁，解锁（钥匙可取）
     * electromagnetic Unlock（The key is advisable）
     */
    public static String electromagneticUnlock="13";
    /**
     * 读卡并返回卡号
     *  Read the card and return the card number
     */
    public static String readCard="15";
    /**
     *  微触开关检测
     *  Micro touch switch detection
     */
    public static String testOnlineButNotReadCard ="16";
    /**
     *  微触开关检测，并读取卡号
     *  Micro touch switch detect and read card number
     */
    public static String testOnlineAndReadCard="17";
    /**
     * 红灯开
     * Red light on
     */
    public static String redLightOpen="19";
    /**
     * 红灯关
     *  Red light off
     */
    public static String redLightClose="1A";
    /**
     *  打开箱门
     *  Open the box door
     */
    public static String doorOpen="23";

    /**
     *  打开箱门（应急锁）
     *  Open the box door
     */
    public static String doorOpenUrgent="20";
    /**
     *  关闭箱门  (应急锁)
     *  Open the box door
     */
    public static String doorClose="21";

    /**
     *  检测钥匙柜门状态
     *  Check the status of key cabinet door
     */
    public static String testDoorState="22";
    /**
     *  设置钥匙节点编号（无返回值）
     */
    public static String setKeyAddressNo="25";
    /**
     *  读取箱ID
     */
    public static String readBoxId="40";
    /**
     *  写箱ID前2字节
     */
    public static String writeBoxIdFirstTwo="41";
    /**
     *  写箱ID后2字节
     */
    public static String writeBoxIdNextTwo="42";
    /**
     *  带小门的开门
     */
    public static String openDoorSmall="1C";
    /**
     *  带小门的检测箱门状态
     */
    public static String smallDoorState="1D";





    //读卡器通信协议
    //命令号
    /**
     * 广播设备信息
     */
    public static final String getDeviceInfo="00";
    /**
     * 读ROM信息
     */
    public static final  String readROM="0E";
    /**
     * 读ROM信息--通讯协议
     */
    public static final  String readROMCommunicationProtocol="04";


    /**
     * 写ROM信息
     */
    public static final String writeROM="8E";

    /**
     * 写ROM信息---设置Led灯时间
     */
    public static final String writeROMSetLedTime="05";

    /**
     * 指纹读卡器动作
     */
    public static final String cardOperation="0F";

    /**
     * 修改通信协议
     */
//    public static final String communication="04";

    //有效数据
    /**
     * 指纹读卡器动作---红灯闪烁
     */
    public static final String cardOperationRedLightTwinkle="05";
    /**
     * 指纹读卡器动作---红灯开
     */
    public static final String cardOperationRedOpen="01";

    /**
     * 指纹读卡器动作---红灯关
     */
    public static final String cardOperationRedClose="02";

    /**
     * 指纹读卡器动作---绿灯闪烁
     */
    public static final String cardOperationGreenLightTwinkle="06";

    /**
     * 指纹读卡器动作---绿灯开
     */
    public static final String cardOperationGreenOpen="03";

    /**
     * 指纹读卡器动作---绿灯关
     */
    public static final String cardOperationGreenClose="04";

    /**
     * 指纹读卡器动作---蜂鸣器响
     */
    public static final String cardOperationBuzzer="07";
    /**
     * 指纹读卡器动作---LED灯开关
     */
    public static final String cardOperationLedLight="08";
    /**
     * LED灯关
     */
    public static final String ledLightClose="00";




    /**
     * 自动读卡卡号
     */
    public static final String autoReadCard="0D";

    /**
     * 复位指纹模块
     */
    public static final String resestFinger="09";










}