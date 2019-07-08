package com.cloudcreativity.cashiersystem.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMessageBinding;
import com.cloudcreativity.cashiersystem.entity.MessageEntity;
import com.cloudcreativity.cashiersystem.fragments.message.MessageDetailFragment;
import com.cloudcreativity.cashiersystem.fragments.message.MessageListFragment;

public class MessageModel extends BaseModel<FragmentActivity, FragmentMessageBinding> {
    private FragmentManager manager;
    private MessageListFragment listFragment;
    private MessageDetailFragment detailFragment;
    public MessageModel(FragmentActivity context, FragmentMessageBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        listFragment = new MessageListFragment();
        detailFragment = new MessageDetailFragment();
        onDefault();
    }

    public void onDefault(){
        if(manager.findFragmentByTag("messageList")!=null){
            manager.beginTransaction()
                    .hide(detailFragment)
                    .show(listFragment)
                    .commit();
        }else{
            manager.beginTransaction()
                    .hide(detailFragment)
                    .add(R.id.frameMessage,listFragment,"messageList")
                    .show(listFragment)
                    .commit();
        }
    }

    public void onDetail(MessageEntity entity){
        if(manager.findFragmentByTag("messageDetail")!=null){
            detailFragment.showData(entity);
            manager.beginTransaction()
                    .hide(listFragment)
                    .show(detailFragment)
                    .commit();
        }else{
            Bundle bundle = new Bundle();
            bundle.putParcelable("entity",entity);
            detailFragment.setArguments(bundle);
            manager.beginTransaction()
                    .hide(listFragment)
                    .add(R.id.frameMessage,detailFragment,"messageDetail")
                    .show(detailFragment)
                    .commit();
        }
    }
}
