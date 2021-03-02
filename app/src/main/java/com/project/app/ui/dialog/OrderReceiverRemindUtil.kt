package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import com.project.app.R
import com.project.app.adapter.NewFreeGiftAdapter
import com.project.app.utils.LocaleUtil
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * create by hb
 * 订单接收弹窗
 */
class OrderReceiverRemindUtil {
    lateinit var mContext: WeakReference<Context>
    lateinit var mDialog: Dialog
    lateinit var mView: View
    lateinit var iv_close:ImageView
    lateinit var btn_sureReceive:QMUIRoundButton
    lateinit var tv_cancelReceive:TextView
    lateinit var mListener:IReceiveListener
    var orderUuid:String = ""

    constructor(context: Context, isCancelable: Boolean, isCancelOutside: Boolean) {
        this.mContext = WeakReference<Context>(context)
//        this.mDialog = Dialog(context, R.style.BottomDialog)
        this.mDialog = Dialog(context)
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build() {
        mView = LayoutInflater.from(mContext.get()).inflate(R.layout.dialog_order_recevie_remind, null)
        mDialog.setContentView(mView)
        val wManager = mContext.get()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window = mDialog.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.CENTER)
        wlp.width = (display.width *0.8).toInt()
        initWidget()
    }

    private fun initWidget() {
        iv_close = mView.findViewById(R.id.iv_close)
        btn_sureReceive = mView.findViewById(R.id.btn_sureReceive)
        tv_cancelReceive = mView.findViewById(R.id.tv_cancelReceive)
        btn_sureReceive.setChangeAlphaWhenPress(true)

        iv_close.setOnClickListener{
            dismiss()
        }
        btn_sureReceive.setOnClickListener{
            if(mListener != null){
                mListener.sureReceive(orderUuid)
            }
        }
        tv_cancelReceive.setOnClickListener{
            dismiss()
        }
    }

    fun dismiss() {
        mDialog?.dismiss()
    }

    fun clear() {
        mDialog?.dismiss()
    }

    fun show(uuid:String) {
        this.orderUuid = uuid
        mDialog?.show()
    }

    interface IReceiveListener{
        fun sureReceive(uuid:String)
    }
}