package com.project.app.fragment.cashback

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.BindViews
import butterknife.OnClick
import com.project.app.R
import com.project.app.base.BaseMvpQmuiFragment
import com.project.app.contract.CashBackRecommendContract
import com.project.app.presenter.CashBackRecommendPresenter

/**
 * 购物返现金的介绍
 */
class CashBackRecommendFragment:BaseMvpQmuiFragment<CashBackRecommendPresenter?>(),CashBackRecommendContract.View{
    @JvmField
    @BindView(R.id.tv_cashBackDescribe)
     var tv_cashBackDescribe:TextView? = null

    @JvmField
    @BindView(R.id.iv_back)
    var iv_back:ImageView? = null

    override fun getLayoutId(): Int {
        return R.layout.cashback_recommend
    }

    override fun initView() {
        var linkPrefix = context?.resources?.getString(R.string.cash_back_how_work_describe_prefix)
        var linkSurfix = context?.resources?.getString(R.string.cash_back_how_work_describe_surfix);
        var linkCashBackRate = context?.resources?.getString(R.string.cash_back_rate)
        tv_cashBackDescribe?.setText(Html.fromHtml(linkPrefix +"<font color=#31B5ED>" + linkCashBackRate + "</font>"+ linkSurfix))
    }

    override fun lazyFetchData() {

    }

    override fun startLoading() {

    }

    override fun stopLoading() {

    }

    override fun showErrorTip(msg: String?) {

    }

    @OnClick(R.id.iv_back)
    fun onClickViewed(view: View){
        when(view.id){
            R.id.iv_back->{
                popBackStack()
            }
        }
    }
}