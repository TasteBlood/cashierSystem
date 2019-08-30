package com.cloudcreativity.cashiersystem.model;

import android.app.Activity;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ExpandableListView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.adapter.GoodsExpandListAdapter;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentGoodsBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutRcvGoodsBinding;
import com.cloudcreativity.cashiersystem.entity.CategoryEntity;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;
import com.cloudcreativity.cashiersystem.utils.BaseResult;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
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

public class GoodsFragmentModel extends BaseModel<Activity, FragmentGoodsBinding> {

    public GoodsExpandListAdapter listAdapter;
    private List<CategoryEntity> categoryEntities;
    public BaseBindingRecyclerViewAdapter<GoodsEntity, ItemLayoutRcvGoodsBinding> goodsAdapter;
    private BaseDialogImpl baseDialog;
    private int pageNum = 1;
    private int pageSize = 20;
    private String oneId;
    private String twoId;
    public ObservableField<String> key = new ObservableField<>();
    private View lastView = null;

    private List<GoodsEntity> totalResult = new ArrayList<>();
    private List<GoodsEntity> newResult = new ArrayList<>();

    public GoodsFragmentModel(Activity context, FragmentGoodsBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        initCategory();
        binding.refreshGoods.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(null, twoId, key.get(), pageNum, pageSize);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(null, twoId, key.get(), pageNum, pageSize);
            }
        });
        initData();
    }

    private void initCategory() {
        categoryEntities = new ArrayList<>();
        listAdapter = new GoodsExpandListAdapter(categoryEntities, context);

        HttpUtils.getInstance().getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(this.baseDialog) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<CategoryEntity>>() {
                        }.getType();
                        List<CategoryEntity> entities = new Gson().fromJson(t, type);
                        if (entities != null && entities.size() > 0) {
                            categoryEntities.addAll(entities);
                            listAdapter.notifyDataSetChanged();
                            binding.elvGoods.expandGroup(0);

                            //加载默认第一大类第一小类的数据
                            if(categoryEntities.get(0).getSeconds()!=null)
                                twoId = categoryEntities.get(0).getSeconds().get(0).getId();
                            binding.refreshGoods.startRefresh();
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

        binding.elvGoods.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (lastView != null)
                    lastView.setBackgroundColor(context.getResources().getColor(R.color.frame_gray_f2edec));
                v.setBackgroundColor(context.getResources().getColor(R.color.white));
                lastView = v;
                twoId = categoryEntities.get(groupPosition).getSeconds().get(childPosition).getId();
                binding.refreshGoods.startRefresh();
                return true;
            }
        });

    }

    private void initData() {
        goodsAdapter = new BaseBindingRecyclerViewAdapter<GoodsEntity, ItemLayoutRcvGoodsBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_rcv_goods;
            }

            @Override
            protected void onBindItem(ItemLayoutRcvGoodsBinding binding, final GoodsEntity item, int position) {
                binding.setItem(item);
                if (position % 2 == 1) {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#e1e1e1"));
                } else {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发送选择事件
                        if(item.getComputerStock()<=0)
                            return;
                        EventBus.getDefault().post(item);
                    }
                });
            }
        };
        binding.rcvGoods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private void loadData(String cateOne, String cateTwo, String key, final int page, int pageSize) {
        HttpUtils.getInstance().getGoods(page, pageSize, cateTwo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<GoodsEntity>>() {
                        }.getType();
                        BaseResult<GoodsEntity> result = new Gson().fromJson(t, type);
                        if (result != null && result.getRecords() != null && result.getRecords().size() > 0) {
                            if (page == 1) {
                                goodsAdapter.getItems().clear();
                                totalResult.clear();
                                binding.refreshGoods.finishRefreshing();
                            } else {
                                binding.refreshGoods.finishLoadmore();
                            }
                            goodsAdapter.getItems().addAll(result.getRecords());
                            totalResult.addAll(result.getRecords());
                            pageNum++;
                        } else {
                            if (page == 1) {
                                goodsAdapter.getItems().clear();
                                totalResult.clear();
                                binding.refreshGoods.finishRefreshing();
                            } else {
                                binding.refreshGoods.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if (page == 1) {
                            binding.refreshGoods.finishRefreshing();
                        } else {
                            binding.refreshGoods.finishLoadmore();
                        }
                    }
                });
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
                            if (entity.getGoodsName().contains(s.toString())||String.valueOf(entity.getGoodsId()).contains(s.toString())) {
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
