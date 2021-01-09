package com.project.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.hb.basemodel.config.Constant
import com.hb.basemodel.image.ImageLoader
import com.hb.basemodel.utils.DataUtil
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import com.project.app.R
import com.project.app.bean.GoodsDetailInfoBean.SkusItem
import com.project.app.utils.MathUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * create by hb
 */
class GDChooiceDialogUtil {
    lateinit var mContext:Context
    lateinit var mDialog:Dialog
    lateinit var mView: View
    lateinit var iv_close:ImageView
    lateinit var iv_goodThumb:ImageView
    lateinit var tv_gdOrignPrice:TextView
    lateinit var tv_gdRetailprice:TextView
    lateinit var tv_cGdColor:TextView
    lateinit var tv_inAdd:TextView
    lateinit var tv_inDes:TextView
    lateinit var tv_buyCount:TextView
    lateinit var qmui_addToCart:QMUIRoundButton
    lateinit var gv_sizes: com.project.app.ui.widget.GoodsViewGroup<TextView>
    lateinit var gv_color: com.project.app.ui.widget.GoodsViewGroup<TextView>
    lateinit var mSkus: com.project.app.bean.GoodsDetailInfoBean.Skus
    lateinit var mCoverPhoto:String
    lateinit var mListener: DInjectListener
    var mSoliderMap:HashMap<String, SkusItem> = HashMap()
    var mColorDatas:ArrayList<String> = ArrayList()
    var mSizeDatas:ArrayList<String> = ArrayList()
    var mChoiceColorId:Int = 0
    var mChoiceSizeId:Int  = 0
    var mBuyCount = 1
    var isDisplay:Boolean = false   //默认显示


    constructor(context: Context,isCancelable:Boolean,isCancelOutside :Boolean,listener: DInjectListener){
        this.mContext = context
        this.mDialog = Dialog(context, R.style.BottomDialog)
        this.mListener = listener
        mDialog.setCancelable(isCancelable)
        mDialog.setCanceledOnTouchOutside(isCancelOutside)
        build()
    }

    fun build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_gdchoice,null)
        mDialog.setContentView(mView)
        val wManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wManager.defaultDisplay as Display
        val window  = mDialog.window
        val wlp = window!!.attributes
        window.setGravity(Gravity.BOTTOM)
        wlp.width = display.width
