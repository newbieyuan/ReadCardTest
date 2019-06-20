package com.yy.usbReadData;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yy.serialporttest.R;

public class Usbctivity extends AppCompatActivity {
    private static final String ACTION_DEVICE_PERMISSION = "com.yy.serialporttest.USB_PERMISSION";
    private static final String TAG = Usbctivity.class.getSimpleName();
    private TextView tvMsg;
    private Button btnOpen,btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usbctivity);
        tvMsg = findViewById(R.id.tvMsg);
        USBHelper.getInstance().init(this, new USBHelper.OnUsbReadCardCallBack() {
            @Override
            public void readCard(String hexCard) {
                tvMsg.append(hexCard);
                tvMsg.append("\n\t");
            }

            @Override
            public void onUsbStateChange(boolean online) {
                if (online){
                    USBHelper.getInstance().startReadTask();
                }else {
                    USBHelper.getInstance().stopReadTask();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        USBHelper.getInstance().registerUsbReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        USBHelper.getInstance().unregisterUsbReceiver();
    }

    /**
     * 开启USB口监听
     * @param view
     */
    public void openDevice(View view) {
        USBHelper.getInstance().startReadTask();
    }

    public void closeDevice(View view) {
        USBHelper.getInstance().stopReadTask();
    }
}
