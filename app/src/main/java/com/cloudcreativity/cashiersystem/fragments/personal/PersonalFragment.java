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
import com.cloudcreativity.cashiersystem.databinding.FragmentPersonalBinding;
import com.cloudcreativity.cashiersystem.model.PersonalModel;

public class PersonalFragment extends LazyFragment {

    @Override
    public void initialLoadData() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPersonalBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal,container,false);
        binding.setModel(new PersonalModel(context,binding,this));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }
}