//        window.attributes = wl
        iv_close = mView.findViewById(R.id.iv_close)
        iv_close.setOnClickListener({
            dismiss()
        })
        qmui_addToCart   = mView.findViewById(R.id.qmui_addToCart)
        tv_gdOrignPrice  = mView.findViewById(R.id.tv_gdOrignPrice)
        tv_gdRetailprice = mView.findViewById(R.id.tv_gdRetailprice)
        tv_cGdColor      = mView.findViewById(R.id.tv_cGdColor)
        iv_goodThumb     = mView.findViewById(R.id.iv_goodThumb)
        tv_inDes         = mView.findViewById(R.id.tv_inDes)
        tv_inAdd         = mView.findViewById(R.id.tv_inAdd)
        tv_buyCount      = mView.findViewById(R.id.tv_buyCount)
        tv_gdOrignPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        qmui_addToCart.setChangeAlphaWhenPress(true)
        tv_inAdd.setOnClickListener({
            mBuyCount++
            tv_buyCount.text = "" + mBuyCount
            getTargetGoodsSuku()
        })
        tv_inDes.setOnClickListener({
            mBuyCount--
            if(mBuyCount <= 1){
                mBuyCount = 1
            }
            tv_buyCount.text = "" + mBuyCount
            getTargetGoodsSuku()
        })
        qmui_addToCart.setOnClickListener({
            val skuuid:String = getChoiceGoodsId()
            if(!TextUtils.isEmpty(skuuid)){
                mListener.buy(mBuyCount,true,skuuid)
                dismiss()
            }else{
                dismiss()
            }
        })
        gv_sizes = mView.findViewById(R.id.gv_sizes)
        gv_color = mView.findViewById(R.id.gv_colors)
    }

    private fun getChoiceGoodsId(): String {
        val choiceColor = mColorDatas[mChoiceColorId]
        val choiceSize:String   = mSizeDatas[mChoiceSizeId]
        if(mSoliderMap.containsKey(choiceColor+choiceSize)){
           val item: SkusItem = mSoliderMap[choiceColor+choiceSize]!!
           return item.uuid
        }
        return ""
    }

    private fun getTargetGoodsSuku(){
        val choiceColor = mColorDatas[mChoiceColorId]
        val choiceSize:String   = mSizeDatas[mChoiceSizeId]
        if(mSoliderMap.containsKey(choiceColor+choiceSize)){
           val item:SkusItem = mSoliderMap[choiceColor+choiceSize]!!
            displayChoiceGoods(item)
        }
    }

    fun syncData(data : com.project.app.bean.GoodsDetailInfoBean.Skus, mainCover:String) {
        if (data != null) {
            if(!DataUtil.idNotNull(data.skus)){
                return
            }
            this.mSkus = data
            this.mCoverPhoto = mainCover

            reSortData()
            for (sizeCell: String in data.size) {
                mSizeDatas.add(sizeCell)
            }
            for(colorCell:String in data.color){
                mColorDatas.add(colorCell)
            }
            if (DataUtil.idNotNull(mSizeDatas)) {            //生成尺寸的viewgroup
                gv_sizes.addItemViews(mSizeDatas, com.project.app.ui.widget.GoodsViewGroup.TEV_MODE)
            }
            if (DataUtil.idNotNull(mColorDatas)) {            //生成颜色的viewGroup
                gv_color.addItemViews(mColorDatas, com.project.app.ui.widget.GoodsViewGroup.TEV_MODE)
            }

            displayChoiceGoods(mSkus.skus[0])             //显示最便宜的一个商品
            selectDefaultIndex(mSkus.skus[0])

            gv_sizes.setGroupClickListener {
                mChoiceSizeId = it
                getTargetGoodsSuku()
            }
            gv_color.setGroupClickListener {
                mChoiceColorId = it
                getTargetGoodsSuku()
            }
        }
    }

    fun selectDefaultIndex(skus: SkusItem){
        val defaultC:String = skus.color
        val defaultS:String = skus.size

        for(index in mColorDatas.indices){
            if(mColorDatas[index] == defaultC){
                mChoiceColorId = index
            }
        }
        for(index in mSizeDatas.indices){
            if(mSizeDatas[index] == defaultS){
                mChoiceSizeId = index
            }
        }
    }
    /**
     * 降序排列
     */
    private fun reSortData() {
        if(DataUtil.idNotNull(mSkus.skus)){
            Collections.sort(mSkus.skus, com.project.app.bean.SoreSkus())
            for(item: SkusItem in mSkus.skus){
                mSoliderMap.put(item.color+item.size,item)
            }
        }
    }

    private fun displayChoiceGoods(skus: SkusItem){
        if(skus.priceOrigin == skus.priceRetail){
            tv_gdOrignPrice.visibility = View.GONE
        }
        tv_gdOrignPrice.text = MathUtil.formatPrice(skus.priceOrigin * mBuyCount)
        tv_gdRetailprice.text = MathUtil.formatPrice(skus.priceRetail * mBuyCount)
        tv_cGdColor.text = skus.color + " " + skus.size

        if(skus.photos.isNotEmpty()){
            ImageLoader.getInstance().displayImage(iv_goodThumb,skus.photos[0] + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods)
        }else{
            ImageLoader.getInstance().displayImage(iv_goodThumb,mCoverPhoto + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods)
        }
        limitOtherSelect(skus)
    }

    private fun limitOtherSelect(skus: SkusItem) {
        val color = skus.color
        val size:String    = skus.size

        gv_sizes.renderGroup(obtainValidColor(color),size)
        gv_color.renderGroup(obtainValidSize(size),color)
    }

    private fun obtainValidColor(matchColor:String):ArrayList<String>{
        val validSizes = arrayListOf<String>()
        for(item in mSkus.skus){
            if(matchColor == item.color){
                validSizes.add(item.size)
            }
        }
        return validSizes
    }

    private fun obtainValidSize(matchSize:String):ArrayList<String>{
        val validColors = arrayListOf<String>()
        for(item in mSkus.skus){
            if(matchSize == item.size){
                validColors.add(item.color)
            }
        }
        return validColors
    }

    fun show(){
        if(mDialog == null){
            return;
        }
        mDialog.show()
    }

    fun dismiss(){
        mDialog.dismiss()
    }

    fun clear() {
        mDialog.dismiss()
    }

}

