package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.hb.basemodel.config.api.UrlConfig
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import com.project.app.R
import com.project.app.activity.HolderActivity
import com.project.app.fragment.WebExplorerFragment
import com.project.app.utils.LocaleUtil


/**
 * create by hb
 * 30 days refund
 */
class RefundDialogUtil {
    lateinit var mContext:Context
    lateinit var mDialog:Dialog
    lateinit var mView: View
    lateinit var iv_closeRefund:ImageView
    lateinit var qmui_addToCart:QMUIRoundButton
    lateinit var mListener: DGoBuyListener
    lateinit var tv_bookRec:TextView

    constructor(context: Context,isCancelable:Boolean,isCancelOutside :Boolean,listener: DGoBuyListener){
        this.mContext = context
        this.mDialog = Dialog(context, R.style.BottomDialog)
        this.mListener = listener
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_refund_layout,null)
        mDialog.setContentView(mView)
        val wManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window  = mDialog.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.BOTTOM)
        wlp.width = display.width
//        window.attributes = wlp
        iv_closeRefund = mView.findViewById(R.id.iv_closeRefund)
        tv_bookRec     = mView.findViewById(R.id.tv_bookRec)
        iv_closeRefund.setOnClickListener({
            dismiss()
        })

        var skipRp = mContext.resources.getString(R.string.goods_detail_skip)

        tv_bookRec.setOnClickListener({
            dismiss()
            val bundle = Bundle()
            var siteUrl = ""
            if(LocaleUtil.getInstance().language.equals("en")){
                siteUrl =  UrlConfig.RETURN_POLICY_EN;
            }else{
                siteUrl =  UrlConfig.RETURN_POLICY;
            }
            bundle.putString("type","1")
            bundle.putString("webUrl", siteUrl)
            bundle.putString("title", skipRp)
            val intent = HolderActivity.of(mContext, WebExplorerFragment::class.java, bundle)
            mContext.startActivity(intent)
        })

        qmui_addToCart = mView.findViewById(R.id.qmui_addToCart)
        qmui_addToCart.setChangeAlphaWhenPress(true)
    }

    fun show(){
        if(mDialog.isShowing){
            mDialog.dismiss()
        }
        qmui_addToCart.setOnClickListener({
            mDialog.dismiss()
            mListener.gotoBuy()
        })
        mDialog.show()
    }

    fun dismiss(){
        mDialog.dismiss()
    }

    fun clear() {
        mDialog.dismiss()
    }

}