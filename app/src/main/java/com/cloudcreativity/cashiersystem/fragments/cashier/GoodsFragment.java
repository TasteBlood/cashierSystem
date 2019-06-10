package com.cloudcreativity.cashiersystem.fragments.cashier;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentGoodsBinding;
import com.cloudcreativity.cashiersystem.model.GoodsFragmentModel;

/**
 * 商品fragment
 */
public class GoodsFragment extends LazyFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentGoodsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods,container,false);
        binding.setModel(new GoodsFragmentModel(context,binding));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
