package com.cloudcreativity.cashiersystem;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.cashiersystem.base.BaseActivity;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.view.category.CategoryView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        CategoryView categoryView = findViewById(R.id.cat_view);
        List<MemberEntity.Category> first = new ArrayList<>();
        for(int i=0;i<20;i++){
            MemberEntity.Category category = new MemberEntity.Category();
            category.setCategory(10*(i+1));
            if(i%2==0){
                category.setCategoryName("蔬菜水果"+i);
            }else{
                category.setCategoryName("粮油副食"+i);
            }
            category.setCategoryOneId("01");
            first.add(category);
        }

        categoryView.setFirst(first);
    }
}
