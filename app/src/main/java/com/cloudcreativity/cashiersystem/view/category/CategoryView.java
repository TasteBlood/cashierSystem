package com.cloudcreativity.cashiersystem.view.category;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.utils.LogUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员购买物类占比图
 */
public class CategoryView extends View {
    public static String[] FIRST_COLORS = {"#CD853F","#FF7F50","#FF6347","#FF0000","#FF4500","#B22222",
            "#FFA500","#FFFF00","#ADFF2F","#00FF00",
    "#00FF7F","#008B8B","#8CC269","#00FF7F","#008B8B","#8CC269","#00FFFF","#1E90FF","#0000FF","#6A5ACD"};


    public static String[] SECOND_COLORS = {
            "#96C24E","#5BAE23","#8CC269","#ADD5A2","#41B349",
            "#9ECCAB","#5DBE8A","#3C9566","#68B88E","#EB3C70",
            "#20A162","#EC7696","#55BB8A","#83CBAC","#45B787",
            "#2BAE85","#69A794","#2C9678","#428675","#1BA784"};

    public OnItemClickListener onItemClickListener;

    private int width;
    private int height;
    private Paint paint;
    private List<MemberEntity.Category> first;
    private List<MemberEntity.Category> second;
    private String firstTitle;

    //save block and x y relations
    private List<Map<String, Object>> firstBlock = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<MemberEntity.Category> getFirst() {
        return first;
    }

    public void setFirst(List<MemberEntity.Category> first) {
        this.first = first;
        this.second = null;
        invalidate();
    }

    public List<MemberEntity.Category> getSecond() {
        return second;
    }

    public void setSecond(List<MemberEntity.Category> second) {
        this.second = second;
        invalidate();
    }

    public CategoryView(Context context) {
        this(context, null);
    }

    public CategoryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0, 0);
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMinimumWidth(800);
        setMinimumHeight(800);
        this.width = getWidth();
        this.height = getHeight();
        //LogUtils.e("xuxiwu", "width=" + width + "height=" + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFirstCate(canvas);
        drawSecondCate(canvas);
    }

    float sx= 0;
    float sy = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //LogUtils.e("xuxiwu","rawX="+event.getRawX()+"rawY"+event.getRawY()+"x="+event.getX()+"y="+event.getY());
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            sx = event.getX();
            sy = event.getY();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float ex = event.getX();
            float ey = event.getY();
            LogUtils.e("xuxiwu", "rx=" + Math.abs(sx - ex) + "ry=" + Math.abs(sy - ey));
            if (Math.abs(sx - ex) <= 5 && Math.abs(sy - ey) <= 5) {
                LogUtils.e("xuxiwu", "click events");
                LogUtils.e("xuxiwu","sx="+sx+"sy="+sy);
                //说明点击,判断当前的点击区域在哪一个块上
                for (int i = 0; i < firstBlock.size(); i++) {
                    float left = Float.parseFloat(String.valueOf(firstBlock.get(i).get("left")));
                    float top = Float.parseFloat(String.valueOf(firstBlock.get(i).get("top")));
                    float right = Float.parseFloat(String.valueOf(firstBlock.get(i).get("right")));
                    float bottom = Float.parseFloat(String.valueOf(firstBlock.get(i).get("bottom")));
                    LogUtils.e("xuxiwu","left="+left+"top="+top+"right="+right+"bottom="+bottom);
                    if (sx >= left && sx <= right) {
                        if (sy >= top && sy <= bottom) {
                            //确实点击到了某一个块上
                            int index = Integer.parseInt(String.valueOf(firstBlock.get(i).get("index")));
                            //ToastUtils.showShortToast(getContext(), first.get(index).getCategoryName());
                            this.firstTitle = first.get(index).getCategoryName();
                            this.setSecond(null);
                            invalidate();
                            if(onItemClickListener!=null)
                                onItemClickListener.onClick(first.get(index));
                            return true;
                        }
                    }
                }

            } else {
                return false;
            }
        }
        return true;
    }

    float preAngle = 0;
    float x = 0;
    float y = 0;

    private void drawFirstCate(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (first == null || first.isEmpty())
            return;
        this.firstBlock = new ArrayList<>();
        float num = first.size();
        float sum = 0;
        for (int i = 0; i < first.size(); i++) {
            sum += first.get(i).getCategory();
        }

        //开始绘制
        RectF rect = new RectF(getX(), getY(), 300, 300);
        for (int i = 0; i < num; i++) {
            first.get(i).setColor(FIRST_COLORS[i]);
            paint.setColor(Color.parseColor(first.get(i).getColor()));
            paint.setStrokeWidth(20);
            float angle = (first.get(i).getCategory() / sum) * 360f;
            // LogUtils.e("xuxiwu","angle=="+preAngle);
            canvas.drawArc(rect, preAngle, angle, true, paint);
            preAngle += angle;
        }
        x = getLeft();
        y = getTop() + 300;

        //开始写字
        paint.setTextSize(18);
        paint.setColor(Color.BLACK);
        canvas.drawText("一级品类占比图", x, y + 10, paint);
        y += 30;
        for (int i = 0; i < num; i++) {
            if (i % 3 == 0 && i != 0) {
                //每一行，将置为初始位置
                x = getLeft();
                y += 60;
            }
            Map<String, Object> data = new HashMap<>();
            paint.setColor(Color.parseColor(first.get(i).getColor()));
            canvas.drawRect(x, y, x + 60, y + 30, paint);
            data.put("index", i);
            data.put("left", x);
            data.put("top", y);
            data.put("right", x + 60);
            data.put("bottom", y + 30);
            firstBlock.add(data);
            // canvas.drawLine(x,y,x+60,y,paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(16);
            String text = first.get(i).getCategoryName();
            Rect rect1 = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect1);
            canvas.drawText(text, x, y + 50, paint);
            x += rect1.right + 50;

        }
    }

    private void drawSecondCate(Canvas canvas) {
        if (second == null || second.isEmpty())
            return;
        float num = second.size();
        float sum = 0;
        for (int i = 0; i < second.size(); i++) {
            sum += second.get(i).getCategory();
        }

        //开始绘制
        RectF rect = new RectF(getX() + 300 + 80, getY(), getX() + 300 + 300 + 60, 300);
        for (int i = 0; i < num; i++) {
            second.get(i).setColor(SECOND_COLORS[i]);
            paint.setColor(Color.parseColor(second.get(i).getColor()));
            //paint.setStrokeWidth(20);
            float angle = (second.get(i).getCategory() / sum) * 360f;
            // LogUtils.e("xuxiwu","angle=="+preAngle);
            canvas.drawArc(rect, preAngle, angle, true, paint);
            preAngle += angle;
        }
        x = getLeft() + 300 + 80;
        y = getTop() + 300;

        //开始写字
        paint.setTextSize(18);
        paint.setColor(Color.BLACK);

        // replace first cate name
        canvas.drawText(this.firstTitle+"占比图", x, y + 10, paint);
        y += 30;
        for (int i = 0; i < num; i++) {
            if (i % 3 == 0 && i != 0) {
                //每一行，将置为初始位置
                x = getLeft() + 300 + 80;
                y += 60;
            }
            paint.setColor(Color.parseColor(second.get(i).getColor()));
            canvas.drawRect(x, y, x + 60, y + 30, paint);
            // canvas.drawLine(x,y,x+60,y,paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(16);
            String text = second.get(i).getCategoryName();
            Rect rect1 = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect1);
            canvas.drawText(text, x, y + 50, paint);
            x += rect1.right + 50;

        }
    }

    public interface OnItemClickListener{
        void onClick(MemberEntity.Category category);
    }

}
