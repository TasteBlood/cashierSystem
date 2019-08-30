package com.cloudcreativity.cashiersystem.entity;

/**
 * All Rights Reserved By CloudCreativity Tech.
 *
 * @author : created by Xu Xiwu
 * date-time: 2019/8/8 17:35
 * e-mail: xxw0701@sina.com
 */
public class Day {
    private int day;
    private int month;
    private int year;
    private boolean isSelect;
    private boolean isToday;

    public Day(int day, int month, int year, boolean isSelect, boolean isToday) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.isSelect = isSelect;
        this.isToday = isToday;
    }

    public boolean isToday() {
        return isToday;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}
