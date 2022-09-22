package com.easycon.easycondroid;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.easycon.easycondroid.device.SwitchReport;
import com.easycon.easycondroid.device.commandcode.Command;
import com.easycon.easycondroid.device.commandcode.Reply;
import com.easycon.easycondroid.device.keys.SwitchButton;
import com.easycon.easycondroid.device.keys.SwitchHAT;
import com.easycon.easycondroid.util.Bytes;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    boolean connected = false;
    UsbSerialPort port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) {
        if (connected) {
            try {
                port.close();
                connected = false;
                Button btn = findViewById(R.id.connectButton);
                btn.setText("连接");
                Toast.makeText(this, "关闭端口成功", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "关闭端口失败", Toast.LENGTH_LONG).show();
            }
        } else {
            // Find all available drivers from attached devices.
            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            if (availableDrivers.isEmpty()) {
                Toast.makeText(this, "寻找设备失败", Toast.LENGTH_LONG).show();
                return;
            }

            // Open a connection to the first available driver.
            UsbSerialDriver driver = availableDrivers.get(0);
            PendingIntent permissionIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.android.example.USB_PERMISSION"), PendingIntent.FLAG_IMMUTABLE);
            } else {
                permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.android.example.USB_PERMISSION"), PendingIntent.FLAG_ONE_SHOT);
            }

            manager.requestPermission(driver.getDevice(), permissionIntent);
            UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
            if (connection == null) {
                // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
                Toast.makeText(this, "连接端口失败", Toast.LENGTH_LONG).show();
                return;
            }

            port = driver.getPorts().get(0); // Most devices have just one port (port 0)
            try {
                port.open(connection);
            } catch (Exception ex) {
                Toast.makeText(this, "打开端口失败", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (Exception ex) {
                Toast.makeText(this, "设置串口参数失败", Toast.LENGTH_LONG).show();
                return;
            }
            //Toast.makeText(this, "打开端口成功", Toast.LENGTH_SHORT).show();
            connected = true;
            Button btn = findViewById(R.id.connectButton);
            btn.setText("关闭");
            init();
        }

    }

    public void init() {
        eatVerbose();
        byte[] send = send(new byte[]{Command.Ready, Command.Ready, Command.Hello});
        if (!Arrays.equals(send, new byte[]{Reply.Hello})) {
            Toast.makeText(this, "easycon协议沟通失败" + Bytes.prettyPrint(send), Toast.LENGTH_LONG).show();
        }
    }

    public void up(View view) {
        send(new SwitchReport(SwitchHAT.TOP).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void down(View view) {
        send(new SwitchReport(SwitchHAT.BOTTOM).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void left(View view) {
        send(new SwitchReport(SwitchHAT.LEFT).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void right(View view) {
        send(new SwitchReport(SwitchHAT.RIGHT).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void A(View view) {
        send(new SwitchReport(SwitchButton.A).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void B(View view) {
        send(new SwitchReport(SwitchButton.B).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void X(View view) {
        send(new SwitchReport(SwitchButton.X).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void Y(View view) {
        send(new SwitchReport(SwitchButton.Y).getBytes());
        sleep(50);
        send(SwitchReport.RESET.getBytes());
    }

    public void sleep(long timeMilli) {
        try {
            Thread.sleep(timeMilli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void eatVerbose() {
        if (connected) {
            try {
                byte[] buffer = new byte[1024];
                int len = port.read(buffer, 1000);
            } catch (IOException e) {
                Toast.makeText(this, "eatVerbose失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "串口未连接", Toast.LENGTH_LONG).show();
        }
    }

    public synchronized byte[] send(byte[] bytes) {
        if (connected) {
            try {
                port.write(bytes, 500);
                byte[] buffer = new byte[255];
                int len = port.read(buffer, 500);
                return Arrays.copyOfRange(buffer, 0, len);
            } catch (IOException e) {
                Toast.makeText(this, "串口发送" + Bytes.prettyPrint(bytes) + "失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "串口未连接", Toast.LENGTH_LONG).show();
        }
        return new byte[0];
    }

}