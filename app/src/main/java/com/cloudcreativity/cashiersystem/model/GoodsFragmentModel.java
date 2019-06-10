package com.cloudcreativity.cashiersystem.model;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.adapter.GoodsExpandListAdapter;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentGoodsBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutRcvGoodsBinding;
import com.cloudcreativity.cashiersystem.entity.CategoryEntity;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

public class GoodsFragmentModel extends BaseModel<Activity, FragmentGoodsBinding>{

    public GoodsExpandListAdapter listAdapter;
    public BaseBindingRecyclerViewAdapter<GoodsEntity, ItemLayoutRcvGoodsBinding> goodsAdapter;

    public GoodsFragmentModel(Activity context, FragmentGoodsBinding binding) {
        super(context, binding);
        initCategory();
        initData();
    }

    private void initCategory(){
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        for(int i=0;i<=5;i++){
            CategoryEntity entity = new CategoryEntity();
            entity.setName("父类"+(i+1));
            List<CategoryEntity> childs = new ArrayList<>();
            for(int j=0;j<=10;j++){
                CategoryEntity child = new CategoryEntity();
                child.setName("子分类"+(j+1));
                childs.add(child);
            }
            entity.setChild(childs);
            categoryEntities.add(entity);
        }

        listAdapter = new GoodsExpandListAdapter(categoryEntities,context);
    }

    private void initData(){
        List<GoodsEntity> goodsEntities = new ArrayList<>();
        for(int i=0;i<=20;i++){
            GoodsEntity entity = new GoodsEntity();
            entity.setId(10101001);
            entity.setName("高原夏菜");
            entity.setPrice(50.00);
            entity.setSize("优质");
            entity.setTags("高原、夏菜");
            entity.setUnit("斤");
            entity.setStock(1000);
            goodsEntities.add(entity);
        }
        goodsAdapter = new BaseBindingRecyclerViewAdapter<GoodsEntity, ItemLayoutRcvGoodsBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_rcv_goods;
            }

            @Override
            protected void onBindItem(ItemLayoutRcvGoodsBinding binding, GoodsEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#e1e1e1"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        };

        goodsAdapter.getItems().addAll(goodsEntities);

        binding.rcvGoods.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
    }
}
