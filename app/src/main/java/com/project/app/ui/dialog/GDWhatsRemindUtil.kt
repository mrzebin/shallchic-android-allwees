package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.hb.basemodel.config.Constant
import com.hb.basemodel.utils.SPManager
import com.hb.basemodel.utils.ToastUtil
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import com.project.app.R
import com.project.app.adapter.NewFreeGiftAdapter
import com.project.app.utils.LocaleUtil
import com.project.app.utils.WhatsAppUtil
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * create by hb
 * whats 提示
 */
class GDWhatsRemindUtil {
    lateinit var mContext: WeakReference<Context>
    lateinit var mDialog: Dialog
    lateinit var mView: View
    lateinit var iv_checkAgainPrompt:ImageView
    lateinit var tv_cancelReminder:TextView
    lateinit var tv_goWhats:TextView
    lateinit var mListener:ICheckInstallWhats
    var isCheck:Boolean = false

    constructor(context: Context, isCancelable: Boolean, isCancelOutside: Boolean) {
        this.mContext = WeakReference<Context>(context)
        this.mDialog = Dialog(context, R.style.BottomDialog)
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build() {
        mView = LayoutInflater.from(mContext.get()).inflate(R.layout.dialog_whats_remind, null)
        mDialog.setContentView(mView)
        val wManager = mContext.get()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window = mDialog.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.CENTER)
        wlp.width = (display.width *0.9).toInt()
        initWidget()
    }

    private fun initWidget() {
        tv_goWhats        = mView.findViewById(R.id.tv_goWhats)
        tv_cancelReminder = mView.findViewById(R.id.tv_cancelReminder)
        iv_checkAgainPrompt = mView.findViewById(R.id.iv_checkAgainPrompt)

        iv_checkAgainPrompt.setOnClickListener{
            iv_checkAgainPrompt.isSelected = !isCheck
            isCheck = !isCheck
            SPManager.sPutBoolean(Constant.SP_NEED_REMIND_LINK_WHATS,isCheck)
        }

        tv_goWhats.setOnClickListener{
            mListener?.checkInstallWhatsApp()
            dismiss()
        }

        tv_cancelReminder.setOnClickListener{
            dismiss()
        }
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

    interface ICheckInstallWhats{
        fun checkInstallWhatsApp();
    }

}