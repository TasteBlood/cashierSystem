package com.cloudcreativity.cashiersystem.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.base.SpaceItemDecoration;
import com.cloudcreativity.cashiersystem.databinding.FragmentListOrderBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemListItemLayoutOrderItemBinding;
import com.cloudcreativity.cashiersystem.databinding.ListItemLayoutBinding;
import com.cloudcreativity.cashiersystem.entity.ListEntity;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.fragments.cashier.ListOrderFragment;
import com.cloudcreativity.cashiersystem.utils.OrderDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListOrderModel extends BaseModel<FragmentActivity, FragmentListOrderBinding> {

    public BaseBindingRecyclerViewAdapter<ListEntity, ListItemLayoutBinding> adapter;
    private ListOrderFragment fragment;
    //private int itemWidth = 0;

    public ListOrderModel(final FragmentActivity context, final FragmentListOrderBinding binding, final ListOrderFragment fragment) {
        super(context, binding);
        this.fragment = fragment;
        adapter = new BaseBindingRecyclerViewAdapter<ListEntity, ListItemLayoutBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.list_item_layout;
            }

            @Override
            protected void onBindItem(final ListItemLayoutBinding binding, final ListEntity item, int position) {
                binding.setItem(item);

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发消息，通知将当前点击的挂单信息导入至开单页面
                        if("open".equals(fragment.getParentName())){
                            EventBus.getDefault().post(item);
                            EventBus.getDefault().post("list_order_close");
                        }
                    }
                });

                //解决点击事件不响应
                binding.icvGoods.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return binding.getRoot().onTouchEvent(event);
                    }
                });

                binding.tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除当前的item
                        new AlertDialog.Builder(context)
                                .setMessage("确定删除这条挂单信息吗?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OrderDao.getInstance(BaseApp.app).deleteOrder(item.getId());
                                        refreshData();
                                    }
                                }).show();
                    }
                });
                BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemListItemLayoutOrderItemBinding> adapter = new BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemListItemLayoutOrderItemBinding>(context) {
                    @Override
                    protected int getLayoutResId(int viewType) {
                        return R.layout.item_list_item_layout_order_item;
                    }

                    @Override
                    protected void onBindItem(ItemListItemLayoutOrderItemBinding itemBinding, OpenOrderGoodsEntity item, int position) {
                        itemBinding.setItem(item);
                        if (position % 2 == 1) {
                            itemBinding.getRoot().setBackgroundColor(Color.parseColor("#F2EDEC"));
                        } else {
                            itemBinding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                };
                binding.icvGoods.setAdapter(adapter);
                binding.icvGoods.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                adapter.getItems().addAll(item.getItems());
                // binding.getRoot().setLayoutParams(new GridLayoutManager.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                binding.tvDisReduce.setText("折扣终价(-"+item.formatDisReduce()+"):");

            }
        };

        binding.rcvListOrder.setLayoutManager(new GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false));
        binding.rcvListOrder.addItemDecoration(new SpaceItemDecoration(8,false,SpaceItemDecoration.GRIDLAYOUT));
//        binding.rcvListOrder.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                binding.rcvListOrder.getViewTreeObserver().removeOnPreDrawListener(this);
//                int width = binding.rcvListOrder.getWidth();
//                float density = context.getResources().getDisplayMetrics().density;
//                itemWidth = (int) ((width - 8*density)/2);
//                return true;
//            }
//        });
    }

    public void refreshData(){
        new Thread(){
            @Override
            public void run() {
                List<Map<String, Object>> maps = OrderDao.getInstance(BaseApp.app).queryOrders();

                if(maps!=null && maps.size()>0){
                    final List<ListEntity> lists = new ArrayList<>();
                    for(Map<String,Object> map:maps){
                        int id = Integer.parseInt(String.valueOf(map.get("id")));
                        String mobile = String.valueOf(map.get("mobile"));
                        long mid = Long.parseLong(String.valueOf(map.get("mid")));
                        double totalMoney = Double.parseDouble(String.valueOf(map.get("totalMoney")));
                        double discountMoney = Double.parseDouble(String.valueOf(map.get("discountMoney")));
                        double discountReduce = Double.parseDouble(String.valueOf(map.get("discountReduce")));
                        String memo = String.valueOf(map.get("memo"));
                        String orderDetails = String.valueOf(map.get("orderDetails"));
                        Type type = new TypeToken<List<OpenOrderGoodsEntity>>() {
                        }.getType();
                        List<OpenOrderGoodsEntity> items = new Gson().fromJson(orderDetails, type);
                        ListEntity entity = new ListEntity(mobile,mid,memo,totalMoney,discountReduce,discountMoney,
                                items);
                        entity.setId(id);
                        lists.add(entity);
                    }

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.getItems().clear();
                            adapter.getItems().addAll(lists);
                        }
                    });

                }else{
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.getItems().clear();
                        }
                    });
                }
            }
        }.start();
    }

    public void onClose(){
        //close list order open other
        // show what? show cashier or open which is visible to the user
        EventBus.getDefault().post("list_order_close");
    }
}
