package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ImageView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogVerifycodeBinding;

public class VerifyCodeDialogUtils {

    private Dialog dialog;
    public ObservableField<String> code = new ObservableField<>();
    private OnOkListener onOkListener;
    private Activity context;
    private LayoutDialogVerifycodeBinding binding;
    public void show(Activity context,String code,OnOkListener onOkListener){
        this.onOkListener = onOkListener;
        this.context = context;
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_dialog_verifycode,null,false);
        binding.setUtils(this);
        //binding.ivIdentity.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
        dialog.setContentView(binding.getRoot());
        int width = context.getResources().getDisplayMetrics().widthPixels;
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = width/3;
        dialog.show();
        GlideUtils.load(binding.ivIdentity.getContext(),APIService.VERIFY_CODE+"?test="+System.currentTimeMillis(),binding.ivIdentity);
    }

    public void dismiss(){
        if(dialog!=null)
            dialog.dismiss();
    }

    public void onRefresh(){
        //刷新验证码
        GlideUtils.load(binding.ivIdentity.getContext(),APIService.VERIFY_CODE+"?test="+System.currentTimeMillis(),binding.ivIdentity);
    }

    public void onOk(){
        if(onOkListener==null)
            return;
        dismiss();
    }

    public interface OnOkListener{
        void onOk(String code);
    }
}
