package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.ImageView
import com.hb.basemodel.config.Constant
import com.hb.basemodel.image.ImageLoader
import com.project.app.R
import com.project.app.activity.HolderActivity
import java.util.*

/**
 * 购买成功的动画
 */
class GDBuyPopupDialogUtil {
    lateinit var mContext:Context
    var mDialog:Dialog ?= null
    lateinit var mView:View
    lateinit var iv_thumb:ImageView

    constructor(context: Context,isCancelable:Boolean,isCancelOutside :Boolean){
        this.mContext = context
        this.mDialog = Dialog(context, R.style.BottomDialog)
        mDialog!!.setCancelable(isCancelable)
        mDialog!!.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_buy_success_popup,null)
        mDialog!!.setContentView(mView)
        val wManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window  = mDialog?.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.BOTTOM)
        wlp.width = (display.width*0.9).toInt()
        window.attributes = wlp
        iv_thumb = mView.findViewById(R.id.iv_thumb)
    }

    fun show(){
        if(mDialog == null){
            return
        }
        if(mDialog!!.isShowing){
            mDialog!!.dismiss()
        }
        var timer:Timer = Timer()
        mDialog!!.show()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                if(mDialog != null && mDialog!!.isShowing()){
                    dismiss()
                }
            }
        }
        timer.schedule(task,800)
    }

    fun lazzyImageUrl(url:String){
        ImageLoader.getInstance().displayImage(iv_thumb, url, R.mipmap.allwees_ic_default_goods)
    }

    fun dismiss(){
        mDialog!!.dismiss()
    }

    fun clear() {
        mDialog?.dismiss()
        mDialog = null
    }

}