package com.project.app.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import butterknife.OnClick
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hb.basemodel.config.Constant
import com.hb.basemodel.utils.SPManager
import com.project.app.R
import com.project.app.base.BaseActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton

/**
 * 功能介绍
 */
class FeatureGuideActivity : BaseActivity() {
    lateinit var btn_skipFeatureGuide: QMUIRoundButton
    lateinit var vp_feature:ViewPager2
    lateinit var mAdapter:FeatureGuideAdapter
    var mCurrentIndex = 0
    var mGuideCovers:List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QMUIStatusBarHelper.setStatusBarLightMode(this)
        setContentView(R.layout.activity_feature_guide)
        btn_skipFeatureGuide = findViewById(R.id.btn_skipFeatureGuide)
        vp_feature = findViewById(R.id.vp_feature)
        initWidget()
        initViewPager2()
    }

    private fun initWidget() {
        SPManager.sPutBoolean(Constant.SP_INIT_GUIDE_FEATURE,true);
        btn_skipFeatureGuide.setChangeAlphaWhenPress(true)
        btn_skipFeatureGuide.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initViewPager2() {
        mGuideCovers= resources.getStringArray(R.array.guide_feature).toList()
        mAdapter = FeatureGuideAdapter(this, mGuideCovers)
        vp_feature.adapter = mAdapter
        vp_feature.offscreenPageLimit = mGuideCovers.size
        vp_feature.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vp_feature.setCurrentItem(mCurrentIndex)
        vp_feature.isUserInputEnabled = true
    }

    class FeatureGuideAdapter(var mContext: Context, var datas: List<String>): RecyclerView.Adapter<BaseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_guide_feature, parent, false)
            return BaseViewHolder(view)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            when(position){
                0 -> {
                    holder.getView<TextView>(R.id.tv_featureTitle).setText(R.string.guide_feature_title_one)
                    holder.getView<TextView>(R.id.tv_featureSummary).setText(R.string.guide_feature_summary_one)
                    holder.getView<ImageView>(R.id.iv_featurePlaceHolder).setImageResource(R.mipmap.ic_guide_feature_one)
                    initIndicator(holder.getView(R.id.ll_featureIndicatorPlaceHolder), 0)
                }
                1 -> {
                    holder.getView<TextView>(R.id.tv_featureTitle).setText(R.string.guide_feature_title_two)
                    holder.getView<TextView>(R.id.tv_featureSummary).setText(R.string.guide_feature_summary_two)
                    holder.getView<ImageView>(R.id.iv_featurePlaceHolder).setImageResource(R.mipmap.ic_guide_feature_two)
                    initIndicator(holder.getView(R.id.ll_featureIndicatorPlaceHolder), 1)
                }
                2 -> {
                    holder.getView<TextView>(R.id.tv_featureTitle).setText(R.string.guide_feature_title_three)
                    holder.getView<TextView>(R.id.tv_featureSummary).setText(R.string.guide_feature_summary_three)
                    holder.getView<ImageView>(R.id.iv_featurePlaceHolder).setImageResource(R.mipmap.ic_guide_feature_three)
                    initIndicator(holder.getView(R.id.ll_featureIndicatorPlaceHolder), 2)
                }
                3 -> {
                    holder.getView<TextView>(R.id.tv_featureTitle).setText(R.string.guide_feature_title_four)
                    holder.getView<TextView>(R.id.tv_featureSummary).setText(R.string.guide_feature_summary_four)
                    holder.getView<ImageView>(R.id.iv_featurePlaceHolder).setImageResource(R.mipmap.ic_guide_feature_four)
                    initIndicator(holder.getView(R.id.ll_featureIndicatorPlaceHolder), 3)
                }
            }
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        private fun initIndicator(linearHoder: LinearLayout, isEnableIndex: Int) {
           var vIndicator: View
           for(cell in datas){
               vIndicator = View(mContext)
               vIndicator.setBackgroundResource(R.drawable.select_feature_guide_indicator_style)
               vIndicator.isEnabled = false
               var lp:LinearLayout.LayoutParams = LinearLayout.LayoutParams(18, 18)
               lp.marginStart = 10
               lp.marginEnd = 10
               vIndicator.layoutParams = lp
               linearHoder.addView(vIndicator)
           }
            linearHoder.getChildAt(isEnableIndex).isEnabled = true
        }
    }


}