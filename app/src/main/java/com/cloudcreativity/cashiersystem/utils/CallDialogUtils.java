package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.CompoundButton;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogCallBinding;

import java.util.Objects;

public class CallDialogUtils {
    private Dialog dialog;
    public ObservableField<String> finalWeight = new ObservableField<>();
    public ObservableField<String> unit = new ObservableField<>();
    public ObservableField<String> goodsName = new ObservableField<>();
    private SerialPortUtils serialPortUtils;
    private Handler handler;
    private byte[] mBuffer;
    private LayoutDialogCallBinding binding;
    private OnOkListener onOkListener;
    public void show(Context context,OnOkListener onOkListener,String unit,String name){
        this.onOkListener = onOkListener;
        this.unit.set(unit);
        this.goodsName.set(name);
        serialPortUtils = new SerialPortUtils();
        handler = new Handler(context.getMainLooper());
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_dialog_call,null,false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        final Window window = dialog.getWindow();
        assert window != null;
        SoftKeyboardUtils.notAutoFocus(window);
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = widthPixels/3;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
                SoftKeyboardUtils.notAutoFocus(window);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },500);
                if(serialPortUtils!=null)
                    serialPortUtils.closeSerialPort();
            }
        });
        serialPortUtils = new SerialPortUtils();
        serialPortUtils.openSerialPort();
        serialPortUtils.setOnDataReceiveListener(listener);
        binding.switchCall.setChecked(true);
        binding.switchCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //打开电子秤
                    serialPortUtils.openSerialPort();
                }else{
                    //关闭电子秤
                    serialPortUtils.closeSerialPort();
                }
            }
        });
        dialog.show();
    }

    public void onOk(){
        dialog.dismiss();
        if(onOkListener!=null&&!TextUtils.isEmpty(finalWeight.get())){
            try{
                onOkListener.onOk(Float.parseFloat(Objects.requireNonNull(finalWeight.get())));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private SerialPortUtils.OnDataReceiveListener listener = new SerialPortUtils.OnDataReceiveListener() {
        @Override
        public void onDataReceive(byte[] buffer, int size) {
            mBuffer = buffer;
            handler.post(runnable);
        }

        //开线程更新UI
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //btButton.setText(new String(mBuffer));
                String str = new String(mBuffer);
                if (!TextUtils.isEmpty(str) && str.length() > 5) {
                    //获取电子秤重量
                    String info = str.substring(0, 5);
                    String weight = info.trim();
                    //LogUtils.e("xuxiwu","weight="+weight);
                    try{
                        if(!TextUtils.isEmpty(weight)) {
                            double v = Float.parseFloat(weight);
                            double decimal = StrUtils.get3BitDecimal(v / 1000f);
                            if(decimal<=0.0f)
                                return;
                            finalWeight.set(String.valueOf(decimal));
                            binding.etWeight.setSelection(binding.etWeight.getText().length());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };
    };

    public interface OnOkListener{
            void onOk(double number);
    }
}
