package com.wiser.transitionanim

import android.os.Bundle
import android.transition.*
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class TransitionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_transition)

        // 进入
        val slide = Slide()
        slide.duration = 300
//        //设置为进入
//        slide.mode = Visibility.MODE_IN
        //设置从右边进入
        slide.slideEdge = Gravity.END
        window.enterTransition = slide

//        val fade = Fade()
//        fade.duration = 1000
//        window.enterTransition = fade

//        val explode: Explode? =
//            TransitionInflater.from(this).inflateTransition(R.transition.activity_explode) as? Explode
//        window.enterTransition = explode
    }

}