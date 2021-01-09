package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import com.project.app.R
import com.project.app.utils.LocaleUtil

/**
 * create by hb
 * Description
 */
class GDetailDeliveryUtil {
    lateinit var mContext: Context
    lateinit var mDialog: Dialog
    lateinit var mView: View
    lateinit var iv_closeRefund: ImageView
    lateinit var qmui_addToCart: QMUIRoundButton
    lateinit var tv_estimePrice: TextView
    lateinit var tv_estimeDay: TextView
    lateinit var mListener: DGoBuyListener
    var mSymbol:String = ""

    constructor(context: Context, isCancelable: Boolean, isCancelOutside: Boolean, listener: DGoBuyListener) {
        this.mContext = context
        this.mDialog = Dialog(context, R.style.BottomDialog)
        this.mListener = listener
        this.mSymbol = LocaleUtil.getInstance().symbole
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_gddelivery, null)
        mDialog.setContentView(mView)
        val wManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window = mDialog.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.BOTTOM)
        wlp.width = display.width
//        window.attributes = wlp
        iv_closeRefund = mView.findViewById(R.id.iv_closeRefund)
        tv_estimeDay = mView.findViewById(R.id.tv_estimeDay)
        tv_estimePrice = mView.findViewById(R.id.tv_estimePrice)
        iv_closeRefund.setOnClickListener({
            dismiss()
        })
        qmui_addToCart = mView.findViewById(R.id.qmui_addToCart)
        qmui_addToCart.setChangeAlphaWhenPress(true)
    }

    fun show(estimeDay: String, estimeP: String,shippingMinDay:String,shippingMaxDay:String) {
        val unitDay:String = mContext.resources.getString(R.string.str_add);

        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
        qmui_addToCart.setOnClickListener({
            dismiss()
            mListener.gotoBuy()
        })
        if(!TextUtils.isEmpty(shippingMinDay) && !TextUtils.isEmpty(shippingMaxDay)){
            tv_estimeDay.text = shippingMinDay + "-" + shippingMaxDay + " " + unitDay
        }else{
            tv_estimeDay.text = estimeDay
        }
        tv_estimePrice.text = mSymbol + estimeP
        mDialog.show()
    }

    fun dismiss() {
        mDialog.dismiss()
    }

    fun clear() {
        mDialog.dismiss()
    }

}