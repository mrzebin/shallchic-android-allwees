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
 * review接收弹窗
 */
class OrderReviewRemindUtil {
    lateinit var mContext: WeakReference<Context>
    lateinit var mDialog: Dialog
    lateinit var mView: View
    lateinit var tv_fromAlbum:TextView
    lateinit var tv_takePhoto:TextView
    lateinit var tv_cancel:TextView
    lateinit var mListener:IReviewListener

    constructor(context: Context, isCancelable: Boolean, isCancelOutside: Boolean) {
        this.mContext = WeakReference<Context>(context)
        this.mDialog = Dialog(context)
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build() {
        mView = LayoutInflater.from(mContext.get()).inflate(R.layout.dialog_order_review_remind, null)
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
        tv_fromAlbum = mView.findViewById(R.id.tv_fromAlbum)
        tv_takePhoto = mView.findViewById(R.id.tv_takePhoto)
        tv_cancel    = mView.findViewById(R.id.tv_cancel)

        tv_fromAlbum.setOnClickListener{
            mListener?.fromAlbum()
            dismiss()
        }

        tv_takePhoto.setOnClickListener {
            mListener?.takePhoto()
            dismiss()
        }

        tv_cancel.setOnClickListener {
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

    interface IReviewListener{
        fun fromAlbum();
        fun takePhoto();
    }
}