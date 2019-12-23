package com.cloudcreativity.cashiersystem.model;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ExpandableListView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.adapter.GoodsAdapter;
import com.cloudcreativity.cashiersystem.adapter.GoodsExpandListAdapter;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentGoodsBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutRcvGoodsBinding;
import com.cloudcreativity.cashiersystem.entity.CategoryEntity;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;
import com.cloudcreativity.cashiersystem.utils.CategoryDao;
import com.cloudcreativity.cashiersystem.utils.GoodsDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GoodsFragmentModel extends BaseModel<Activity, FragmentGoodsBinding> {

    private GoodsExpandListAdapter listAdapter;
    private List<CategoryEntity> categoryEntities;
    public BaseBindingRecyclerViewAdapter<GoodsEntity, ItemLayoutRcvGoodsBinding> goodsAdapter;
    private BaseDialogImpl baseDialog;
    private int pageNum = 1;
    private int pageSize = 15;
    private String oneId;
    private String twoId;
    public ObservableField<String> key = new ObservableField<>();
    private View lastView = null;

    private List<GoodsEntity> totalResult = new ArrayList<>();
    private List<GoodsEntity> newResult = new ArrayList<>();

    private Dialog loadDialog;
    private GoodsAdapter adapter;

    public GoodsFragmentModel(Activity context, FragmentGoodsBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        loadDialog = new Dialog(context,R.style.myProgressDialogStyle2);
        View root = View.inflate(context,R.layout.layout_dialog_loading_goods,null);
        loadDialog.setContentView(root);
        initCategory();
        initData();
    }

    public void refresh(){
        // loadDialog.show();
        pageNum = 1;
        loadData(oneId,twoId,key.get(),pageNum,pageSize);
    }

    public void onLoadMoreClick(){
        loadData(oneId,twoId,key.get(),pageNum,pageSize);
    }

    private void initCategory() {
        categoryEntities = new ArrayList<>();
        listAdapter = new GoodsExpandListAdapter(categoryEntities, context);
        binding.elvGoods.setAdapter(listAdapter);
        //从数据库加载
        String cateContent = CategoryDao.getInstance(context).getCateContent();
        Type type = new TypeToken<List<CategoryEntity>>() {
        }.getType();
        List<CategoryEntity> entities = new Gson().fromJson(cateContent, type);
        if (entities != null && entities.size() > 0) {
            categoryEntities.addAll(entities);
            listAdapter.notifyDataSetChanged();

            //加载默认第一大类第一小类的数据
            if (categoryEntities.get(0).getSeconds() != null)
                twoId = categoryEntities.get(0).getSeconds().get(0).getId();

            binding.elvGoods.expandGroup(0);

            refresh();
        }
//
        binding.elvGoods.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (lastView != null)
                    lastView.setBackgroundColor(context.getResources().getColor(R.color.frame_gray_f2edec));
                v.setBackgroundColor(context.getResources().getColor(R.color.white));
                lastView = v;
                twoId = categoryEntities.get(groupPosition).getSeconds().get(childPosition).getId();
                refresh();
                return true;
            }
        });

    }

    private void initData() {
        binding.rcvGoods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new GoodsAdapter(context);
        binding.rcvGoods.setAdapter(adapter);
        adapter.setOnItemClicker(new GoodsAdapter.OnItemClicker() {
            @Override
            public void onItemClick(int position, GoodsEntity entity) {
                if(entity.getComputerStock()<=0)
                    return;
                EventBus.getDefault().post(entity);
            }
        });

    }

    private void loadData(String cateOne, final String cateTwo, String key, final int page, final int pageSize) {
        // loadDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<GoodsEntity> goodsEntities = GoodsDao.getInstance(context).queryGoods(cateTwo,page,pageSize);
                if(goodsEntities!=null && !goodsEntities.isEmpty() && goodsEntities.size()>=15)
                    pageNum ++;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // loadDialog.dismiss();
                        if(page==1){
                            adapter.clear();
                            totalResult.clear();
                        }

                        adapter.add(goodsEntities);
                        totalResult.addAll(goodsEntities);
                        //先让当前的线程休息200毫秒  再加载剩下的
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(pageNum>=2){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // 然后在判断当前的页数是否大于1，如果大于1就加载剩下全部的

                                    final List<GoodsEntity> goodsEntities2 = GoodsDao.getInstance(context).queryGoods(cateTwo,pageNum,100);

                                    if(goodsEntities2==null || goodsEntities2.isEmpty())
                                        return;
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.add(goodsEntities2);
                                            totalResult.addAll(goodsEntities2);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                });

            }
        }).start();
    }

    public TextWatcher myWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(final Editable s) {
            //延迟500ms确定输入完了
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //每次筛选之前先进行数据的保存
                    if (TextUtils.isEmpty(s.toString())) {
                        goodsAdapter.getItems().clear();
                        newResult.clear();
                        goodsAdapter.getItems().addAll(totalResult);
                    } else {
                        newResult.clear();
                        for (GoodsEntity entity : totalResult) {
                            if (entity.getGoodsName().contains(s.toString()) || String.valueOf(entity.getGoodsId()).contains(s.toString())) {
                                newResult.add(entity);
                            }
                        }
                        goodsAdapter.getItems().clear();
                        goodsAdapter.getItems().addAll(newResult);
                    }
                }
            }, 500);
        }
    };
}
