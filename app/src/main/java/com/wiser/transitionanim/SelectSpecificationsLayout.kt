package com.wiser.transitionanim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class SelectSpecificationsLayout : LinearLayout {
    private var mTopMatrix: Matrix? = null
    private val offset = 40

    //动画时长
    private val duration = 300
    var mTopView: View? = null
    var mBottomView: View? = null
    private val src = FloatArray(8)
    private val dst = FloatArray(8)
    private var mTopWidth = 0
    private var mTopHeight = 0
    private var init = true

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mTopMatrix = Matrix()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopView = getChildAt(0)
        mBottomView = getChildAt(1)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mTopWidth = mTopView!!.width
        mTopHeight = mTopView!!.height
    }

    fun moveView() {
        init = false
        initTopViewLocation()
        startAnimation(STATUS_ONE)
    }

    fun resetView() {
        startAnimation(STATUS_THREE)
    }

    private fun initTopViewLocation() {
        setTopViewSrcLocation(
            0f,
            0f,
            mTopWidth.toFloat(),
            0f,
            mTopWidth.toFloat(),
            mTopHeight.toFloat(),
            0f,
            mTopHeight.toFloat()
        )
    }

    private fun setTopViewSrcLocation(
        topLeftX: Float, topLeftY: Float, topRightX: Float, topRightY: Float, bottomRightX: Float,
        bottomRightY: Float, bottomLeftX: Float, bottomLeftY: Float
    ) {
        src[0] = topLeftX
        src[1] = topLeftY
        src[2] = topRightX
        src[3] = topRightY
        src[4] = bottomRightX
        src[5] = bottomRightY
        src[6] = bottomLeftX
        src[7] = bottomLeftY
    }

    private fun setTopViewDstLocation(
        topLeftX: Float, topLeftY: Float, topRightX: Float, topRightY: Float, bottomRightX: Float,
        bottomRightY: Float, bottomLeftX: Float, bottomLeftY: Float
    ) {
        dst[0] = topLeftX
        dst[1] = topLeftY
        dst[2] = topRightX
        dst[3] = topRightY
        dst[4] = bottomRightX
        dst[5] = bottomRightY
        dst[6] = bottomLeftX
        dst[7] = bottomLeftY
    }

    private fun setViewLocation(status: Int, moveOffset: Float) {
        var leftTopX = 0f
        var leftTopY = 0f
        var rightTopX = 0f
        var rightTopY = 0f
        var rightBottomX = 0f
        var rightBottomY = 0f
        var leftBottomX = 0f
        var leftBottomY = 0f
        when (status) {
            STATUS_ONE -> {
                run {
                    leftTopY = moveOffset
                    leftTopX = leftTopY
                }
                rightTopX = mTopWidth - moveOffset
                rightTopY = moveOffset
                rightBottomX = mTopWidth.toFloat()
                rightBottomY = mTopHeight.toFloat()
                leftBottomX = 0f
                leftBottomY = rightBottomY
            }
            STATUS_TWO -> {
                run {
                    leftTopY = offset.toFloat()
                    leftTopX = leftTopY
                }
                rightTopX = (mTopWidth - offset).toFloat()
                rightTopY = offset.toFloat()
                rightBottomX = mTopWidth - moveOffset
                rightBottomY = mTopHeight - moveOffset
                leftBottomX = moveOffset
                leftBottomY = rightBottomY
            }
            STATUS_THREE -> {
                run {
                    leftTopY = offset.toFloat()
                    leftTopX = leftTopY
                }
                rightTopX = (mTopWidth - offset).toFloat()
                rightTopY = offset.toFloat()
                rightBottomX = mTopWidth - offset + moveOffset
                rightBottomY = mTopHeight - offset + moveOffset
                leftBottomX = offset - moveOffset
                leftBottomY = rightBottomY
            }
            STATUS_FOUR -> {
                run {
                    leftTopY = offset - moveOffset
                    leftTopX = leftTopY
                }
                rightTopX = mTopWidth - offset + moveOffset
                rightTopY = offset - moveOffset
                rightBottomX = mTopWidth.toFloat()
                rightBottomY = mTopHeight.toFloat()
                leftBottomX = 0f
                leftBottomY = rightBottomY
            }
        }
        setTopViewDstLocation(
            leftTopX,
            leftTopY,
            rightTopX,
            rightTopY,
            rightBottomX,
            rightBottomY,
            leftBottomX,
            leftBottomY
        )
    }

    private fun startAnimation(status: Int) {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener { animation ->
            val moveOffset = animation.animatedValue as Float * offset
            setViewLocation(status, moveOffset)
            postInvalidate()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (status == STATUS_ONE || status == STATUS_THREE) {
                    startAnimation(if (status == STATUS_ONE) STATUS_TWO else STATUS_FOUR)
                }
            }
        })
        valueAnimator.duration = duration.toLong()
        valueAnimator.start()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (!init) {
            canvas.save()
            mTopMatrix!!.reset()
            mTopMatrix!!.setPolyToPoly(src, 0, dst, 0, src.size shr 1)
            canvas.concat(mTopMatrix)
            drawChild(canvas, mTopView, drawingTime)
            canvas.restore()
            if (mBottomView != null)
                drawChild(canvas, mBottomView, drawingTime)
            return
        }
        super.dispatchDraw(canvas)
    }

    companion object {
        /**
         * TopView的不同状态
         */
        private const val STATUS_ONE = 1
        private const val STATUS_TWO = 2
        private const val STATUS_THREE = 3
        private const val STATUS_FOUR = 4
    }
}