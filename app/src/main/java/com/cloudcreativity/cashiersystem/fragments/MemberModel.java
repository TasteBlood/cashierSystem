package com.cloudcreativity.cashiersystem.fragments;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberBinding;
import com.cloudcreativity.cashiersystem.fragments.member.MemberDetailFragment;
import com.cloudcreativity.cashiersystem.fragments.member.MemberDetailIndexFragment;
import com.cloudcreativity.cashiersystem.fragments.member.MemberEditFragment;
import com.cloudcreativity.cashiersystem.fragments.member.MemberListFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class MemberModel extends BaseModel<FragmentActivity, FragmentMemberBinding> {

    private FragmentManager manager;

    private MemberListFragment memberListFragment;
    private MemberDetailIndexFragment memberDetailIndexFragment;
    private MemberEditFragment memberEditFragment;

    MemberModel(FragmentActivity context, FragmentMemberBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        //FGUtils.replace(manager, R.id.frameMember,new MemberListFragment());
        memberListFragment = new MemberListFragment();
        memberEditFragment = new MemberEditFragment();
        memberDetailIndexFragment = new MemberDetailIndexFragment();

        onList();
    }

    public void onDetail() {
        //FGUtils.replace(manager,R.id.frameMember,new MemberDetailIndexFragment());
        memberDetailIndexFragment = new MemberDetailIndexFragment();
        manager.beginTransaction()
                .hide(memberListFragment)
                .hide(memberEditFragment)
                .add(R.id.frameMember,memberDetailIndexFragment, "memberDetail")
                .show(memberDetailIndexFragment)
                .commit();
    }

    public void onList() {
        if (manager.findFragmentByTag("memberList") != null) {
            manager.beginTransaction()
                    .remove(memberDetailIndexFragment)
                    .hide(memberEditFragment)
                    .hide(memberDetailIndexFragment)
                    .show(memberListFragment)
                    .commit();
        } else {
            manager.beginTransaction()
                    .remove(memberDetailIndexFragment)
                    .hide(memberEditFragment)
                    .hide(memberDetailIndexFragment)
                    .add(R.id.frameMember, memberListFragment, "memberList")
                    .show(memberListFragment)
                    .commit();
        }
    }

    public void onEdit() {
        //FGUtils.replace(manager,R.id.frameMember,new MemberDetailFragment());
//        if (manager.findFragmentByTag("memberEdit") != null) {
//            manager.beginTransaction()
//                    .hide(memberListFragment)
//                    .hide(memberDetailIndexFragment)
//                    .show(memberDetailFragment)
//                    .commit();
//        } else {
//            manager.beginTransaction()
//                    .hide(memberListFragment)
//                    .hide(memberDetailIndexFragment)
//                    .add(R.id.frameMember, memberDetailFragment, "memberEdit")
//                    .show(memberDetailFragment)
//                    .commit();
//        }
        memberEditFragment = new MemberEditFragment();
        manager.beginTransaction()
                .hide(memberListFragment)
                .hide(memberEditFragment)
                .add(R.id.frameMember,memberEditFragment, "memberEdit")
                .show(memberEditFragment)
                .commit();
    }
}
