package com.easycon.easycondroid.device;

import com.easycon.easycondroid.device.keys.SwitchButton;
import com.easycon.easycondroid.device.keys.SwitchHAT;
import com.easycon.easycondroid.device.keys.SwitchStick;

import java.util.Optional;

public class SwitchReport {
    public int button;
    public byte HAT;
    public byte LX;
    public byte LY;
    public byte RX;
    public byte RY;

    public SwitchReport() {
        reset();
    }

    public SwitchReport(SwitchButton button) {
        reset();
        this.button = button.getKeyCode();
    }

    public SwitchReport(SwitchHAT hat) {
        reset();
        this.HAT = (byte) hat.getKeyCode();
    }

    public static final SwitchReport RESET = new SwitchReport();

    public void reset() {
        button = 0;
        HAT = (byte) SwitchHAT.CENTER.getKeyCode();
        LX = SwitchStick.STICK_CENTER;
        LY = SwitchStick.STICK_CENTER;
        RX = SwitchStick.STICK_CENTER;
        RY = SwitchStick.STICK_CENTER;
    }

    public byte[] getBytes() {
        // Protocal packet structure:
        // bit 7 (highest):    0 = data byte, 1 = end flag
        // bit 6~0:            data (Big-Endian)
        // serialize data
        byte[] command = new byte[]{
                (byte) ((button >>> 8) & 0xFF),
                (byte) (button & 0xFF),
                HAT,
                LX,
                LY,
                RX,
                RY
        };
        byte[] packet = new byte[8];
        int i = 0;
        long n = 0;
        int bits = 0;
        for (byte b : command) {
            n = (n << 8) | (b & 0xFF);
            bits += 8;
            while (bits >= 7) {
                bits -= 7;
                packet[i++] = ((byte) (n >>> bits));
                n &= (1L << bits) - 1;
            }
        }
        packet[7] = (byte) (packet[7] | 0x80);
        return packet;
    }
}
