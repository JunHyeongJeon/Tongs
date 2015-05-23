package com.tongs.store.util;

/**
 * Created by jun on 15. 5. 15..
 */
public interface GlobalVar {
    public static final int PROTOCOL_STATUS_USER_CALL = 0;
    public static final int PROTOCOL_STATUS_USER_CANCEL = 1;
    public static final int PROTOCOL_STATUS_GET_LIST = 2;
    public static final int PROTOCOL_STATUS_USER_ADD = 3;

    public static final int PROTOCOL_STATUS_MANAGER_LOGIN = 7;
    public static final int PROTOCOL_STATUS_MANAGER_LOGOUT = 8;
    public static final int PROTOCOL_STATUS_GET_GLOBAL_SET=9;
    public static final int PROTOCOL_STATUS_SIGN_UP = 10;

    public static final String TOKEN = "token";
}
