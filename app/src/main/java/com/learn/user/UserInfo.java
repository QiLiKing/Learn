package com.learn.user;

import com.qlk.library.notice.util.DataConvert;

/**
 * Created by QiLiKing on 15/10/15.
 */
public class UserInfo
{
    private String userName;
    private String password;
    private String phone;

    public String getUserName()
    {
        return DataConvert.getSafeStr(userName);
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return DataConvert.getSafeStr(password);
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPhone()
    {
        return DataConvert.getSafeStr(phone);
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
