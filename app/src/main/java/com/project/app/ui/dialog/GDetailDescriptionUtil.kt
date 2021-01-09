package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.Html
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import com.project.app.R

/**
 * create by hb
 * Description
 */
class GDetailDescriptionUtil {
    lateinit var mContext:Context
    lateinit var mDialog:Dialog
    lateinit var mView: View
    lateinit var tv_gdDescribte:TextView
    lateinit var iv_closeRefund:ImageView
    lateinit var qmui_addToCart:QMUIRoundButton
    lateinit var listener:DGoBuyListener

    constructor(context: Context, isCancelable: Boolean, isCancelOutside: Boolean, listener: DGoBuyListener){
        this.mContext = context
        this.mDialog  = Dialog(context, R.style.BottomDialog)
        this.listener = listener
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }
    fun build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_gdetail_description_layout,null)
        mDialog.setContentView(mView)
        val wManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window  = mDialog.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.BOTTOM)
        wlp.width = display.width
//        window.attributes = wlp
        iv_closeRefund = mView.findViewById(R.id.iv_closeRefund)
        tv_gdDescribte = mView.findViewById(R.id.tv_gdDescribte)
        iv_closeRefund.setOnClickListener({
            dismiss()
        })
        qmui_addToCart = mView.findViewById(R.id.qmui_addToCart)
        qmui_addToCart.setChangeAlphaWhenPress(true)
    }

    fun show(desContent:String){
        if(mDialog.isShowing){
            mDialog.dismiss()
        }

      var htmlFormat:String = "<html><head></head><body>\\%s</body></html>";
        tv_gdDescribte.setText(Html.fromHtml(String.format(htmlFormat,desContent)));
//        tv_gdDescribte.text = desContent
        qmui_addToCart.setOnClickListener({
            mDialog.dismiss()
            listener.gotoBuy()
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