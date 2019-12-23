package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogVerifycodeBinding;
import com.cloudcreativity.cashiersystem.entity.ImgCode;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VerifyCodeDialogUtils {

    private Dialog dialog;
    public ObservableField<String> code = new ObservableField<>();
    private OnOkListener onOkListener;
    private Activity context;
    private LayoutDialogVerifycodeBinding binding;
    private ImgCode imgCode;
    private BaseDialogImpl baseDialog;
    public void show(Activity context, OnOkListener onOkListener, BaseDialogImpl baseDialog){
        this.onOkListener = onOkListener;
        this.baseDialog = baseDialog;
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
        this.loadImgCode();
    }

    public void dismiss(){
        if(dialog!=null)
            dialog.dismiss();
    }

    public void onRefresh(){
        //刷新验证码
        this.loadImgCode();
    }

    private void loadImgCode(){
        HttpUtils.getInstance().getImageCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        ImgCode imgCode = new Gson().fromJson(t, ImgCode.class);
                        if(imgCode!=null){
                            VerifyCodeDialogUtils.this.imgCode = imgCode;
                            byte[] decode = Base64.decode(imgCode.getImgCode(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                            binding.ivIdentity.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    public void onOk(){
        if(imgCode==null)
            return;
        if(!imgCode.getStrCode().equals(String.valueOf(binding.etCode.getText()))){
            ToastUtils.showShortToast(context,"图形验证码不正确");
            return;
        }
        if(onOkListener==null)
            return;
        onOkListener.onOk(imgCode.getStrCode());
        dismiss();
    }

    public interface OnOkListener{
        void onOk(String code);
    }
}
