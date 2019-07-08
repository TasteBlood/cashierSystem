package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogRechargeBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RechargeDialogUtils {
    private Dialog dialog;
    private OnChooseListener chooseListener;
    private RadioButton lastCheck = null;
    public void show(Activity context,OnChooseListener onChooseListener){
        this.chooseListener = onChooseListener;
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LayoutDialogRechargeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_dialog_recharge,null,false);
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
        if(this.chooseListener!=null){
            if(this.lastCheck!=null){
                String s = this.lastCheck.getText().toString();
                String reg = "(\\d)\\D$";
                Pattern compile = Pattern.compile(reg);
                Matcher matcher = compile.matcher(s);
                if(matcher.find()){
                    this.chooseListener.onChoose(Integer.parseInt(matcher.group(1)));
                }

            }

        }
        dismiss();

    }

    public interface OnChooseListener{
        void onChoose(int money);
    }

    public void onClick(View v){
        if(this.lastCheck!=null&&this.lastCheck.isChecked())
            lastCheck.setChecked(false);
        switch (v.getId()){
            case R.id.rb_200:
                lastCheck = (RadioButton) v;
                break;
            case R.id.rb_300:
                lastCheck = (RadioButton) v;
                break;
            case R.id.rb_400:
                lastCheck = (RadioButton) v;
                break;
            case R.id.rb_500:
                lastCheck = (RadioButton) v;
                break;
            case R.id.rb_600:
                lastCheck = (RadioButton) v;
                break;
            case R.id.rb_700:
                lastCheck = (RadioButton) v;
                break;
            case R.id.rb_800:
                lastCheck = (RadioButton) v;
                break;
            case R.id.rb_1000:
                lastCheck = (RadioButton) v;
                break;
        }
    }
}
