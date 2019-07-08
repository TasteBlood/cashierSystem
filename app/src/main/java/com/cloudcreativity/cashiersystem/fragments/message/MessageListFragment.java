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
import com.cloudcreativity.cashiersystem.databinding.FragmentMessageListBinding;
import com.cloudcreativity.cashiersystem.model.MessageListModel;

public class MessageListFragment extends LazyFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMessageListBinding listBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_list,container,false);
        listBinding.getRoot().setClickable(true);
        listBinding.setModel(new MessageListModel(context,listBinding,this));
        return listBinding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
