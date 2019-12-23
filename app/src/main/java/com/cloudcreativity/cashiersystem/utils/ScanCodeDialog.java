package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogScanCodeUtilsBinding;

public class ScanCodeDialog extends Dialog {

    public ObservableField<String> code = new ObservableField<>();
    private OnOkListener onOkListener;

    public void setOnOkListener(OnOkListener onOkListener) {
        this.onOkListener = onOkListener;
    }

    public ScanCodeDialog(final Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        assert window!=null;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        window.getAttributes().width = metrics.widthPixels/2;
        LayoutDialogScanCodeUtilsBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_dialog_scan_code_utils,null,false);
        setContentView(binding.getRoot());
        binding.setUtils(this);
        binding.etCode.setShowSoftInputOnFocus(false);
        binding.etCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                View view = v.focusSearch(View.FOCUS_DOWN);
                if(view!=null){
                    view.requestFocus(View.FOCUS_DOWN);
                }
                return true;
            }
        });
    }

    public void onOk() {
        dismiss();
        if(this.onOkListener!=null)
            this.onOkListener.onOk(code.get());
    }

    public void onCancel() {
        dismiss();
    }

    public interface OnOkListener {
        void onOk(String code);
    }
}
