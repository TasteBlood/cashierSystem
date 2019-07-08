package com.cloudcreativity.cashiersystem.model;

import android.app.Dialog;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberListBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.utils.AddMemberUtils;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.GlideUtils;
import com.cloudcreativity.cashiersystem.utils.RechargeDialogUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MemberListModel extends BaseModel<FragmentActivity, FragmentMemberListBinding> {

    public BaseBindingRecyclerViewAdapter<MemberEntity, ItemLayoutMemberBinding> adapter;

    public MemberListModel(FragmentActivity context, FragmentMemberListBinding binding) {
        super(context, binding);
        binding.rcvMemberList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        adapter = new BaseBindingRecyclerViewAdapter<MemberEntity, ItemLayoutMemberBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_member;
            }

            @Override
            protected void onBindItem(ItemLayoutMemberBinding binding, MemberEntity item, int position) {
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
                        new RechargeDialogUtils().show(MemberListModel.this.context, new RechargeDialogUtils.OnChooseListener() {
                            @Override
                            public void onChoose(int money) {
                                final Dialog dialog = new Dialog(context,R.style.myProgressDialogStyle);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                if(System.currentTimeMillis()%1000/2==0){
                                    View content = View.inflate(context,R.layout.layout_dialog_recharge_success,null);
                                    dialog.setContentView(content);
                                }else{
                                    View content = View.inflate(context,R.layout.layout_dialog_recharge_failed,null);
                                    dialog.setContentView(content);
                                }
                                Window window = dialog.getWindow();
                                window.setGravity(Gravity.CENTER);
                                window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/3;
                                dialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                },2000);
                            }
                        });
                    }
                });

                binding.getRoot().findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击编辑
                        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_EDIT);
                    }
                });

                binding.getRoot().findViewById(R.id.btn_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击详情
                        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_DETAIL_INDEX);
                    }
                });


            }
        };
        loadData();
    }

    private RadioButton lastCheck1 = null;
    private RadioButton lastCheck2 = null;

    private void loadData(){
        List<MemberEntity> memberEntities = new ArrayList<>();
        for(int i=0;i<=20;i++){
            MemberEntity entity = new MemberEntity();
            entity.setAvatar("http://wx.qlogo.cn/mmopen/Q3auHgzwzM50BKOHLaSfVI8LnvIeXd1qbTv9CPZXOfqZYiaFjjUZbqiboQnUGfDLEgY0geBAPtvpXoKDaRvZ2viaQ/0");
            entity.setBalance(2000);
            entity.setIdentity("VIP会员");
            entity.setLevel("铂金会员");
            entity.setMobile("18809446038");

            memberEntities.add(entity);
        }

        adapter.getItems().addAll(memberEntities);
    }

    @BindingAdapter("memberHeader")
    public static void display(ImageView imageView,String url){
        GlideUtils.load(imageView.getContext(),url,imageView);
    }

    public void onOptionClick1(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck1!=null&&lastCheck1.getId()==rb.getId()){
            binding.rgIdentity.clearCheck();
            lastCheck1 = null;
            return;
        }
        binding.rgIdentity.check(rb.getId());
        lastCheck1 = rb;
    }

    public void onOptionClick2(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck2!=null&&lastCheck2.getId()==rb.getId()){
            binding.rgLevel.clearCheck();
            lastCheck2 = null;
            return;
        }
        binding.rgLevel.check(rb.getId());
        lastCheck2 = rb;
    }

    public void onNewClick(){
        new AddMemberUtils().show(context, new AddMemberUtils.OnOkListener() {
            @Override
            public void onOk(String mobile, int type) {

            }
        });
    }

    public void onSearchClick(){
        ToastUtils.showShortToast(context,"搜索会员了");
    }
}
