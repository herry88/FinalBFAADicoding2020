package com.example.githubusernew.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusernew.R
import com.example.githubusernew.model.Following
import kotlinx.android.synthetic.main.following_items.view.*

/*
    author : Herry Prasetyo
    reference : Dicoding
 */

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    private val listFollowing = ArrayList<Following>()

    fun setData(items: ArrayList<Following>){
        listFollowing.clear()
        listFollowing.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.following_items, viewGroup, false)
        return FollowingViewHolder(view)
    }

    override fun getItemCount(): Int = listFollowing.size

    override fun onBindViewHolder(followingViewHolder: FollowingViewHolder, position: Int) {
        followingViewHolder.bind(listFollowing[position])
    }

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(following: Following){
            with(itemView){
                Glide.with(itemView.context).load(following.avatarUrl).apply(RequestOptions().override(55,55)).into(imgAvatarFollowing)
                tv_username.text = following.login
                tvType.text = following.type
            }
        }
    }
}