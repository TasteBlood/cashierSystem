package com.cloudcreativity.cashiersystem.model;

import android.app.Dialog;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberListBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.utils.AddMemberUtils;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BaseResult;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.GlideUtils;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.RechargeDialogUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberListModel extends BaseModel<FragmentActivity, FragmentMemberListBinding> {

    public BaseBindingRecyclerViewAdapter<MemberEntity, ItemLayoutMemberBinding> adapter;
    private BaseDialogImpl baseDialog;
    private Integer identity;
    private Integer level;
    private int pageNum = 1;
    private int pageSize = 20;
    public ObservableField<String> key = new ObservableField<>();
    public MemberListModel(FragmentActivity context, final FragmentMemberListBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        binding.rcvMemberList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        adapter = new BaseBindingRecyclerViewAdapter<MemberEntity, ItemLayoutMemberBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_member;
            }

            @Override
            protected void onBindItem(ItemLayoutMemberBinding binding, final MemberEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }

                binding.getRoot().findViewById(R.id.btn_recharge).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击充值
                        new RechargeDialogUtils().show(MemberListModel.this.context);
                    }
                });

                binding.getRoot().findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击编辑
                        BaseApp.CURRENT_MID = item.getId();
                        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_EDIT);

                    }
                });

                binding.getRoot().findViewById(R.id.btn_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击详情
                        BaseApp.CURRENT_MID = item.getId();
                        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_DETAIL_INDEX);
                    }
                });


            }
        };

        binding.refreshMember.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(identity,level,pageNum,pageSize,key.get());
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(identity,level,pageNum,pageSize,key.get());
            }
        });

        binding.refreshMember.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.refreshMember.startRefresh();
            }
        },500);
    }

    private RadioButton lastCheck1 = null;
    private RadioButton lastCheck2 = null;

    private void loadData(Integer identity, Integer level, final int page, int size, String key){
        HttpUtils.getInstance().getMembers(identity,level,page,size,key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<MemberEntity>>() {
                        }.getType();
                        BaseResult<MemberEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshMember.finishRefreshing();
                            }else{
                                binding.refreshMember.finishLoadmore();
                            }
                            adapter.getItems().addAll(result.getRecords());
                            pageNum ++;
                        }else{
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshMember.finishRefreshing();
                            }else{
                                binding.refreshMember.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshMember.finishRefreshing();
                        }else{
                            binding.refreshMember.finishLoadmore();
                        }
                    }
                });
    }

    @BindingAdapter("memberHeader")
    public static void display(ImageView imageView,String url){
        if(TextUtils.isEmpty(url)){
            imageView.setImageResource(R.mipmap.img_header_portrait);
            return;
        }
        GlideUtils.load(imageView.getContext(),url,imageView);
    }

    public void onOptionClick1(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck1!=null&&lastCheck1.getId()==rb.getId()){
            binding.rgIdentity.clearCheck();
            lastCheck1 = null;
            identity = null;
            binding.refreshMember.startRefresh();
            return;
        }
        binding.rgIdentity.check(rb.getId());
        lastCheck1 = rb;
        if(lastCheck1.getId()==R.id.rb_member_family){
            identity = 1;
        }else if(lastCheck1.getId()==R.id.rb_member_vip){
            identity = 2;
        }
        binding.refreshMember.startRefresh();
    }

    public void onOptionClick2(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck2!=null&&lastCheck2.getId()==rb.getId()){
            binding.rgLevel.clearCheck();
            lastCheck2 = null;
            level = null;
            binding.refreshMember.startRefresh();
            return;
        }
        binding.rgLevel.check(rb.getId());
        lastCheck2 = rb;
        if(lastCheck2.getId()==R.id.rb_member_common){
            level = 1;
        }else if(lastCheck2.getId()==R.id.rb_member_silver){
            level = 2;
        }else if(lastCheck2.getId()==R.id.rb_member_gold){
            level = 3;
        }else if(lastCheck2.getId()==R.id.rb_member_diamond){
            level = 4;
        }
        binding.refreshMember.startRefresh();
    }

    public void onNewClick(){
        new AddMemberUtils().show(context, new AddMemberUtils.OnOkListener() {
            @Override
            public void onOk(String mobile, int type) {
                addMember(mobile,type);
            }
        });
    }

    private void addMember(String mobile, int type) {
        HttpUtils.getInstance().addMember(type,mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        //创建成功，展示创建成功的对话框，并且更新数据
                        final Dialog dialog = new Dialog(context,R.style.myProgressDialogStyle);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        View content = View.inflate(context,R.layout.layout_dialog_add_member_success,null);dialog.setContentView(content);
                        Window window = dialog.getWindow();
                        assert window != null;
                        window.setGravity(Gravity.CENTER);
                        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/3;
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },2000);
                        binding.refreshMember.startRefresh();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    public void onSearchClick(){
        if(key.get()!=null){
            binding.refreshMember.startRefresh();
        }
    }
}
