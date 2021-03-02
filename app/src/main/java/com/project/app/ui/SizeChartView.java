package com.project.app.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.project.app.R;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * create by hb
 * 尺寸表
 */
public class SizeChartView<V extends TextView> extends LinearLayout {
    private final String BTN_MODE = "btn";
    private final String TEX_MODE = "text";
    private Context mContext;
    private final int mGridWidth = 100;
    private final int mGridHeight = 50;
    private Drawable mDrawableGray;
    private Drawable mDrawableLightGray;

    public SizeChartView(Context context) {
        this(context,null);
    }

    public SizeChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mDrawableGray = mContext.getResources().getDrawable(R.color.color_E7E7E7);
        mDrawableLightGray = mContext.getResources().getDrawable(R.color.color_f3f3);
    }

    /**
     * 画尺寸
     */
    public void canvasTableByDataSize(LinkedHashMap<String,List<String>> mapData){
        removeAllViews();
        boolean isNext = false;
        for(Map.Entry<String,List<String>> item : mapData.entrySet()){
            LinearLayout columnLayout=new LinearLayout(mContext);
            columnLayout.setOrientation(LinearLayout.HORIZONTAL);
            List<String> cellItems = item.getValue();

            for (int columnSize = 0;columnSize < cellItems.size();columnSize++){
                //循环它每一行显示4条数据，这就用到Textview了
                TextView text=new TextView(mContext);
                text.setLayoutParams(new LayoutParams(QMUIDisplayHelper.dp2px(mContext,mGridWidth),QMUIDisplayHelper.dp2px(mContext,mGridHeight)));
                //设置Textview宽为0，高为不确定，Layout_weight为1
                text.setText(cellItems.get(columnSize));
                text.setGravity(Gravity.CENTER);
                if(isNext){
                    text.setBackground(mDrawableGray);
                }else{
                    text.setBackground(mDrawableLightGray);
                }
                columnLayout.addView(text);//添加到水平线性布局
            }
            isNext = !isNext;
            addView(columnLayout);
        }
    }
}
