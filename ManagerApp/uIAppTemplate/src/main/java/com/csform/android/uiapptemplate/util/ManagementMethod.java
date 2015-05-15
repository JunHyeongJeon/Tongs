package com.csform.android.uiapptemplate.util;

/**
 * Created by jun on 15. 5. 15..
 */
public class ManagementMethod {


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
