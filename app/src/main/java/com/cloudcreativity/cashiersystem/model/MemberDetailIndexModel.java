package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberDetailIndexBinding;
import com.cloudcreativity.cashiersystem.fragments.member.BalanceFragment;
import com.cloudcreativity.cashiersystem.fragments.member.MemberDetailFragment;
import com.cloudcreativity.cashiersystem.fragments.member.PayFragment;
import com.cloudcreativity.cashiersystem.fragments.member.ScoreFragment;
import com.cloudcreativity.cashiersystem.fragments.message.MessageDetailFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class MemberDetailIndexModel extends BaseModel<FragmentActivity, FragmentMemberDetailIndexBinding> {

    private FragmentManager manager;


    public MemberDetailIndexModel(FragmentActivity context, FragmentMemberDetailIndexBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        onDetail();
    }


    public void onDetail(){
        MemberDetailFragment fragment = new MemberDetailFragment();
        FGUtils.replaceNoAnim(manager,R.id.frameMemberDetail,fragment);
    }

    public void onBalance(){
        FGUtils.replaceNoAnim(manager,R.id.frameMemberDetail,new BalanceFragment());
    }

    public void onScore(){
        FGUtils.replaceNoAnim(manager,R.id.frameMemberDetail,new ScoreFragment());
    }

    public void onPay(){
        FGUtils.replaceNoAnim(manager,R.id.frameMemberDetail,new PayFragment());
    }
}
