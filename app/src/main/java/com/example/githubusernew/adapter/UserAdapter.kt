@file:Suppress("NAME_SHADOWING")

package com.example.githubusernew.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusernew.R
import com.example.githubusernew.model.Favorite
import com.example.githubusernew.model.User
import kotlinx.android.synthetic.main.useritems.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val data = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    fun setData(items: ArrayList<User>, itemsFavorite: ArrayList<Favorite>){
        data.clear()

        for (i in 0 until items.size){
            val j = 0
            if(j >= itemsFavorite.size)break
            for (j in 0 until itemsFavorite.size){
                if (itemsFavorite[j].login.toString() == items[i].login.toString()){
                    items[i].favorite = 1
                }
            }
        }

        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.useritems, viewGroup, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(data[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(users: User){
            with(itemView){
                Glide.with(itemView.context).load(users.avatarUrl).apply(RequestOptions().override(55,55)).into(imguser)
                tv_username.text = users.login
                tvType.text = users.type
                if(users.favorite == 1) {
                    val iconFavorite = R.drawable.ic_favorite
                    btnFavorite.setImageResource(iconFavorite)
                }else{
                    val iconFavorite = R.drawable.ic_favorite_border
                    btnFavorite.setImageResource(iconFavorite)
                }
                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(users)}

                btnFavorite.setOnClickListener { onItemClickCallback?.onBtnFavoriteClicked(itemView, users) }
            }
        }
    }

    //interface untuk on item click callback
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
        fun onBtnFavoriteClicked(view: View, data: User)
    }
}
