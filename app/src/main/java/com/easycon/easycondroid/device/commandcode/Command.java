package com.easycon.easycondroid.device.commandcode;

public class Command {
    public static final byte Ready = (byte) 0xA5;
    public static final byte Debug = (byte) 0x80;
    public static final byte Hello = (byte) 0x81;
    public static final byte Flash = (byte) 0x82;
    public static final byte ScriptStart = (byte) 0x83;
    public static final byte ScriptStop = (byte) 0x84;
    public static final byte Version = (byte) 0x85;
    public static final byte LED = (byte) 0x86;
    public static final byte UnPair = (byte) 0x87;
    public static final byte ChangeControllerMode = (byte) 0x88;
    public static final byte ChangeControllerColor = (byte) 0x89;
    public static final byte SaveAmiibo = (byte) 0x90;
    public static final byte ChangeAmiiboIndex = (byte) 0x91;
}
