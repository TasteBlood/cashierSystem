package com.cloudcreativity.cashiersystem.fragments.personal;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentChangeMobileBinding;
import com.cloudcreativity.cashiersystem.model.ChangeMobileModel;
import com.cloudcreativity.cashiersystem.utils.SPUtils;

public class ChangeMobileFragment extends LazyFragment {

    private ChangeMobileModel mobileModel;

    @Override
    public void initialLoadData() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentChangeMobileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_mobile,container,false);
        binding.setModel(mobileModel = new ChangeMobileModel(context,binding,this));
        binding.getRoot().setClickable(true);

        //
        if(SPUtils.get().getSMSTime()>0 && mobileModel!=null){
            mobileModel.startTimer(SPUtils.get().getSMSTime());
        }
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        mobileModel.stopTimer();
    }
}
