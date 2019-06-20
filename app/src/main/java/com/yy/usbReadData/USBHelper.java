package com.yy.usbReadData;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yy.keyboxlib.CharConversionUtil;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by YY on 2019/5/17.
 * USB设备帮助类
 */

public class USBHelper {
    private static final String ACTION_DEVICE_PERMISSION = "com.yy.serialporttest.USB_PERMISSION";
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private UsbManager mUsbManager;
    private UsbPermissionReceiver usbPermissionReceiver;
    private UsbDevice mUsbDevice;

    private static final int VENDOR_ID = 2520;
    private static final int PRODECT_ID = 1056;
    private UsbEndpoint usbEpOut;
    private UsbEndpoint usbEpIn;
    private UsbDeviceConnection mDeviceConnection;

    private OnUsbReadCardCallBack mOnReadCardCallBack;
    private Handler mHandler;
    private USBBroadcastReceiver mUSBBroadcastReceiver;

    /**
     * 是否是目标设备
     *
     * @param usbDevice
     * @return
     */
    private boolean isTargetDevice(UsbDevice usbDevice) {
        if (usbDevice == null) {
            return false;
        }
        if (VENDOR_ID == usbDevice.getVendorId() && PRODECT_ID == usbDevice.getProductId()) {
            return true;
        } else {
            return false;
        }
    }

    private USBHelper() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static USBHelper getInstance() {
        return USBHelperHolder.sInstance;
    }

    //静态内部类
    private static class USBHelperHolder {
        private static final USBHelper sInstance = new USBHelper();
    }

    /**
     * 初始化USB
     *
     * @param context
     */
    public USBHelper init(Context context, OnUsbReadCardCallBack onReadCardCallBack) {
        this.mContext = context.getApplicationContext();
        this.mOnReadCardCallBack = onReadCardCallBack;
        mUSBBroadcastReceiver = new USBBroadcastReceiver();
        return this;
    }

    //搜索usb设备
    private USBHelper searchUsb() {
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        usbPermissionReceiver = new UsbPermissionReceiver();
        HashMap<String, UsbDevice> deviceMap = mUsbManager.getDeviceList();
        Iterator<UsbDevice> iterator = deviceMap.values().iterator();
        while (iterator.hasNext()) {
            UsbDevice device = iterator.next();
            if (device.getVendorId() == VENDOR_ID && device.getProductId() == PRODECT_ID) {
                mUsbDevice = device;
                Log.w(TAG, "searchUsb: 找到设备");
                getUsbPermission(mUsbDevice);
            }
        }
        return this;
    }

    /**
     * 获取USB权限
     *
     * @param usbDevice
     */
    private void getUsbPermission(UsbDevice usbDevice) {
        if (!mUsbManager.hasPermission(usbDevice)) {
            Log.w(TAG, "getUsbPermission: 没有权限,申请权限");
            usbPermissionReceiver = new UsbPermissionReceiver();
            //申请权限
            Intent intent = new Intent(ACTION_DEVICE_PERMISSION);
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
            IntentFilter permissionFilter = new IntentFilter(ACTION_DEVICE_PERMISSION);
            mContext.registerReceiver(usbPermissionReceiver, permissionFilter);
            mUsbManager.requestPermission(usbDevice, mPermissionIntent);
        } else {
            Log.w(TAG, "getUsbPermission: 有权限");
            usbDeviceInit(usbDevice);
        }
    }

