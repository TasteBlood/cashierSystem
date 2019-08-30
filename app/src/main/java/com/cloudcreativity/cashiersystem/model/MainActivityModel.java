package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.activity.MainActivity;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.ActivityMainBinding;
import com.cloudcreativity.cashiersystem.entity.UserEntity;
import com.cloudcreativity.cashiersystem.fragments.CashierIndexFragment;
import com.cloudcreativity.cashiersystem.fragments.MemberFragment;
import com.cloudcreativity.cashiersystem.fragments.MessageFragment;
import com.cloudcreativity.cashiersystem.fragments.PersonalIndexFragment;
import com.cloudcreativity.cashiersystem.fragments.SettingFragment;
import com.cloudcreativity.cashiersystem.fragments.member.MemberListFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;

public class MainActivityModel extends BaseModel<MainActivity, ActivityMainBinding> {

    private FragmentManager manager;

    private CashierIndexFragment indexFragment;
    private MessageFragment messageFragment;
    private MemberFragment memberListFragment;
    private SettingFragment settingFragment;
    private PersonalIndexFragment personalIndexFragment;

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> role = new ObservableField<>();



    public MainActivityModel(MainActivity context, ActivityMainBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        RadioButton radioButton = (RadioButton) binding.rgMain.getChildAt(0);
        radioButton.setChecked(true);
        indexFragment = new CashierIndexFragment();
        messageFragment = new MessageFragment();
        memberListFragment = new MemberFragment();
        settingFragment = new SettingFragment();
        personalIndexFragment = new PersonalIndexFragment();
        manager.beginTransaction().add(R.id.frameMain,indexFragment,"cashierIndex").commit();

        UserEntity user = SPUtils.get().getUser();
        userName.set(user.getName());
        role.set(user.getRole().getName());
        int[] images = {R.mipmap.img_headsculpture1_default,R.mipmap.img_headsculpture2_default};
        BaseApp.AVATAR = images[Math.random()>0.5?1:0];
        binding.ivHeader.setImageResource(BaseApp.AVATAR);
    }

    public void onCheckChange(RadioGroup group,int checkId){
        //FragmentTransaction transaction = manager.beginTransaction();
        switch (checkId){
            case R.id.rb_cashier:
                if(manager.findFragmentByTag("cashierIndex")!=null){
                    manager.beginTransaction().show(indexFragment)
                            .hide(messageFragment)
                            .hide(memberListFragment)
                            .hide(settingFragment)
                            .hide(personalIndexFragment)
                            .commit();
                }else{
                    manager.beginTransaction()
                            .hide(messageFragment)
                            .hide(memberListFragment)
                            .hide(settingFragment)
                            .hide(personalIndexFragment)
                            .add(R.id.frameMain,indexFragment,"cashierIndex")
                            .show(indexFragment)
                            .commit();
                }
                break;
            case R.id.rb_news:
                if(manager.findFragmentByTag("news")!=null){
                    manager.beginTransaction().show(messageFragment)
                            .hide(indexFragment)
                            .hide(memberListFragment)
                            .hide(settingFragment)
                            .hide(personalIndexFragment)
                            .commit();
                }else{
                    manager.beginTransaction()
                            .hide(indexFragment)
                            .hide(memberListFragment)
                            .hide(settingFragment)
                            .hide(personalIndexFragment)
                            .add(R.id.frameMain,messageFragment,"news")
                            .show(messageFragment)
                            .commit();
                }
                break;
            case R.id.rb_member:
                if(manager.findFragmentByTag("member")!=null){
                    manager.beginTransaction().show(memberListFragment)
                            .hide(indexFragment)
                            .hide(messageFragment)
                            .hide(settingFragment)
                            .hide(personalIndexFragment)
                            .commit();
                }else{
                    manager.beginTransaction()
                            .hide(indexFragment)
                            .hide(messageFragment)
                            .hide(settingFragment)
                            .hide(personalIndexFragment)
                            .add(R.id.frameMain,memberListFragment,"member")
                            .show(memberListFragment)
                            .commit();
                }
                break;
            case R.id.rb_setting:
                if(manager.findFragmentByTag("setting")!=null){
                    manager.beginTransaction().show(settingFragment)
                            .hide(indexFragment)
                            .hide(messageFragment)
                            .hide(memberListFragment)
                            .hide(personalIndexFragment)
                            .commit();
                }else{
                    manager.beginTransaction()
                            .hide(indexFragment)
                            .hide(memberListFragment)
                            .hide(messageFragment)
                            .hide(personalIndexFragment)
                            .add(R.id.frameMain,settingFragment,"setting")
                            .show(settingFragment)
                            .commit();
                }
                break;
            case R.id.rb_personal:
                if(manager.findFragmentByTag("personalIndex")!=null){
                    manager.beginTransaction().show(personalIndexFragment)
                            .hide(indexFragment)
                            .hide(messageFragment)
                            .hide(memberListFragment)
                            .hide(settingFragment)
                            .commit();
                }else{
                    manager.beginTransaction()
                            .hide(indexFragment)
                            .hide(memberListFragment)
                            .hide(settingFragment)
                            .hide(messageFragment)
                            .add(R.id.frameMain,personalIndexFragment,"personalIndex")
                            .show(personalIndexFragment)
                            .commit();
                }
                break;
        }
    }
}
