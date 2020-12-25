package com.wiser.transitionanim

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo_list.*

class PreviewPhotoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_list)

        rlv_photo?.layoutManager = GridLayoutManager(this,2)
        rlv_photo?.adapter = PreviewPhotoListAdapter(this,getPhotoData())
    }

    private fun getPhotoData(): MutableList<String> {
        val list = mutableListOf<String>()
        list.add("https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1089874897,1268118658&fm=26&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1963661110,1685480926&fm=26&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2098771985,3287548652&fm=26&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1299184499,3858873216&fm=26&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2099423025,3974043555&fm=26&gp=0.jpg")
        return list
    }

}

class PreviewPhotoListAdapter(private val context:Context, var list: MutableList<String>?) :
    RecyclerView.Adapter<PreviewPhotoListAdapter.PreviewPhotoListHolder>() {

    class PreviewPhotoListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewPhotoListHolder =
        PreviewPhotoListHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_preview_photo, parent, false)
        )

    override fun onBindViewHolder(holder: PreviewPhotoListHolder, position: Int) {
        Glide.with(context).load(list?.get(position)).into((holder.itemView as AppCompatImageView))

        holder.itemView.setOnClickListener{
            PreviewPhotoActivity.intent(context,list?.get(position)?:"",holder.itemView)
        }
    }

    override fun getItemCount(): Int = list?.size ?: 0
}