    /**
     * USB设备初始化,得到输入输出流
     *
     * @param device
     */
    private void usbDeviceInit(UsbDevice device) {
        Log.w(TAG, "usbDeviceInit: 已获取到权限");
        int interfaceCount = device.getInterfaceCount();
        UsbInterface usbInterface = null;
        for (int i = 0; i < interfaceCount; i++) {
            usbInterface = device.getInterface(i);
            if (usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
                break;
            }
        }
        if (usbInterface != null) {
            //获取UsbDeviceConnection
            mDeviceConnection = mUsbManager.openDevice(device);
            if (mDeviceConnection != null) {
                Log.w(TAG, "usbDeviceInit: 打开USB设备");
                if (mDeviceConnection.claimInterface(usbInterface, true)) {
                    for (int j = 0; j < usbInterface.getEndpointCount(); j++) {
                        UsbEndpoint endpoint = usbInterface.getEndpoint(j);
                        //类型为大块传输
                        if (endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                                usbEpOut = endpoint;
                            } else {
                                usbEpIn = endpoint;
                                Log.w(TAG, "usbDeviceInit: 找到输入流");
//                                mUsbEndpointInMap.put(device.getDeviceName(), endpoint);
                            }
                        }
                    }
                }
            }
        }
    }

    private int inMax;
    private ByteBuffer byteBuffer;
    private boolean isReading = true;
    private Date startDate;

    /**
     * 开启USB读卡任务
     */
    public boolean startReadTask() {
        searchUsb();
        startDate = new Date();
        if (mUsbDevice == null) {
            Log.w(TAG, "startReadTask: 找不到USB读卡器");
            return false;
        }
        if (mDeviceConnection == null) {
            Log.w(TAG, "startReadTask: 连接不上Usb读卡器");
            return false;
        }
        if (usbEpIn == null) {
            Log.w(TAG, "startReadTask: 找不到监听口");
            return false;
        }
        inMax = usbEpIn.getMaxPacketSize();
        byteBuffer = ByteBuffer.allocate(inMax);
        isReading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //读取数据2
                Log.w(TAG, "run: 开启读卡监听:"+isReading);
                while (isReading) {
                    synchronized (this) {
                        byte[] bytes = new byte[usbEpIn.getMaxPacketSize()];
                        int ret = mDeviceConnection.bulkTransfer(usbEpIn, bytes, bytes.length, 100);
                        Date returnDate = new Date();
                        if (returnDate.getTime() - startDate.getTime() > 100) {
                            if (ret > 0) {
                                //最终处理数据
                                byte[] data = delRedundantData(bytes);
                                final String hexString = CharConversionUtil.byte2HexString(data);
                                if (mOnReadCardCallBack != null) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.w(TAG, hexString);
                                            mOnReadCardCallBack.readCard(hexString);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }).start();
        return true;
    }

    /**
     * 停止USB读卡任务
     */
    public USBHelper stopReadTask() {
        isReading = false;
//        if (mDeviceConnection!=null){
//            mDeviceConnection.close();
//        }
        return this;
    }

    private byte[] delRedundantData(byte[] bytes) {
        int index = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            byte b = bytes[i];
            if (b != 0) {
                index = i + 1;
                break;
            }
        }
        byte[] data = new byte[index];
        System.arraycopy(bytes, 0, data, 0, data.length);
        return data;
    }

    private class UsbPermissionReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_DEVICE_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //授权成功,在这里进行打开设备操作
                        //获得了usb使用权限
                        usbDeviceInit(device);
                    } else {
                        //授权失败
                    }
                }
            }
        }
    }

    public void registerUsbReceiver() {
        mUSBBroadcastReceiver.registerUsbReceiver(mContext);
    }

    public void unregisterUsbReceiver() {
        mUSBBroadcastReceiver.unregisterUsbReceiver(mContext);
    }

    public class USBBroadcastReceiver extends BroadcastReceiver {

        public void registerUsbReceiver(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            context.registerReceiver(this, filter);
        }

        public void unregisterUsbReceiver(Context context) {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            //TODO 检查一个该device是否是我们的目标设备
            if (!isTargetDevice(device)) {
                return;
            }
            if (mOnReadCardCallBack != null) {
                if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                    mOnReadCardCallBack.onUsbStateChange(true);
                } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                    mOnReadCardCallBack.onUsbStateChange(false);
                }
            }
        }
    }

    public interface OnUsbReadCardCallBack {
        void readCard(String hexCard);

        void onUsbStateChange(boolean online);
    }
}
