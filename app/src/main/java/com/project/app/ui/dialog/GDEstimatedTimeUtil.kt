package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.*
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
 * estimated time dialog
 */
class GDEstimatedTimeUtil {
    lateinit var mContext: WeakReference<Context>
    lateinit var mDialog: Dialog
    lateinit var mView: View
    lateinit var rbtn_confirm:QMUIRoundButton
    lateinit var tv_estimateDurationTime:TextView
    lateinit var iv_close:ImageView

    constructor(context: Context, isCancelable: Boolean, isCancelOutside: Boolean) {
        this.mContext = WeakReference<Context>(context)
        this.mDialog = Dialog(context, R.style.BottomDialog)
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build() {
        mView = LayoutInflater.from(mContext.get()).inflate(R.layout.dialog_estimated_time, null)
        mDialog.setContentView(mView)
        val wManager = mContext.get()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window = mDialog.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.BOTTOM)
        wlp.width = display.width
        initWidget()
    }

    private fun initWidget() {
        rbtn_confirm = mView.findViewById(R.id.rbtn_confirm)
        tv_estimateDurationTime = mView.findViewById(R.id.tv_estimateDurationTime)
        iv_close = mView.findViewById(R.id.iv_close)
        rbtn_confirm.setChangeAlphaWhenPress(true)
        rbtn_confirm.setOnClickListener{
            mDialog?.dismiss()
        }
        iv_close.setOnClickListener{
            mDialog?.dismiss()
        }
        var simpleFormat = SimpleDateFormat("yyyy/MM/dd")
        var timeSb = StringBuffer()
        var c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH,6);
        timeSb.append(simpleFormat.format(c.time))
        timeSb.append("-")
        c.add(Calendar.DAY_OF_MONTH,10)  //在加10天
        timeSb.append(simpleFormat.format(c.time))
        tv_estimateDurationTime.text = timeSb.toString()
    }

    fun dismiss() {
        mDialog?.dismiss()
    }

    fun clear() {
        mDialog?.dismiss()
    }

    fun show() {
        mDialog?.show()
    }

}