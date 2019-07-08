package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutOpenOrderFinalBinding;

public class OpenOrderFinalDialog {
    private Dialog dialog;
    public void show(Activity context){
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LayoutOpenOrderFinalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_open_order_final,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
//        int width = context.getResources().getDisplayMetrics().widthPixels;
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.END);
        //window.getAttributes().width = width/3;
        dialog.show();
    }

    public void onBack(){
        if(dialog!=null)
            dialog.dismiss();
    }
}
