package com.project.app.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.qmuiteam.qmui.widget.QMUIViewPager

class NoScrollViewPager(context: Context?, attrs: AttributeSet?) : QMUIViewPager(context, attrs) {
    var noScroll = true
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (noScroll) {
            false
        } else {
            super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (noScroll) {
            false
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }
}