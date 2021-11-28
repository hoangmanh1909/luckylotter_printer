package com.mbl.lucklotterprinter.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String KEY_SHARE_PREFERENCES = "KEY_SHARE_PREFERENCES";
    public static final String KEY_EMPLOYEE = "KEY_EMPLOYEE";
    public static final String KEY_DATE_TIME_NOW = "KEY_DATE_TIME_NOW";
    public static final String KEY_DIFF_PRINT_SECOND = "KEY_DIFF_PRINT_SECOND";

    public static String TICKET_SHOW_AMOUNT = "TICKET_SHOW_AMOUNT";
    public static String TICKET_NO_AMOUNT = "TICKET_NO_AMOUNT";
    public static final String BLUETOOTH_NAME = "BLUETOOTH_NAME";
    public static final String PRODUCT_ID = "PRODUCT_ID";

    public static final String IMAGE_BEFORE = "IMAGE_BEFORE";
    public static final String IMAGE_AFTER = "IMAGE_AFTER";

    public static final String KENO_EVEN = "EVEN";
    public static final String KENO_ODD = "ODD";
    public static final String KENO_SMALL = "SMALL";
    public static final String KENO_BIG = "BIG";
    public static final String KENO_BIG_SMALL = "BIG_SMALL";
    public static final String KENO_EVEN_11_12 = "EVEN_11_12";
    public static final String KENO_EVEN_ODD = "EVEN_ODD";
    public static final String KENO_ODD_11_12 = "ODD_11_12";

    public static final int PRODUCT_NORMAL = 0;
    public static final int PRODUCT_MEGA = 1;
    public static final int PRODUCT_MAX4D = 2;
    public static final int PRODUCT_POWER = 3;
    public static final int PRODUCT_MAX3D = 4;
    public static final int PRODUCT_MAX3D_PLUS = 5;
    public static final int PRODUCT_KENO = 6;
    public static final int PRODUCT_MAX3D_PRO = 13;

    //x,1,l,y,m,M,<,u,k,N,q,t,d,F,i,Z
    //x,Z,l,y,m,M,<,u,k,N,q,t,d,F,i,Z
    public static final List<String> POSKeyArray = Arrays.asList("x", "Z", "l", "y", "m", "M", "<", "u", "k", "N", "q", "t", "d", "F", "i", "Z");
    public static final long SymbolSpecial =  1250;
    public static final long SymbolBase = 250;
    public static final long SymbolNumber = 180;//150;

    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
}
