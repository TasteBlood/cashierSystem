package com.cloudcreativity.cashiersystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.entity.CategoryEntity;

import java.util.List;

public class GoodsExpandListAdapter extends BaseExpandableListAdapter {

    private List<CategoryEntity> entities;
    private Context context;

    public GoodsExpandListAdapter(List<CategoryEntity> entities, Context context) {
        this.entities = entities;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return entities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return entities.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = new GroupHolder().getView(convertView);
        GroupHolder holder = (GroupHolder) convertView.getTag();
        holder.tv_name.setText(entities.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = new ChildHolder().getView(convertView);
        ChildHolder holder = (ChildHolder) convertView.getTag();
        holder.tv_name.setText(entities.get(groupPosition).getChild().get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupHolder{
        TextView tv_name;
        View getView(View view){
            GroupHolder holder;
            if(view==null){
                view = View.inflate(context,R.layout.item_layout_group_goods,null);
                holder = new GroupHolder();
                holder.tv_name = view.findViewById(R.id.tv_name);
                view.setTag(holder);
            }
            return view;
        }
    }

    public class ChildHolder{
        TextView tv_name;
        View getView(View view){
            ChildHolder holder;
            if(view==null){
                view = View.inflate(context,R.layout.item_layout_goods,null);
                holder = new ChildHolder();
                holder.tv_name = view.findViewById(R.id.tv_name);
                view.setTag(holder);
            }
            return view;
        }
    }
}
