package com.yy.serialporttest;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yy.keyboxlib.ReadCardOperateType;
import com.yy.keyboxlib.SerialPort;
import com.yy.keyboxlib.SerialPortFinder;
import com.yy.keyboxlib.SpAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnPort, btnBaudRate,btnCoding;
    private EditText et_twinkle_num;
    private Context mContext;
    private AlertDialog dialog;
    private List<String> serialPortPathList;
    private List<String> codingFormatList;
    private List<String> serialPortBaudRateList;
    private String mSerialPortPath;
    private String mSerialPortBaudRate="19200";
    private StringBuffer sb;
    private TextView tvReceivedData;
    private Button btnOpenPort;
    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private Button btnClear, btnRedTwinkle, btnGreenTwinkle, btnReadAgreement, btnVoiceBuzzer,
            btnLedOpen, btnLedClose, btnReadDevice, btnCommunication03, btnNfc0,btn_resest_finger,
            btnRedOpen, btnGreenOpen,btnRedClose, btnGreenClose, btnLedSetTime;

    private static final String HEX = "HEX";
    private static final String ASCII = "ASCII";

    private String codingFormat = "HEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initDate();
    }

    /**
     * 准备数据
     */
    private void initDate() {
        try {
            serialPortPathList = SerialPortFinder.getSerialPortPathList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serialPortBaudRateList = new ArrayList<>();
        serialPortBaudRateList.add("50");
        serialPortBaudRateList.add("75");
        serialPortBaudRateList.add("110");
        serialPortBaudRateList.add("134");
        serialPortBaudRateList.add("150");
        serialPortBaudRateList.add("200");
        serialPortBaudRateList.add("300");
        serialPortBaudRateList.add("600");
        serialPortBaudRateList.add("1200");
        serialPortBaudRateList.add("1800");
        serialPortBaudRateList.add("2400");
        serialPortBaudRateList.add("4800");
        serialPortBaudRateList.add("9600");
        serialPortBaudRateList.add("19200");
        serialPortBaudRateList.add("38400");
        serialPortBaudRateList.add("57600");
        serialPortBaudRateList.add("115200");
        serialPortBaudRateList.add("230400");
        serialPortBaudRateList.add("460800");
        serialPortBaudRateList.add("500000");
        serialPortBaudRateList.add("576000");
        serialPortBaudRateList.add("921600");
        serialPortBaudRateList.add("1000000");
        serialPortBaudRateList.add("1152000");
        serialPortBaudRateList.add("1500000");
        serialPortBaudRateList.add("2000000");
        serialPortBaudRateList.add("2500000");
        serialPortBaudRateList.add("3000000");
        serialPortBaudRateList.add("3500000");

        sb = new StringBuffer();

        codingFormatList = new ArrayList<>();
        codingFormatList.add(HEX);
        codingFormatList.add(ASCII);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        btnPort = findViewById(R.id.btnPort);
        btnCoding = findViewById(R.id.btnCoding);
        btnCoding.setText(codingFormat);
        btnBaudRate = findViewById(R.id.btnBaudRate);
        btnOpenPort = findViewById(R.id.btnOpenPort);
        btnRedTwinkle = findViewById(R.id.btn_red_twinkle);
        btnGreenTwinkle = findViewById(R.id.btn_green_twinkle);
        btnReadAgreement = findViewById(R.id.btn_read_agreement);
        btnVoiceBuzzer = findViewById(R.id.btn_voice_buzzer);
        btnLedOpen = findViewById(R.id.btn_led_open);
        btnLedClose = findViewById(R.id.btn_led_close);
        btnReadDevice = findViewById(R.id.btn_read_device);
        btnCommunication03 = findViewById(R.id.btn_communication_03);
        btn_resest_finger = findViewById(R.id.btn_resest_finger);
        btnNfc0 = findViewById(R.id.btn_NFC_0);
        et_twinkle_num = findViewById(R.id.et_twinkle_num);
        btnRedClose = findViewById(R.id.btn_red_close);
        btnGreenClose = findViewById(R.id.btn_green_close);
        btnRedOpen = findViewById(R.id.btn_red_open);
        btnGreenOpen = findViewById(R.id.btn_green_open);
        btnLedSetTime = findViewById(R.id.btn_led_set_time);

        btnLedSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLedTime();
            }
        });
        btnRedOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redOpen();
            }
        });
        btnGreenOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greenOpen();
            }
        });

        btnRedClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redClose();
            }
        });
        btnGreenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greenClose();
            }
        });

        btnPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPort();
            }
        });

        btnCoding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCodingFormat();
            }
        });
        btnRedTwinkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redLightTwinkle();
            }
        });
        btnGreenTwinkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greenLightTwinkle();
            }
        });
        btnVoiceBuzzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceBuzzer();
            }
        });
        btnReadAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAgreement();
            }
        });
        btnLedOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledOpen();
            }
        });
        btnLedClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledClose();
            }
        });

        btnBaudRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBaudRate();
            }
        });
        btnReadDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceInfo();
            }
        });
        btnCommunication03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCommunication03();
            }
        });
        btnNfc0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNFC0();
            }
        });
        btn_resest_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resestFinger();
            }
        });

        btnOpenPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpenSerialPort) {
                    isRun =false;
                    //已打开串口，执行关闭串口操作
                    closeSerialPort();
                    isOpenSerialPort = false;
                    btnOpenPort.setText(getString(R.string.open_port));
//                    mReadThread.stop();
                } else {
                    //已关闭串口，执行打开串口操作
                    openSerialPort();
                }
            }
        });

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTheScreen();
            }
        });

        tvReceivedData = findViewById(R.id.tvReceivedData);
    }

    /**
     * 打开串口
     */
    private void openSerialPort() {
        String resString = "";
        try {
            if (TextUtils.isEmpty(mSerialPortPath)) {
                Toast.makeText(mContext, getResources().getString(R.string.error_configuration), Toast.LENGTH_SHORT).show();
                return;
            }
            mSerialPort = new SerialPort(mSerialPortPath, Integer.parseInt(mSerialPortBaudRate), 0);
            resString = getString(R.string.hint_open_port_success);

            isOpenSerialPort = true;
            btnOpenPort.setText(getString(R.string.close_port));
            mInputStream = mSerialPort.getInputStream();
            /* Create a receiving thread */
            isRun =true;
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (IOException e) {
            resString = getString(R.string.hint_open_port_fail);
            Toast.makeText(mContext, getResources().getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            resString = getString(R.string.hint_open_port_fail);
            Toast.makeText(mContext, getResources().getString(R.string.error_security), Toast.LENGTH_SHORT).show();
        }
        writePromptMessage(resString);
    }

    /**
     * 关闭串口
     */
    private void closeSerialPort() {
        mSerialPort.close();
        String resString = getString(R.string.hint_close_port);
        writePromptMessage(resString);
    }

    private boolean isOpenSerialPort = false;

    /**
     * 选择串口号
     * Select serial number
     */
    private void setPort() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_dialog, null);
        builder.setView(v);
        ListView lv = (ListView) v.findViewById(R.id.lvDialog);
        SpAdapter adapter = new SpAdapter(serialPortPathList, mContext);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSerialPortPath = serialPortPathList.get(i);
                btnPort.setText(mSerialPortPath);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 选择字符编码格式
     * Select serial number
     */
    private void setCodingFormat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_dialog, null);
        builder.setView(v);
        ListView lv = (ListView) v.findViewById(R.id.lvDialog);
        SpAdapter adapter = new SpAdapter(codingFormatList, mContext);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                codingFormat = codingFormatList.get(i);
                btnCoding.setText(codingFormat);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 选择波特率
     * Select serial number
     */
    private void setBaudRate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_dialog, null);
        builder.setView(v);
        ListView lv = (ListView) v.findViewById(R.id.lvDialog);
        SpAdapter adapter = new SpAdapter(serialPortBaudRateList, mContext);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSerialPortBaudRate = serialPortBaudRateList.get(i);
                btnBaudRate.setText(mSerialPortBaudRate);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 写入提示信息
     * Write prompt information
     */
    private void writePromptMessage(String cardString) {
        sb.append(cardString + "\t\t\t\t\t\t\t\t");

        String date = format.format(new Date());
        sb.append(date + "\t\n");
        String dataString = sb.toString();
        tvReceivedData.setText(dataString);
    }

    /**
     * 清屏
     * Clear the screen
     */
    private void clearTheScreen() {
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.setLength(0);
        tvReceivedData.setText(sb.toString());
    }

    boolean isRun = true;
    private boolean isRead = true;

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isRun) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    if (mInputStream.available() > 0) {
                        size = mInputStream.read(buffer);
                        if (size > 0) {
//                            Log.e("接收到的原始数据大小--", size+"");
                            byte[] date = new byte[size];
                            System.arraycopy(buffer, 0, date, 0, size);
                            onDataReceived(date, size);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private String msg = "";
    private void onDataReceived(byte[] buffer, int size) {
        String dataFirst = String.format("%02X", buffer[0]).trim();
        String dataLast = String.format("%02X", buffer[buffer.length - 1]).trim();
        Log.e("接收到的原始数据--", byte2hex(buffer));
        if ("55".equals(dataFirst) && "AA".equals(dataLast) && buffer.length > 10) {
            //加校验的卡号，校验
            checkCard(buffer);
        } else {
            if (codingFormat.equals(HEX)){
                msg = byte2hex(buffer);
            }else if (codingFormat.equals(ASCII)){
                try {
                    msg = new String(buffer,"ascii");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    msg = "格式错误";
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    writePromptMessage("222======"+msg);
                }
            });
        }
    }

//    private StringBuffer deviceType=new StringBuffer();//设备类型
//    private StringBuffer deviceNo=new StringBuffer();//设备编号

    private String deviceType;//设备类型
    private String deviceNo;
    boolean isClose=true;
    /**
     * 带CRC8校验的卡号
     * 数据头（1字节）+数据包长度（1字节）+设备类型（2字节）+设备编号（2字节）+命令号（1字节）+包号（2字节）+有效数据（>32字节）+校验（1字节）+数据尾（1字节）
     * 55               10                     03 00          xx xx                 0d           00 00        1字节卡类型+卡号       xx            AA
     */
    private boolean greenLight = false;
    private void checkCard(final byte[] buffer) {
        try {
//            Log.e("接收到的--checkCard", byte2hex(buffer));
            int returnCrc = buffer[buffer.length - 2] & 0xff;
            byte[] data = new byte[buffer.length - 3];//除去数据头、数据尾、校验
            System.arraycopy(buffer, 1, data, 0, data.length);
            int checkCrc = FindCRC(data);
            if (returnCrc == checkCrc) {
                int dataLength = buffer[1] & 0xff;
                Log.e("接收到的--命令号=", padLeft(Integer.toHexString(buffer[6] & 0xFF),2));
                switch (padLeft(Integer.toHexString(buffer[6] & 0xFF),2)){
                    case ReadCardOperateType.getDeviceInfo://广播设备信息
                        byte[] deviceTypeByte = new byte[2];
                        System.arraycopy(buffer, 2, deviceTypeByte, 0, deviceTypeByte.length);
                        deviceType=byte2hex(deviceTypeByte);
                        byte[] deviceNoByte = new byte[2];
                        System.arraycopy(buffer, 4, deviceNoByte, 0, deviceNoByte.length);
                        deviceNo=byte2hex(deviceNoByte);
                        Log.e("接收到的--设备类型", deviceType);
                        Log.e("接收到的--设备编号", deviceNo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                writePromptMessage(byte2hex(buffer));
                            }
                        });
//                        ledClose();
                        break;
                    case ReadCardOperateType.cardOperation://指纹读卡器动作
                        Log.e("接收到的--指纹读卡器动作",padLeft(Integer.toHexString(buffer[9] & 0xFF),2));
//                        switch (padLeft(Integer.toHexString(buffer[9] & 0xFF),2)){
//                            case ReadCardOperateType.cardOperationLedLight://LED灯开关
//                                if (isClose){
//                                    isClose=false;
//                                    if (greenLight){
//                                        greenLightTwinkle();
//                                        greenLight = false;
//                                    }else {
//                                        redLightTwinkle();
//                                        greenLight = true;
//                                    }
//                                }else {
//                                    isClose=true;
//                                }
//                                break;
//                            case ReadCardOperateType.cardOperationRedLightTwinkle://红灯闪烁
//                                ledOpen();
//                                break;
//                            case ReadCardOperateType.cardOperationGreenLightTwinkle://绿灯闪烁
//                                ledOpen();
//                                break;
//                            case ReadCardOperateType.resestFinger://
//                                Log.e("resestFinger==","复位返回");
//                                break;
//                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                writePromptMessage("指纹读卡器动作=="+byte2hex(buffer));
                            }
                        });
                        break;
                    case ReadCardOperateType.autoReadCard://自动读卡卡号
                        byte[] card = new byte[dataLength - 1 - 1 - 2 - 2 - 1 - 2 - 1 - 1 - 1];
                        System.arraycopy(buffer, 10, card, 0, card.length);
                        final String cardString = byte2hex(card);
                        Log.e("checkCard", byte2hex(card));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                writePromptMessage("自动读卡卡号"+cardString);
