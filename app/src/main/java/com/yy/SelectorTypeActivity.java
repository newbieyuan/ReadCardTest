package com.yy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yy.serialporttest.MainActivity;
import com.yy.serialporttest.R;
import com.yy.usbReadData.Usbctivity;

public class SelectorTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_type);
    }

    public void serialPortReadCard(View view) {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void usbReadCard(View view) {
        startActivity(new Intent(this, Usbctivity.class));
    }
}
