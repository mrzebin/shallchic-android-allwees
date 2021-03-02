package com.project.app.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.project.app.R;
import com.project.app.bean.RecentSearchBean;

import java.util.List;

public class RecentSearchsGroup<X extends TextView> extends ViewGroup {
    private static final int HorInterval   = 15;
    private static final int VerInterval   = 15;
    public  final static String BTN_MODE   = "BTN_MODE";
    public  final static  String TV_MODE   = "TV_MODE";

    //正常样式
    private float itemTextSize = 10;
    private int itemBGResNor = R.drawable.goods_item_btn_normal;


    private int viewHeight = 0;     //控件的高度
    private int viewWidth  = 0;     //控件的宽度
    private final int textModePadding = 10;

    private int itemTextColorNor = Color.parseColor("#999999");

    private Context mContext;

    public RecentSearchsGroup(Context context) {
        this(context,null);
    }

    public RecentSearchsGroup(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.mContext = context;
    }

    public void addItemViews(List<RecentSearchBean.SearchHistoryItem> texts,String mode) {
        removeAllViews();
        for (int i=0;i<texts.size();i++) {
            addItemView(i,texts.get(i).getKeyWord(),mode);
        }
    }

    private void addItemView(int position,String text,String mode) {
        X childView = null;
        switch (mode) {
            case BTN_MODE:
                childView = (X) new Button(mContext);
                break;
            case TV_MODE:
                childView = (X) new TextView(mContext);
                break;
        }

        childView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        childView.setTextSize(itemTextSize);
        setItemPadding(childView);
        childView.setText(text);
        childView.setBackgroundResource(itemBGResNor);
        childView.setTextColor(itemTextColorNor);

        this.addView(childView);
    }

    private void setItemPadding(View view) {
        if (view instanceof Button) {
            view.setPadding(textModePadding, 0, textModePadding, 0);
        } else {
            int fixedPadding_h = QMUIDisplayHelper.dp2px(getContext(),15);
            int fixedPadding_w = QMUIDisplayHelper.dp2px(getContext(),5);
            view.setPadding(fixedPadding_h, fixedPadding_w, fixedPadding_h, fixedPadding_w);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = measureWidth(widthMeasureSpec);
        viewHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(viewWidth, getViewHeight());
    }

    /**
     * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下，
     * 才能确定怎么摆放
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 遍历所有子视图
        int posLeft = HorInterval;
        int posTop = VerInterval;
        int posRight;
        int posBottom;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            if (posLeft + getNextHorLastPos(i) > viewWidth) {
                posLeft = HorInterval;
                posTop += (measureHeight + VerInterval);
            }
            posRight = posLeft + measuredWidth;
            posBottom = posTop + measureHeight;
            childView.layout(posLeft, posTop, posRight, posBottom);
            posLeft += (measuredWidth + HorInterval);
        }
    }

    //获取控件的自适应高度
    private int getViewHeight() {
        int viewwidth = HorInterval;
        int viewheight = VerInterval;
        if (getChildCount() > 0) {
            viewheight = getChildAt(0).getMeasuredHeight() + VerInterval;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            if (viewwidth + getNextHorLastPos(i) > viewWidth) {
                viewwidth = HorInterval;
                viewheight += (measureHeight + VerInterval);
            } else {
                viewwidth += (measuredWidth + HorInterval);
            }
        }
        return viewheight;
    }

    private int getNextHorLastPos(int i) {
        return getChildAt(i).getMeasuredWidth() + HorInterval;
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);
        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                result = getSuggestedMinimumHeight();
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    public void setGroupClickListener(com.project.app.ui.widget.RecentSearchsGroup.OnGroupItemClickListener listener) {
        this.onGroupItemClickListener = listener;
        for (int i = 0; i < getChildCount(); i++) {
            final X childView = (X) getChildAt(i);

            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onGroupItemClickListener.onGroupItemClick(childView.getText().toString());
                }
            });
        }
    }

    public OnGroupItemClickListener onGroupItemClickListener;

    public OnGroupItemClickListener getListener() {
        return onGroupItemClickListener;
    }

    public void setListener(OnGroupItemClickListener listener) {
        this.onGroupItemClickListener = listener;
    }

    public interface OnGroupItemClickListener {
        void onGroupItemClick(String keyName);
    }

}