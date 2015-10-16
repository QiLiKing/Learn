package com.qlk.library.notice.util;

/**
 * Created by QiLiKing on 15/10/15.
 */
public class DataConvert
{

    public static String getSafeStr(String msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return msg;
    }
}
