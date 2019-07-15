package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.EditText;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogRechargeBinding;

public class RechargeDialogUtils{
    private Dialog dialog;
    private ViewStub stepOne;
    private ViewStub stepTwo;
    private ViewStub stepThree;
    public void show(Activity context){
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LayoutDialogRechargeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_dialog_recharge,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        initStepOne(binding.getRoot());
        int width = context.getResources().getDisplayMetrics().widthPixels;
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = width/3;
        dialog.show();
    }

    private void initStepOne(final View content){
        stepOne = content.findViewById(R.id.stepOne);
        stepOne.inflate();

        content.findViewById(R.id.btn_cancel_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        content.findViewById(R.id.btn_ok_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_money = content.findViewById(R.id.et_money);
                String money = et_money.getText().toString();
                if(TextUtils.isEmpty(money))
                    return;
                if(Integer.parseInt(money)<200)
                    return;
                stepOne.setVisibility(View.GONE);
                SoftKeyboardUtils.hideSoftInput(et_money);
                initStepTwo(content);
            }
        });
    }

    private void initStepTwo(final View content){
        stepTwo = content.findViewById(R.id.stepTwo);
        stepTwo.inflate();
        content.findViewById(R.id.btn_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTwo.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        content.findViewById(R.id.btn_mobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTwo.setVisibility(View.GONE);
                initStepThree(content);
            }
        });

        content.findViewById(R.id.btn_cancel_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void initStepThree(final View content){
        stepThree = content.findViewById(R.id.stepThree);
        stepThree.inflate();
        content.findViewById(R.id.btn_cancel_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
