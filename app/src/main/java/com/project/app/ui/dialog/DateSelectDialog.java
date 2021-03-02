package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hb.basemodel.wheelview.OnWheelChangedListener;
import com.hb.basemodel.wheelview.WheelView;
import com.hb.basemodel.wheelview.adapter.ArrayWheelAdapter;
import com.hb.basemodel.wheelview.adapter.ListWheelAdapter;
import com.project.app.R;
import com.project.app.utils.CaldenarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * create by hb
 * 日志选择器
 */
public class DateSelectDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private IDateSelectListener mWheelClickLitener;
    private WheelView wv_selectMonth;
    private WheelView wv_selectDay;
    private TextView area_tv_ok;
    private TextView area_tv_cancel;
    private View view;
    private ArrayWheelAdapter mMonthAdapter;
    private ListWheelAdapter mDayAdapter;
    private String[] mSystemMonths;
    private List<String> mSystemDays;
    private int mSelectMonth  = 1;
    private int mSelectDay    = 1;
    private int mCurrentYear  = 0;
    private int mCurrentToday = 0;  //今天是这个月的第几天
    private int mOldChooseDay = 0;     //选择的天数
    private boolean isFristed = false;  //判断是不是第一次刷新

    private SimpleDateFormat mFormat = new SimpleDateFormat("MM");
    private SimpleDateFormat mDayFormat = new SimpleDateFormat("dd");

    public DateSelectDialog(@NonNull Context context) {
        super(context);
    }

    public DateSelectDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.transparentFrameWindowStyle);
    }

    public DateSelectDialog(Context context, IDateSelectListener listener) {
        super(context, R.style.transparentFrameWindowStyle);
        mContext = context;
        mWheelClickLitener = listener;
        view = View.inflate(context, R.layout.dialog_select_month_and_day, null);
        setContentView(view);
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.Dialog_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wl.x = 0;
        wl.y = wm.getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
        initView();
        initData();
    }

    public void initData(){
        mSystemDays   = new ArrayList<>();
        mCurrentYear  = CaldenarUtil.INSTANCE.getCurrentYear();
        mSelectMonth  = CaldenarUtil.INSTANCE.getCurrentMonth();    //获取当前月
        mSystemMonths = mContext.getResources().getStringArray(R.array.date_months);
        int maxDay      = CaldenarUtil.INSTANCE.getMonthLastDay(mCurrentYear,mSelectMonth);
        refreshDayAdapter(maxDay);

        String index = mFormat.format(new Date(System.currentTimeMillis()));
        String today = mDayFormat.format(new Date(System.currentTimeMillis()));
        mCurrentToday = Integer.valueOf(today);

        wv_selectMonth.invalidateWheel(true);
        mMonthAdapter = new ArrayWheelAdapter<>(mContext,mSystemMonths);
        wv_selectMonth.setViewAdapter(mMonthAdapter);
        wv_selectMonth.setCurrentItem(Integer.valueOf(index) -1);

        wv_selectMonth.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int maxDay = CaldenarUtil.INSTANCE.getMonthLastDay(mCurrentYear,newValue+1);
                mSelectMonth = newValue+1;
                refreshDayAdapter(maxDay);
            }
        });

        wv_selectDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                mSelectDay = newValue +1;
                mOldChooseDay = mSelectDay;
            }
        });
    }

    private void refreshDayAdapter(int maxDay){
        mSystemDays.clear();
        for (int i=1;i<=maxDay;i++){
            mSystemDays.add(String.valueOf(i));
        }
        mDayAdapter =  new ListWheelAdapter(mContext,mSystemDays);
        wv_selectDay.setViewAdapter(mDayAdapter);

        if(isFristed){
            isFristed = false;        //第一次选择当月
            wv_selectDay.setCurrentItem(mCurrentToday-1);
        }else{
            if(maxDay < mOldChooseDay){
                wv_selectDay.setCurrentItem(maxDay-1);
                mOldChooseDay = maxDay;
            }
        }
    }

    private void initView() {
        wv_selectMonth = view.findViewById(R.id.wv_selectDateMonth);
        wv_selectDay   = view.findViewById(R.id.wv_selectDateDay);
        area_tv_ok = view.findViewById(R.id.tv_ok);
        area_tv_cancel = view.findViewById(R.id.tv_cancel);
        area_tv_ok.setOnClickListener(this);
        area_tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok) {
            if(mWheelClickLitener != null){
                mWheelClickLitener.sure(mSelectMonth,mSelectDay);
            }
            dismiss();
        } else if (v.getId() == R.id.tv_cancel) {
            dismiss();
        }
    }

    public interface IDateSelectListener {
        void sure(int sureMonth,int sureDay);
        void cancel();
    }
}
