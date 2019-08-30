package com.cloudcreativity.cashiersystem.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutDayBinding;
import com.cloudcreativity.cashiersystem.databinding.LayoutChooseDateBinding;
import com.cloudcreativity.cashiersystem.entity.Day;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * All Rights Reserved By CloudCreativity Tech.
 *
 * @author : created by Xu Xiwu
 * date-time: 2019/8/8 15:48
 * e-mail: xxw0701@sina.com
 */
public class DateChooseUtils extends Dialog {

    public BaseBindingRecyclerViewAdapter<Day, ItemLayoutDayBinding> adapter;

    private Calendar current;
    private Calendar today;
    private OnDateClickListener onDateClickListener;
    private float startX = 0;
    private float endX = 0;
    public ObservableField<String> currentTitle = new ObservableField<>();
    private final LayoutChooseDateBinding binding;

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    public DateChooseUtils(Context context, int themeResId) {
        super(context, themeResId);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_choose_date, null, false);
        binding.setUtils(this);
        setContentView(binding.getRoot());
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        assert getWindow() != null;
        getWindow().getAttributes().width = widthPixels / 5 * 2;
        setCanceledOnTouchOutside(false);
//        binding.rcvDate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    startX = event.getX();
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    endX = event.getX();
//                    if (endX == startX) {
//                        return false;
//                    }
//                    if (endX > startX) {
//                        //向右
//                        if (Math.abs(endX - startX) >= 100) {
//                            onLast();
//                            startX = 0;
//                            endX = 0;
//                            return true;
//                        } else {
//                            startX = 0;
//                            endX = 0;
//                            return false;
//                        }
//                    } else if (endX < startX) {
//                        //向左
//                        if (Math.abs(endX - startX) >= 100) {
//                            onNext();
//                            startX = 0;
//                            endX = 0;
//                            return true;
//                        } else {
//                            startX = 0;
//                            endX = 0;
//                            return false;
//                        }
//                    } else {
//                        startX = 0;
//                        endX = 0;
//                        return false;
//                    }
//                }
//                return false;
//            }
//        });
        binding.rcvDate.setLayoutManager(new GridLayoutManager(context, 7, LinearLayoutManager.VERTICAL, false));
        adapter = new BaseBindingRecyclerViewAdapter<Day, ItemLayoutDayBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_day;
            }

            @Override
            protected void onBindItem(ItemLayoutDayBinding binding, final Day item, int position) {
                binding.setItem(item);
                binding.tvDate.setText(item == null ? "" : String.valueOf(item.getDay()));
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item == null)
                            return;
                        if (onDateClickListener != null) {
                            onDateClickListener.onClick(item.getYear(), item.getMonth(), item.getDay());
                        }
                        dismiss();
                    }
                });
            }
        };
        today = Calendar.getInstance(Locale.CHINA);
        current = (Calendar) today.clone();
        currentTitle.set(current.get(Calendar.YEAR) + "年" + (current.get(Calendar.MONTH) + 1) + "月");
        adapter.getItems().addAll(getAllDays(current));
    }

    public void onCancel() {
        dismiss();
    }


    private List<Day> getAllDays(Calendar calendar) {
        int temp = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.DAY_OF_MONTH, temp);

        int firstDay = calendar.getMinimum(Calendar.DAY_OF_MONTH);
        int lastDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);


        List<Day> days = new ArrayList<>();
        for (int day = firstDay; day <= lastDay; day++) {
            if (year == today.get(Calendar.YEAR) &&
                    month == today.get(Calendar.MONTH) &&
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                days.add(new Day(day, month, year, false, true));
            } else {
                days.add(new Day(day, month, year, false, false));
            }

        }
        for (int start = 1; start < i; start++) {
            days.add(0, null);
        }
        return days;
    }

    public void onNext() {
        current.add(Calendar.MONTH, 1);
        adapter.getItems().clear();
        adapter.getItems().addAll(getAllDays(current));
        currentTitle.set(current.get(Calendar.YEAR) + "年" + (current.get(Calendar.MONTH) + 1) + "月");
    }

    public void onLast() {
        current.add(Calendar.MONTH, -1);
        adapter.getItems().clear();
        adapter.getItems().addAll(getAllDays(current));
        currentTitle.set(current.get(Calendar.YEAR) + "年" + (current.get(Calendar.MONTH) + 1) + "月");
    }

    public interface OnDateClickListener {
        void onClick(int year, int month, int day);
    }

}
