package com.wiser.transitionanim

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_preview_photo.*

class PreviewPhotoActivity : AppCompatActivity() {

    companion object {
        fun intent(context: Context, photoUrl: String, view: View) {
            val intent = Intent(context, PreviewPhotoActivity::class.java)
            intent.putExtra("photoUrl", photoUrl)
            context.startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity?, view, "previewPhotoTransitionName"
                ).toBundle()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_photo)

        Glide.with(this).load(intent?.getStringExtra("photoUrl")).into(iv_preview_photo)

        iv_preview_photo?.setOnClickListener{
            finishAfterTransition()
        }
    }

}