package com.wiser.transitionanim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class OverridePendingTransitionActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_override_pending_transition)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.translate_out,R.anim.translate_right_out)
    }

}