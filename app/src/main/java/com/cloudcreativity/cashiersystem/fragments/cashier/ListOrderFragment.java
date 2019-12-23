package com.cloudcreativity.cashiersystem.fragments.cashier;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.databinding.FragmentListOrderBinding;
import com.cloudcreativity.cashiersystem.model.ListOrderModel;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

/**
 * list order fragment
 */
public class ListOrderFragment extends Fragment {

    private ListOrderModel model;
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public static ListOrderFragment getInstance(String name){
        ListOrderFragment fragment = new ListOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            this.parentName = bundle.getString("name");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("xuxiwu","on resume...");
        if(getUserVisibleHint()){
            if(model!=null){
                LogUtils.e("xuxiwu","load data...");
                model.refreshData();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentListOrderBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_order,null,false);
        model = new ListOrderModel(getActivity(), binding,this);
        binding.setModel(model);
        binding.getRoot().setClickable(true);
        //防止穿透
        return binding.getRoot();
    }

}
