package com.cloudcreativity.cashiersystem.fragments.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentMessageDetailBinding;
import com.cloudcreativity.cashiersystem.entity.MessageEntity;
import com.cloudcreativity.cashiersystem.model.MessageDetailModel;

public class MessageDetailFragment extends LazyFragment {

    private MessageDetailModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMessageDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_detail,container,false);
        model = new MessageDetailModel(context, binding);
        assert getArguments()!=null;
        MessageEntity entity = getArguments().getParcelable("entity");
        binding.setModel(model);
        model.showData(entity);
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    public void showData(MessageEntity entity){
        model.showData(entity);
    }

    @Override
    public void initialLoadData() {

    }
}
