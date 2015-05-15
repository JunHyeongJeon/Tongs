package com.csform.android.uiapptemplate.util;

/**
 * Created by jun on 15. 5. 15..
 */
public class ManagementMethod {

    public static final int PROTOCOL_STATUS_USER_ADD = 1;
    public static final int PROTOCOL_STATUS_GET_LIST = 2;
    public static final int PROTOCOL_STATUS_USER_CALL = 3;
    public static final int PROTOCOL_STATUS_USER_CANCLE = 4;
    public static final int PROTOCOL_STATUS_USER_LOGIN = 7;
    public static final int PROTOCOL_STATUS_USER_LOGOUT = 8;

    public static int mProtocolStatus;

    public static void setProtocolStatus(int status) {
        mProtocolStatus = status;
    }
    public static int getProtocolStatus() {
        return mProtocolStatus;
    }
    public static boolean isProtocolStatus(int status) {
        return mProtocolStatus == status ? true : false;
    }

}
