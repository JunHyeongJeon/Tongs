package com.csform.android.managerapp.util;

/**
 * Created by jun on 15. 5. 15..
 */
public class ManagementMethod {

    static private ManagementMethod g_this;

    public int mProtocolStatus;

    public void setProtocolStatus(int status) {
        mProtocolStatus = status;
    }

    public int getProtocolStatus() {
        return mProtocolStatus;
    }
    public boolean isProtocolStatus(int status) {
        return mProtocolStatus == status ? true : false;
    }

    public static ManagementMethod getInstance()
    {
        if(g_this == null)
            g_this = new ManagementMethod();
        return g_this;
    }

}
