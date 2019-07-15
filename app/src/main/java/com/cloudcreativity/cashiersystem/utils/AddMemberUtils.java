package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.RadioGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogAddMemberBinding;

public class AddMemberUtils {
    private Dialog dialog;
    private static final int FAMILY = 1;
    private static final int VIP = 2;
    private OnOkListener onOkListener;
    private Context context;
    public ObservableField<String> mobile = new ObservableField<>();
    private int type = FAMILY;
    public void show(Context context, OnOkListener onOkListener){
        this.onOkListener = onOkListener;
        this.context = context;
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        LayoutDialogAddMemberBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_dialog_add_member,null,false);
        binding.setUtils(this);
        binding.rgIdentity.check(R.id.rb_family);
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        assert window != null;
        window.getAttributes().gravity = Gravity.CENTER;
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        window.getAttributes().width = widthPixels/3;
        SoftKeyboardUtils.notAutoFocus(window);
        dialog.show();
    }

    public void onCheckChange(RadioGroup rg,int checkId){
        switch (checkId){
            case R.id.rb_family:
                type = FAMILY;
                break;
            case R.id.rb_vip:
                type = VIP;
                break;
        }
    }

    public void onOk(){
        if(mobile.get()==null||!StrUtils.isPhone(mobile.get())){
            ToastUtils.showShortToast(context,"手机号不正确");
            return;
        }
        if(type==0){
            ToastUtils.showShortToast(context,"请选择会员类型");
            return;
        }
        if(onOkListener!=null){
            onOkListener.onOk(mobile.get(),type);
        }
        dialog.dismiss();
    }

    public void onCancel(){
        dialog.dismiss();
    }

    public interface OnOkListener{
         void onOk(String mobile,int type);
    }
}
