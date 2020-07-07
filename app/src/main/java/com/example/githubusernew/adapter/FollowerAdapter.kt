package com.example.githubusernew.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusernew.R
import com.example.githubusernew.model.Follower
import kotlinx.android.synthetic.main.follower_items.view.*

class FollowerAdapter: RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {
    private val data = ArrayList<Follower>()

    fun setData(items: ArrayList<Follower>){
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.follower_items, viewGroup, false)
        return FollowerViewHolder(view)
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(followerViewHolder: FollowerViewHolder, position: Int) {
        followerViewHolder.bind(data[position])
    }

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(follower: Follower){
            with(itemView){
                Glide.with(itemView.context).load(follower.avatarUrl).apply(RequestOptions().override(55,55)).into(imgAvatarFollower)
                tv_username_follower.text = follower.login
                tvTypeFollower.text = follower.type

            }

        }
    }
}