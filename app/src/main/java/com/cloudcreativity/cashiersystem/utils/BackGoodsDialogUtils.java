package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogBackGoodsBinding;

public class BackGoodsDialogUtils {

    private Dialog dialog;
    private ObservableField<String> number = new ObservableField<>();
    private ObservableField<String> money = new ObservableField<>();
    private OnOkListener onOkListener;

    private double initPrice = 0.0;

    public void show(Activity context,double initNum,double initPrice,OnOkListener onOkListener){
        this.onOkListener = onOkListener;
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LayoutDialogBackGoodsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_dialog_back_goods,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        int width = context.getResources().getDisplayMetrics().widthPixels;
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = width/3;
        dialog.show();
    }

    public void dismiss(){
        if(dialog!=null)
            dialog.dismiss();
    }

    public void onOk(){
        if(onOkListener==null)
            return;

    }

    public interface OnOkListener{
        void onOk(double num,double money);
    }

}
