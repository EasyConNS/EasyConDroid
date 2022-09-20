package com.easycon.easycondroid.device.keys;

public enum SwitchHAT {
    TOP(0x00),
    TOP_RIGHT(0x01),
    RIGHT(0x02),
    BOTTOM_RIGHT(0x03),
    BOTTOM(0x04),
    BOTTOM_LEFT(0x05),
    LEFT(0x06),
    TOP_LEFT(0x07),
    CENTER(0x08);

    public int getKeyCode() {
        return keyCode;
    }

    private int keyCode;

    SwitchHAT(int keyCode) {
        this.keyCode = keyCode;
    }
}
