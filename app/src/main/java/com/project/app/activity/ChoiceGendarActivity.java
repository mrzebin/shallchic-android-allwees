package com.project.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.app.R;
import com.project.app.base.BaseActivity;
import com.project.app.utils.StatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceGendarActivity extends BaseActivity {
    @BindView(R.id.ll_inflate)
    LinearLayout ll_inflate;
    @BindView(R.id.iv_checkBoxMan)
    ImageView iv_checkBoxMan;
    @BindView(R.id.iv_checkBoxWomen)
    ImageView iv_checkBoxWomen;
    @BindView(R.id.tv_sexMan)
    TextView tv_sexMan;
    @BindView(R.id.tv_sexWomen)
    TextView tv_sexWomen;
    @BindView(R.id.cl_gendarMan)
    ConstraintLayout cl_gendarMan;
    @BindView(R.id.cl_gendarWomen)
    ConstraintLayout cl_gendarWomen;
    @BindView(R.id.tv_skipChoiceGendar)
    TextView tv_skipChoiceGendar;
    @BindView(R.id.btn_skipChoiceGendar)
    QMUIRoundButton btn_skipChoiceGendar;

    private Context mContext;
    private int GENDAR_DEFAULT = 0;
    private static final int GENDAR_MODEL_MAN = 0; //选择男
    private static final int GENDAR_MODEL_WOMEN = 1; //选择女
    private List<ImageView> boxIcons = new ArrayList<>();
    private  List<TextView> boxNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_choice_gendar);
        ButterKnife.bind(this);
        initData();
        initWidget();
    }

    private void initData() {
        boxIcons.add(iv_checkBoxMan);
        boxIcons.add(iv_checkBoxWomen);
        boxNames.add(tv_sexMan);
        boxNames.add(tv_sexWomen);
    }

    private void initWidget() {
        mContext = this;
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        StatusBarUtils.setStatusBarView(this,ll_inflate);
        btn_skipChoiceGendar.setChangeAlphaWhenPress(true);
    }

    @OnClick({R.id.cl_gendarMan,R.id.cl_gendarWomen,R.id.tv_skipChoiceGendar,R.id.btn_skipChoiceGendar})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.cl_gendarMan:
                GENDAR_DEFAULT = GENDAR_MODEL_MAN;
                choiceGendarByPeople();
                break;
            case R.id.cl_gendarWomen:
                GENDAR_DEFAULT = GENDAR_MODEL_WOMEN;
                choiceGendarByPeople();
                break;
            case R.id.tv_skipChoiceGendar:
            case R.id.btn_skipChoiceGendar:
                startActivity(new Intent(mContext,MainActivity.class));
                finish();
                break;
        }
    }

    private void choiceGendarByPeople(){
        for(ImageView item:boxIcons){
            item.setSelected(false);
        }
        for(TextView itemName:boxNames){
            itemName.setTextColor(mContext.getResources().getColor(R.color.color_333));
        }
        boxIcons.get(GENDAR_DEFAULT).setSelected(true);
        boxNames.get(GENDAR_DEFAULT).setTextColor(mContext.getResources().getColor(R.color.allwees_theme_color));
    }
}
