package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogInputBinding;

import java.util.Objects;

public class InputDialogUtils {
    private Dialog dialog;
    private OnOkListener onOkListener;
    public ObservableField<String> content = new ObservableField<>();
    public void show(Context context, String oldContent,OnOkListener onOkListener){
        this.onOkListener = onOkListener;
        this.content.set(oldContent);
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        LayoutDialogInputBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_dialog_input,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        assert window != null;
        window.getAttributes().gravity = Gravity.CENTER;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        window.getAttributes().width = widthPixels/3;
        dialog.show();
    }


    public void onOk(){

        if(onOkListener!=null){
            onOkListener.onOk(content.get());
        }
        SoftKeyboardUtils.notAutoFocus(Objects.requireNonNull(dialog.getWindow()));
        dialog.dismiss();
    }

    public void onCancel(){
        SoftKeyboardUtils.notAutoFocus(Objects.requireNonNull(dialog.getWindow()));
        dialog.dismiss();
    }

    public interface OnOkListener{
         void onOk(String content);
    }
}
