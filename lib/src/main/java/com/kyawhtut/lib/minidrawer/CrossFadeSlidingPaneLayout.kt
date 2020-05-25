package com.kyawhtut.lib.minidrawer

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.slidingpanelayout.widget.SlidingPaneLayout

class CrossFadeSlidingPaneLayout : SlidingPaneLayout,
    ICrossFadeSlidingPaneLayout {
    private var partialView: View? = null
    private var fullView: View? = null
    private var contentView: View? = null

    private var wasOpened = false
    private var mCanSlide = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount < 1) {
            return
        }
        val panel = getChildAt(0) as? ViewGroup ?: return
        contentView = getChildAt(1) as? ViewGroup ?: return
        if (panel.childCount != 2) {
            return
        }
        fullView = panel.getChildAt(0)
        partialView = panel.getChildAt(1)
        super.setPanelSlideListener(crossFadeListener)
    }

    override fun setPanelSlideListener(listener: PanelSlideListener?) {
        if (listener == null) {
            super.setPanelSlideListener(crossFadeListener)
            return
        }
        super.setPanelSlideListener(object :
            PanelSlideListener {
            override fun onPanelSlide(
                panel: View,
                slideOffset: Float
            ) {
                crossFadeListener.onPanelSlide(panel, slideOffset)
                listener.onPanelSlide(panel, slideOffset)
            }

            override fun onPanelOpened(panel: View) {
                listener.onPanelOpened(panel)
            }

            override fun onPanelClosed(panel: View) {
                listener.onPanelClosed(panel)
            }
        })
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        if (partialView != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                updatePartialViewVisibilityPreHoneycomb(isOpen)
            }
            partialView!!.visibility = if (isOpen) View.GONE else View.VISIBLE
        }
    }

    private val crossFadeListener: SimplePanelSlideListener =
        object : SimplePanelSlideListener() {
            override fun onPanelSlide(
                panel: View,
                slideOffset: Float
            ) {
                super.onPanelSlide(panel, slideOffset)
                if (partialView == null || fullView == null) {
                    return
                }
                setOffset(slideOffset)
            }
        }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mCanSlide && super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return mCanSlide && super.onTouchEvent(ev)
    }

    override fun setCanSlide(canSlide: Boolean) {
        this.mCanSlide = canSlide
    }

    override fun setOffset(slideOffset: Float) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            if (slideOffset == 1f && !wasOpened) {
                updatePartialViewVisibilityPreHoneycomb(true)
                wasOpened = true
            } else if (slideOffset < 1 && wasOpened) {
                updatePartialViewVisibilityPreHoneycomb(false)
                wasOpened = false
            }
            updateAlphaApi10(partialView, 1 - slideOffset)
            updateAlphaApi10(fullView, slideOffset)
        } else {
            partialView!!.alpha = 1 - slideOffset
            fullView!!.alpha = slideOffset
        }
        partialView!!.visibility = if (isOpen) View.GONE else View.VISIBLE
    }

    private fun updateAlphaApi10(v: View?, value: Float) {
        val alpha = AlphaAnimation(value, value)
        alpha.duration = 0
        alpha.fillAfter = true
        v!!.startAnimation(alpha)
    }

    private fun updatePartialViewVisibilityPreHoneycomb(slidingPaneOpened: Boolean) {
        if (slidingPaneOpened) {
            partialView!!.layout(-partialView!!.width, 0, 0, partialView!!.height)
        } else {
            partialView!!.layout(0, 0, partialView!!.width, partialView!!.height)
        }
    }
}