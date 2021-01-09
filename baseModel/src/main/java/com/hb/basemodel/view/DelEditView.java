package com.hb.basemodel.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.basemodel.R;
import com.hb.basemodel.utils.Unit;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 带一键删除的editview
 *
 * create by hb
 */
public class DelEditView extends LinearLayout {
    private Context mContext;
    private AppCompatImageView leftIconIv;
    private Drawable mLeftIcon;
    private int mMarginLeft;
    private int mMarginRight;
    private String mHint;

    //editText相关
    public AppCompatEditText editText;
    private String etHint;
    private int etTextSize;
    private ColorStateList etTextColor;
    private Drawable editBackground;
    private Drawable llBackground;

    //清除按钮相关
    public AppCompatImageView clearIconIv;
    private boolean deletable;
    private Drawable deleteIconSrc;


    private boolean isSingleLine = false;
    private boolean isDeleteAble = false;
    private Drawable mDeleteDrawable;

    private int defaultPadding = 5;
    private int defaultSize = 14;
    private int defaultMargin = 15;
    private int defaultHeight = 40;
    private int defaultMaxEms = 15;   //输入editview的长度
    private int defaultEtHeight = 40;

    private int defaultSearchAction = 3;   //默认是搜索


    public DelEditView(Context context) {
        this(context,null);
    }

    public DelEditView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public DelEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        initPaint();
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.DelEditView);
        isDeleteAble = ta.getBoolean(R.styleable.DelEditView_del_deleteAble,false);
        mDeleteDrawable = ta.getDrawable(R.styleable.DelEditView_del_deleteSrc);
        mLeftIcon       = ta.getDrawable(R.styleable.DelEditView_del_LeftIconSrc);

        mMarginLeft = ta.getInteger(R.styleable.DelEditView_del_MarginLeft,0);
        mMarginRight = ta.getInteger(R.styleable.DelEditView_del_MarginRight,0);
        editBackground = ta.getDrawable(R.styleable.DelEditView_del_etBackground);
        llBackground   = ta.getDrawable(R.styleable.DelEditView_del_background);

        etTextSize = ta.getDimensionPixelSize(R.styleable.DelEditView_del_etTextSize,defaultSize);
        etTextColor = ta.getColorStateList(R.styleable.DelEditView_del_etTextColor);
        isSingleLine = ta.getBoolean(R.styleable.DelEditView_delIsSingleLine,true);
        defaultMaxEms = ta.getInteger(R.styleable.DelEditView_del_maxEms,defaultMaxEms);
        mHint = ta.getString(R.styleable.DelEditView_del_etHint);
        defaultEtHeight = ta.getDimensionPixelOffset(R.styleable.DelEditView_del_etHeight,defaultEtHeight);
        defaultSearchAction    = ta.getInteger(R.styleable.DelEditView_del_searchIme,defaultSearchAction);
    }

    private void initPaint() {

    }

    private void initView() {
        initSuperEidtText();
        initLeftIcon();
        initEditText();
        initClearIcon();
    }

    private void initSuperEidtText() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setPadding(Unit.dp2px(mContext,5),0,Unit.dp2px(mContext,5),0);
        setLayoutParams(params);
        if(llBackground != null){
            setBackground(llBackground);
        }
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initLeftIcon() {
        if(leftIconIv == null){
            leftIconIv = new AppCompatImageView(mContext);
        }
        LayoutParams leftIconParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        leftIconIv.setId(R.id.sLeftIconIv);
        leftIconIv.setLayoutParams(leftIconParams);
        if(mLeftIcon != null){
            leftIconIv.setImageDrawable(mLeftIcon);
        }else{
            leftIconIv.setVisibility(GONE);
        }
        addView(leftIconIv);
    }

    private void initEditText() {
        if(editText == null){
            editText = new AppCompatEditText(mContext);
        }
        LayoutParams editParam = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        editParam.weight = 1;
        editParam.setMarginStart(Unit.sp2px(mContext,mMarginLeft));
        editText.setLayoutParams(editParam);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, etTextSize);
        editText.setSingleLine(isSingleLine);
        editText.setTextColor(etTextColor);
        editText.setHint(mHint);
        editText.setEllipsize(TextUtils.TruncateAt.END);
        editText.setFilters(new InputFilter[]{new MaxTextLenghtFilter(defaultMaxEms)});
        editText.setImeOptions(defaultSearchAction); //设置搜索

        if(editBackground != null){
            editText.setBackgroundDrawable(editBackground);
        }else{
            editText.setBackgroundDrawable(null);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(editable) && clearIconIv.getVisibility() == View.GONE && isDeleteAble){
                    clearIconIv.setVisibility(VISIBLE);
                }else if(TextUtils.isEmpty(editable)){
                    clearIconIv.setVisibility(GONE);
                }
                if(TextUtils.isEmpty(editable.toString())){
                    if(listener != null){
                        listener.editClearDataObserve();
                    }
                    return;
                }else{
                    if(listener != null){
                        listener.editHasDataObserve();
                    }
                }
            }
        });
        addView(editText);
    }

    private void initClearIcon() {
        if(clearIconIv == null){
            clearIconIv = new AppCompatImageView(mContext);
        }
        LayoutParams params = new LinearLayout.LayoutParams(Unit.dp2px(mContext,15),Unit.dp2px(mContext,15));
        clearIconIv.setId(R.id.sClearIconIv);
        clearIconIv.setLayoutParams(params);
        params.setMarginStart(Unit.sp2px(mContext,5));
        clearIconIv.setVisibility(GONE);
        if(deleteIconSrc == null){
            deleteIconSrc = mContext.getResources().getDrawable(R.drawable.ic_clear);
        }
        clearIconIv.setImageDrawable(deleteIconSrc);
        clearIconIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                if(listener != null){
                    listener.editClearDataObserve();
                }
            }
        });
        addView(clearIconIv);
    }

    public void clearText() {
        editText.setText("");
    }

    class MaxTextLenghtFilter implements InputFilter{
        private int mMaxLength;

        public MaxTextLenghtFilter(int max){
            mMaxLength = max - 1;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int keep = mMaxLength - (dest.length() - (dend - dstart));
            if(keep < (end - start)){
                /*todo 可以吐司*/
            }
            if(keep <= 0){
                return "";
            }else if(keep >= end - start){
                return null;
            }else{
                return source.subSequence(start,start + keep);
            }
        }
    }

    public DelObserveListener listener;

    public DelObserveListener getListener() {
        return listener;
    }

    public void setListener(DelObserveListener listener) {
        this.listener = listener;
    }

    public interface DelObserveListener{
        void editClearDataObserve();      //清理edittext监听
        void editHasDataObserve();        //有数据输入
    }

}
