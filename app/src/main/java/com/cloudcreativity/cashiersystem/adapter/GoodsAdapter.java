package com.cloudcreativity.cashiersystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<GoodsEntity> goodsEntities = new ArrayList<>();
    private Context context;

    private OnItemClicker onItemClicker;

    public GoodsAdapter(Context context) {
        this.context = context;
    }

    public void add(List<GoodsEntity> goods){
        this.goodsEntities.addAll(goods);
        notifyDataSetChanged();
    }

    public void clear(){
        this.goodsEntities.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClicker(OnItemClicker onItemClicker) {
        this.onItemClicker = onItemClicker;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {
        View root = LayoutInflater.from(context)
                .inflate(R.layout.item_layout_rcv_goods,viewGroup,false);
        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        if (myViewHolder.getAdapterPosition() % 2 == 1) {
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#e1e1e1"));
        } else {
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClicker!=null){
                    onItemClicker.onItemClick(myViewHolder.getAdapterPosition(),goodsEntities.get(myViewHolder.getAdapterPosition()));
                }
            }
        });
        GoodsEntity goodsEntity = goodsEntities.get(i);
        myViewHolder.tv_name.setText(goodsEntity.getGoodsName());
        myViewHolder.tv_standards.setText(goodsEntity.getGoodsDomain().getStandards());
        myViewHolder.tv_id.setText(String.valueOf(goodsEntity.getGoodsId()));
        myViewHolder.tv_tab.setText(goodsEntity.getGoodsDomain().getTab());
        myViewHolder.tv_stock.setText(String.valueOf(goodsEntity.getComputerStock()));
        myViewHolder.tv_unit.setText(goodsEntity.getGoodsDomain().getUnit());
        myViewHolder.tv_price.setText(goodsEntity.getGoodsDomain().formatPrice());
    }

    @Override
    public int getItemCount() {
        return goodsEntities.size();
    }

    public interface OnItemClicker{
        void onItemClick(int position,GoodsEntity entity);
    }
}



class MyViewHolder extends RecyclerView.ViewHolder{

    TextView tv_name;
    TextView tv_id;
    TextView tv_standards;
    TextView tv_unit;
    TextView tv_price;
    TextView tv_stock;
    TextView tv_tab;

    MyViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_id = itemView.findViewById(R.id.tv_goodsId);
        tv_standards = itemView.findViewById(R.id.tv_standards);
        tv_unit = itemView.findViewById(R.id.tv_unit);
        tv_price = itemView.findViewById(R.id.tv_price);
        tv_stock = itemView.findViewById(R.id.tv_stock);
        tv_tab = itemView.findViewById(R.id.tv_tab);
    }
}
