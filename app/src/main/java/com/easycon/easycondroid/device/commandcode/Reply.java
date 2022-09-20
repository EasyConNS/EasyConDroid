package com.easycon.easycondroid.device.commandcode;

public class Reply {
    public static final byte Error = (byte) 0x0;
    public static final byte Busy = (byte) 0xFE;
    public static final byte Ack = (byte) 0xFF;
    public static final byte Hello = (byte) 0x80;
    public static final byte FlashStart = (byte) 0x81;
    public static final byte FlashEnd = (byte) 0x82;
    public static final byte ScriptAck = (byte) 0x83;
}
