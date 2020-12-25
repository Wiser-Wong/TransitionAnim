package com.wiser.transitionanim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ActivityOptions
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.util.Pair
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {

    private var targetView: TextView? = null

    private var tvCircularReveal: TextView? = null

    private var isRunningAnim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main)

        tvCircularReveal = findViewById(R.id.tv_circular_reveal)
        targetView = findViewById(R.id.tv_circular_reveal_content)
    }

    /**
     * Transition Activity 方式转场
     * fade explode slide三种动画
     */
    fun transitionActivityClick(view: View) {
        val slide = Slide()
        slide.duration = 300
        //设置从左边退出
        slide.slideEdge = android.view.Gravity.START
//        //设置为退出
//        slide.mode = Visibility.MODE_OUT
        window.exitTransition = slide

//        val explode: Explode? =
//                TransitionInflater.from(this).inflateTransition(R.transition.activity_explode) as? Explode
//        window.exitTransition = explode
        startActivity(
            Intent(this, TransitionActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(
                this
            ).toBundle()
        )
    }

    /**
     * Transition Window 方式转场
     * fade explode slide三种动画
     */
    fun transitionWindowClick(view: View) {
        window.enterTransition = Explode()
        window.exitTransition = Explode()
        val transition1: Transition =
            TransitionInflater.from(this).inflateTransition(R.transition.activity_explode)
        window.reenterTransition = transition1
        val transition2: Transition =
            TransitionInflater.from(this).inflateTransition(R.transition.activity_explode)
        window.returnTransition = transition2
        startActivity(
            Intent(this, TransitionActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(
                this
            ).toBundle()
        )
    }

    /**
     * 共享元素转场
     */
    fun shareElements(view: View) {
        startActivity(
            Intent(this, ShareElementsActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair(findViewById<TextView>(R.id.tv_share_elements1), "shareElements1"), Pair(
                    findViewById<TextView>(
                        R.id.tv_share_elements2
                    ), "shareElements2"
                )
            ).toBundle()
        )
    }

    /**
     * overridePendingTransition 转场动画
     */
    fun overridePendingTransitionClick(view: View) {
        startActivity(Intent(this, OverridePendingTransitionActivity::class.java))
        overridePendingTransition(R.anim.translate_left_in, R.anim.translate_out)
    }

    /**
     * 圆形揭露动画1
     */
    fun circularRevealClick1(view: View) {
        if (isRunningAnim) return
        isRunningAnim = true
        val fade = Fade()
        window.enterTransition = fade
        window.exitTransition = fade
        window.reenterTransition = fade
        window.returnTransition = fade

        val changeBounds = ChangeBounds()
        changeBounds.duration = 1000
        val changeClipBounds = ChangeClipBounds()
        changeClipBounds.duration = 1000
        val changeImageTransform = ChangeImageTransform()
        changeImageTransform.duration = 1000
        val changeTransform = ChangeTransform()
        changeTransform.duration = 1000
        window.sharedElementEnterTransition = changeBounds
        window.sharedElementExitTransition = changeClipBounds
        window.sharedElementReenterTransition = changeImageTransform
        window.sharedElementReturnTransition = changeTransform

        window.sharedElementsUseOverlay = true

        val width: Int = targetView?.measuredWidth ?: 0
        val height: Int = targetView?.measuredHeight ?: 0
        val radius = Math.sqrt((width * width + height * height).toDouble())
            .toFloat() / 2 //半径

        val animator: Animator
        if (targetView?.visibility == View.VISIBLE) {
            animator = ViewAnimationUtils.createCircularReveal(
                targetView,
                width / 2,
                height / 2,
                radius,
                0f
            )
            animator.duration = 1000
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.start()
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    targetView?.visibility = View.GONE
                    isRunningAnim = false
                }
            })
        } else {
            animator = ViewAnimationUtils.createCircularReveal(
                targetView,
                width / 2,
                height / 2,
                0f,
                radius
            )
            animator.duration = 1000
            animator.interpolator = AccelerateDecelerateInterpolator()
            targetView?.visibility = View.VISIBLE
            animator.start()
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    targetView?.visibility = View.VISIBLE
                    isRunningAnim = false
                }
            })
        }
    }

    /**
     * 圆形揭露动画
     */
    fun circularRevealClick2(view: View) {
        if (isRunningAnim) return
        isRunningAnim = true
        //计算动画圆形的X坐标，View的宽度 - 按钮宽度的一半 - 按钮右边距
        val centerX: Int =
            (targetView?.measuredWidth?.minus(
                (tvCircularReveal?.measuredWidth?.div(2)
                    ?: 0)
            ) ?: 0) - (targetView?.measuredWidth?.minus(tvCircularReveal?.right!!)?:0)
        val centerY: Int =
            targetView?.measuredHeight?.minus((tvCircularReveal?.measuredHeight?.div(2) ?: 0)) ?: 0

        //半径计算不精确，但是无伤大雅
        val width: Int = targetView?.width?.minus((tvCircularReveal?.measuredWidth?.div(2) ?: 0)) ?: 0
        val height: Int =
            targetView?.height?.minus((tvCircularReveal?.measuredHeight?.div(2) ?: 0)) ?: 0
        val radius =
            hypot(width.toDouble(), height.toDouble()).toInt() //斜边，半径

        if (targetView?.visibility == View.VISIBLE) {
            val animator = ViewAnimationUtils.createCircularReveal(
                targetView,
                centerX,
                centerY,
                radius.toFloat(),
                0f
            )
            animator.duration = 1000
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    targetView?.visibility = View.GONE
                    isRunningAnim = false
                }
            })
            animator.start()
        } else {
            //动画前需要把View的可见性设置为Visible
            targetView?.visibility = View.VISIBLE
            val animator = ViewAnimationUtils.createCircularReveal(
                targetView,
                centerX,
                centerY,
                0f,
                radius.toFloat()
            )
            animator.duration = 1000
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isRunningAnim = false
                }
            })
            animator.start()
        }
    }

    /**
     * 图片预览
     */
    fun previewPhotoClick(view: View) {
        startActivity(Intent(this, PreviewPhotoListActivity::class.java))
    }

    /**
     * 选择规格弹窗
     */
    fun selectSpecificationDialogClick(view: View) {
        showDialog()
        selectSfLayout?.moveView()
    }

    private fun showDialog() {
        val mDialog = Dialog(this, R.style.dialog)
        mDialog.setContentView(View.inflate(this, R.layout.dialog_view, null))
        mDialog.setCanceledOnTouchOutside(true)
        mDialog.setCancelable(true)
        val localWindow: Window? = mDialog.window
        localWindow?.setWindowAnimations(R.style.dialog_animation)
        localWindow?.setGravity(Gravity.BOTTOM)
        localWindow?.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = localWindow?.attributes
        val wh =
            intArrayOf(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp?.width = wh[0]
        lp?.height = wh[1]
        localWindow?.attributes = lp
        if (!mDialog.isShowing) mDialog.show()
        mDialog.setOnDismissListener(DialogInterface.OnDismissListener { selectSfLayout.resetView() })
    }
}