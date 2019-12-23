package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.databinding.LayoutMemberTableBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.view.category.CategoryView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberTableUtils extends Dialog {
    private long mid;
    public MemberTableUtils(Context context, List<MemberEntity.Category> first, final long mid, final BaseDialogImpl baseDialog) {
        super(context, R.style.myProgressDialogStyle);
        this.mid = mid;
        setCanceledOnTouchOutside(false);
        final LayoutMemberTableBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.layout_member_table,null,false);
        binding.setUtils(this);
        setContentView(binding.getRoot());
        binding.catView.setFirst(first);
        binding.catView.setOnItemClickListener(new CategoryView.OnItemClickListener() {
            @Override
            public void onClick(MemberEntity.Category category) {
                //加载二级展示
                HttpUtils.getInstance().getCate2Amout(category.getCategoryOneId(),mid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<String>(baseDialog,true) {
                            @Override
                            public void onSuccess(String t) {
                                Type type = new TypeToken<List<MemberEntity.Category>>() {
                                }.getType();
                                List<MemberEntity.Category> second = new Gson().fromJson(t,type);
                                binding.catView.setSecond(second);
                            }

                            @Override
                            public void onFail(ExceptionReason msg) {

                            }
                        });
            }
        });
    }

    public void onClose(){
        dismiss();
    }
}
