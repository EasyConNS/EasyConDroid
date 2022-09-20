package com.easycon.easycondroid.device.keys;

public enum SwitchButton {
    Y(0x01),
    B(0x02),
    A(0x04),
    X(0x08),
    L(0x10),
    R(0x20),
    ZL(0x40),
    ZR(0x80),
    MINUS(0x100),
    PLUS(0x200),
    LCLICK(0x400),
    RCLICK(0x800),
    HOME(0x1000),
    CAPTURE(0x2000);

    public int getKeyCode() {
        return keyCode;
    }

    private int keyCode;

    SwitchButton(int keyCode) {
        this.keyCode = keyCode;
    }
}
