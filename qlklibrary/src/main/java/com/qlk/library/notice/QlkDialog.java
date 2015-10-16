package com.qlk.library.notice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

/**
 * Created by QiLiKing on 15/10/15.
 */
public class QlkDialog
{
    public static Dialog showProgressDialog(Context context, String prompt)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        if (!TextUtils.isEmpty(prompt))
        {
            progressDialog.setMessage(prompt);
        }
        return progressDialog;
    }


}
