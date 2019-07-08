package com.cloudcreativity.cashiersystem.model;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMessageListBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMessageBinding;
import com.cloudcreativity.cashiersystem.entity.MessageEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BaseResult;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageListModel extends BaseModel<FragmentActivity, FragmentMessageListBinding> {

    private BaseDialogImpl baseDialog;
    public BaseBindingRecyclerViewAdapter<MessageEntity, ItemLayoutMessageBinding> adapter;

    private int pageNum = 1;
    private int pageSize = 20;
    private Integer category;
    private Integer state;

    public MessageListModel(FragmentActivity context, final FragmentMessageListBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        adapter = new BaseBindingRecyclerViewAdapter<MessageEntity, ItemLayoutMessageBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_message;
            }

            @Override
            protected void onBindItem(ItemLayoutMessageBinding binding, final MessageEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                binding.btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(item);
                    }
                });
            }
        };

        binding.rcvMessageList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        binding.refreshMessage.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(category,state,pageNum,pageSize);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(category,state,pageNum,pageSize);
            }
        });

        binding.refreshMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.refreshMessage.startRefresh();
            }
        },500);
    }

    private void loadData(Integer category, Integer state, final int page, int size){
        HttpUtils.getInstance().getNews(page,size,category,state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<MessageEntity>>() {
                        }.getType();
                        BaseResult<MessageEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshMessage.finishRefreshing();
                            }else{
                                binding.refreshMessage.finishLoadmore();
                            }
                            adapter.getItems().addAll(result.getRecords());
                            pageNum++;
                        }else{
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshMessage.finishRefreshing();
                            }else{
                                binding.refreshMessage.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshMessage.finishRefreshing();
                        }else{
                            binding.refreshMessage.finishLoadmore();
                        }
                    }
                });
    }
}
