package com.project.app.fragment.help

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
class ExceptReceiveSmsCodeFragment:BaseMvpQmuiFragment<CashBackRecommendPresenter?>(),CashBackRecommendContract.View{
    @JvmField
    @BindView(R.id.tv_cashBackDescribe)
     var tv_cashBackDescribe:TextView? = null

    @JvmField
    @BindView(R.id.iv_back)
    var iv_back:ImageView? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_except_receive_sms_code
    }

    override fun initView() {

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