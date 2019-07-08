package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogUserAuthErrorBinding;
import com.cloudcreativity.cashiersystem.receiver.MyBusinessReceiver;

/**
 * 用户权限出错对话框
 */
public class AuthDialogUtils {
    private Dialog dialog;


    public void show(Context context){
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LayoutDialogUserAuthErrorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_dialog_user_auth_error,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/3;
        dialog.show();
    }


    public void onCancelClick(View view){
        dialog.dismiss();
        dialog = null;
        Intent intent = new Intent(MyBusinessReceiver.ACTION_EXIT_APP);
        view.getContext().sendBroadcast(intent);

    }

    public void onLoginClick(View view){
        //重新登录
        dialog.dismiss();
        dialog = null;
        Intent intent = new Intent(MyBusinessReceiver.ACTION_LOGOUT);
        view.getContext().sendBroadcast(intent);
    }
}