//                                lighting(false);
                            }
                        });
                        break;
                }
            } else {
                Log.e("checkCard", "校验未通过----校验值：" + checkCrc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isRight;
    public void lighting(boolean isRight){
        this.isRight=isRight;
        if (TextUtils.isEmpty(deviceNo) || TextUtils.isEmpty(deviceType)){
            getDeviceInfo();
        }else {
            ledClose();
        }
    }

    /**
     *  CRC8校验
     * @param data
     * @return
     */
    public int FindCRC(byte[] data) {
        int CRC = 0;
        int genPoly = 0x8C;
        for (int i = 0; i < data.length; i++) {
            CRC ^= data[i];
            CRC &= 0xff;//保证CRC余码输出为1字节。
            for (int j = 0; j < 8; j++) {
                if ((CRC & 0x01) != 0) {
                    CRC = (CRC >> 1) ^ genPoly;
                    CRC &= 0xff;//保证CRC余码输出为1字节。
                } else {
                    CRC >>= 1;
                }
            }
        }
        CRC &= 0xff;//保证CRC余码输出为1字节。
        return CRC;
    }

    public static final String ascii2String(byte[] data) {
        StringBuffer tStringBuf = new StringBuffer();
        byte[] tBytes = new byte[]{0x31, 0x32, 0x33};  //实际上是ascii码字符串"123"
        char[] tChars = new char[data.length];

        for (int i = 0; i < data.length; i++)
            tChars[i] = (char) data[i];
        tStringBuf.append(tChars);         //nRcvString从tBytes转成了String类型的"123
        return tStringBuf.toString();
    }

    /**
     * 将byte数组化为十六进制串
     */

    public static final String byte2hex(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            stringBuilder.append(String.format("%02X ", byteChar).trim());
        }
        return stringBuilder.toString();
    }

    /**
     * 红灯关
     */
    private void redClose() {
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationRedClose);
    }
    /**
     * 红灯开
     */
    private void redOpen() {
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationRedOpen);
    }
    /**
     *
     * 绿灯开
     */
    private void greenOpen() {
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationGreenOpen);
    }
    /**
     *
     * 绿灯关
     */
    private void greenClose() {
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationGreenClose);
    }

    /**
     * 红灯闪烁3次
     */
    private void redLightTwinkle(){
        Log.e("命令--","红灯闪烁3次");
        String num = et_twinkle_num.getText().toString().trim();
        if (TextUtils.isEmpty(num)){
            return;
        }
        if (num.length()<2){
            num = "0"+num;
        }
//        isRead=false;
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationRedLightTwinkle + num);
    }
    /**
     * 绿灯闪烁3次
     */
    private void greenLightTwinkle(){
        Log.e("命令--","绿灯闪烁3次");
//        isRead=false;
        String num = et_twinkle_num.getText().toString().trim();
        if (TextUtils.isEmpty(num)){
            return;
        }
        if (num.length()<2){
            num = "0"+num;
        }
        boolean result=sendOperation(deviceType,deviceNo
                ,ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationGreenLightTwinkle + num);
    }

    /**
     * 蜂鸣器响一声
     */
    private void voiceBuzzer(){
        Log.e("命令--","蜂鸣器响一声");
//        isRead=false;
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationBuzzer+"01");
//        if (result){
//            writePromptMessage("蜂鸣器响一声成功");
//        }else {
//            writePromptMessage("蜂鸣器响一声失败");
//        }
//        isRead=true;
    }
    /**
     * LED打开
     */
    private void ledOpen(){
        Log.e("命令--","LED打开");
//        isRead=false;
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationLedLight+"01");
//        if (result){
//            writePromptMessage("LED打开成功");
//        }else {
//            writePromptMessage("LED打开失败");
//        }
//        isRead=true;
    }

    /**
     *
     * 设置Led等时间
     */
    private void setLedTime() {
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.writeROM,ReadCardOperateType.writeROMSetLedTime+"6400C800");
    }

    /**
     * 复位指纹模块
     */
    private void resestFinger() {
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.resestFinger+"0100");
        if (result){
//            writePromptMessage("复位指纹模块成功");
        }else {
            writePromptMessage("复位指纹模块失败");
        }
    }


    /**
     * LED关闭
     */
    private void ledClose(){
        Log.e("命令--","LED关闭");
//        isRead=false;
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.cardOperation,ReadCardOperateType.cardOperationLedLight + ReadCardOperateType.ledLightClose);
//        if (result){
//            writePromptMessage("LED关闭成功");
//        }else {
//            writePromptMessage("LED关闭失败");
//        }
//        isRead=true;
    }
    /**
     * 读通讯协议
     */
    private void readAgreement(){
        Log.e("命令--","读通讯协议");
//        isRead=false;
        boolean result=sendOperation(deviceType,deviceNo,
                ReadCardOperateType.readROM,"04");
//        if (result){
//            writePromptMessage("读通讯协议成功");
//        }else {
//            writePromptMessage("读通讯协议失败");
//        }
//        isRead=true;
    }

    /**
     * 广播设备信息
     */
    private void getDeviceInfo(){
        Log.e("命令--","广播设备信息");
//        isRead=false;
        boolean result=sendOperation("0000","0000",ReadCardOperateType.getDeviceInfo,"");
//        if (result){
//            writePromptMessage("广播设备信息成功");
//        }else {
//            writePromptMessage("广播设备信息失败");
//        }
//        isRead=true;
    }
    /**
     * 修改通信协议
     */
    private void getCommunication03(){
        Log.e("命令--","广播设备信息");
//        isRead=false;
        boolean result=sendOperation(deviceType.toString(),deviceNo.toString(),ReadCardOperateType.writeROM,"0403");
//        if (result){
//            writePromptMessage("修改通信协议成功");
//        }else {
//            writePromptMessage("修改通信协议失败");
//        }
//        isRead=true;
    }

    /**
     * NFC读卡次数设置成0
     */
    private void setNFC0(){
        Log.e("命令--","广播设备信息");
//        isRead=false;
        boolean result=sendOperation(deviceType.toString(),deviceNo.toString(),ReadCardOperateType.writeROM,"0600");
        if (result){
            writePromptMessage("修改通信协议成功");
        }else {
            writePromptMessage("修改通信协议失败");
        }
//        isRead=true;
    }

    /**
     *
     * @param deviceType 设备类型
     * @param deviceNo 设备编号
     * @param operationType 命令号
     * @param data 有效数据  "0012"
     */
    public boolean sendOperation(String deviceType,String deviceNo,String operationType,String data){
        if (TextUtils.isEmpty(deviceType) || TextUtils.isEmpty(deviceNo)){
            Toast.makeText(this,"请先获取设备类型和编号",Toast.LENGTH_SHORT).show();
            return false;
        }

        String baoHao="0000";//包号，暂时写死
        int N = data.length() / 2;//有效数据字节长度，data必须为16进制
        byte[] bytesSend = new byte[11 + N];//11+n
        //发送协议中，发送的内容为11+n字节
        byte[] check = new byte[8 + N];//8+n
        int checkpos = 0;
        //1数据包长度
        check[checkpos] = (byte) Integer.parseInt(Integer.toHexString(11 + N), 16);
        //2设备类型
        checkpos++;
        check[checkpos] = (byte) Integer.parseInt(deviceType.substring(0, 2), 16);
        checkpos++;
        check[checkpos] = (byte) Integer.parseInt(deviceType.substring(2, 4), 16);
        //2设备编号
        checkpos++;
        check[checkpos] = (byte) Integer.parseInt(deviceNo.substring(0, 2), 16);
        checkpos++;
        check[checkpos] = (byte) Integer.parseInt(deviceNo.substring(2, 4), 16);
        //1命令号
        checkpos++;
        check[checkpos] = (byte) Integer.parseInt(operationType, 16);
        //2包号
        checkpos++;
        check[checkpos] = (byte) Integer.parseInt(baoHao.substring(0, 2), 16);
        checkpos++;
        check[checkpos] = (byte) Integer.parseInt(baoHao.substring(2, 4), 16);
        //N有效数据
        for (int i = 0; i < N; i++) {
            checkpos++;
            check[checkpos] = (byte) Integer.parseInt(data.substring(i * 2, i * 2 + 2), 16);
        }
        // 55 0D 03 00 FF FF 0F 00 00 06 03 79 AA
//		发送：0x55 + 1数据包长度 + 2设备类型 + 2设备编号 + 1命令号 + 2包号 + N有效数据 + 1校验 + 0xAA
        int i_Pos = 0;
        //数据头
        bytesSend[i_Pos] = 0x55;
        System.arraycopy(check, 0, bytesSend, 1, check.length);//复制校验的数据（源数组，源数组要复制的起始位置，目的数组，目的数组放置的起始位置，复制的长度）
        i_Pos = check.length;
        //1校验
        i_Pos++;
        bytesSend[i_Pos] = (byte) Integer.parseInt(Integer.toHexString(FindCRC(check)), 16);
        i_Pos++;
        //数据尾
        bytesSend[i_Pos] = (byte) 0xAA;
        Log.e("发送数据",bytesToHexString(bytesSend,bytesSend.length));
        boolean f_Result=serialportWrite(bytesSend);
        Log.e("发送结果",f_Result+"");
//        f_Result = serialportRead();
        return f_Result;
    }


    /**
     * 串口数据发送
     * @param bytesSend 要发送的byte数组
     * @return 发送成功返回true，错误返回false
     */
    private boolean serialportWrite(byte[] bytesSend){
        OutputStream mOutputStream=mSerialPort.getOutputStream();
        try {
            mOutputStream.write(bytesSend);
            mOutputStream.flush();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 存储接收到的数据
     */
    private byte[] _bytesGet=null;
    /**

     /**
     * 串口数据接收，固定读取18字节内容
     * @return 数据接收成功返回true，错误返回false
     */
    public boolean serialportRead(){
//        long aaaaaa=System.currentTimeMillis();
        boolean f_Result = false;
        long timeOld = System.currentTimeMillis();
        InputStream mInputStream = mSerialPort.getInputStream();
        int bytesRequire = 18,bytesReceived = 0;
        _bytesGet = new byte[bytesRequire];
        while((System.currentTimeMillis() - timeOld) < 1000){
            try {
                if(mInputStream.available() > 0){
                    int i_Pos = 0;
                    try {
                        i_Pos = mInputStream.read(_bytesGet, bytesReceived, (bytesRequire - bytesReceived));
                        bytesReceived += i_Pos;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Log.e("串口数据接收异常11=",e+"");
                        break;
                    }
                    if(bytesReceived >= bytesRequire){
                        f_Result = true;
                        break;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("串口数据接收异常2222=",e+"");
                break;
            }
            if(bytesReceived >= bytesRequire) {
                f_Result = true;
                break;
            }
        }
        Log.e("串口数据接收f_Result=",f_Result+"");
        Log.e("接收到的--checkCard", byte2hex(_bytesGet));
        return f_Result;
    }

    private String bytesToHexString(byte[] bArray, int size) {
        if (bArray==null){
            return "";
        }
        StringBuffer strBuf = new StringBuffer(bArray.length);
        String strTemp;
        for(int i = 0; i < size; i++) {
            strTemp = Integer.toHexString(0xFF & bArray[i]);
            if(strTemp.length() < 2) {
                strBuf.append(0);
            }
            strBuf.append(strTemp.toUpperCase());
        }
        return strBuf.toString();
    }

    /**
     * 字符串左补位
     * @param str1，要补位的字符串
     * @param len，补位长度
     * @return 补位完的String类型字符串
     */
    private String padLeft(String str1,int len)
    {
        String result=str1;
        for (int i = 0; i < (len-str1.length()); i++) {
            result="0"+result;
        }
        return result.toUpperCase();
    }





}
