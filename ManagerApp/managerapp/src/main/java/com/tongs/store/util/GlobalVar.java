package com.tongs.store.util;

/**
 * Created by jun on 15. 5. 15..
 */
public interface GlobalVar {

    public static final int PROTOCOL_STATUS_USER_CALL = 0;
    public static final int PROTOCOL_STATUS_GET_LIST = 1;

    public static final int PROTOCOL_STATUS_USER_REMOVE = 2;
    public static final int PROTOCOL_STATUS_USER_POP = 3;
    public static final int PROTOCOL_STATUS_USER_PUSH = 4;

    public static final int PROTOCOL_STATUS_MANAGER_LOGIN = 5;
    public static final int PROTOCOL_STATUS_MANAGER_LOGOUT = 6;
    public static final int PROTOCOL_STATUS_GET_GLOBAL_SET= 7;
    public static final int PROTOCOL_STATUS_SIGN_UP = 8;
    public static final int PROTOCOL_STATUS_GCM_INIT = 9;

    public static final String ISAUTOLOGIN = "autoLogin";
    public static final String ID = "id";
    public static final String PASSWORD = "password";
    public static final String COUPON_ID = "couponId";







    public static final String TOKEN = "token";
}
