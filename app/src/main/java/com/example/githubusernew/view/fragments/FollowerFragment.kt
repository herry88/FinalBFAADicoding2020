package com.example.githubusernew.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.R
import com.example.githubusernew.adapter.FollowerAdapter
import com.example.githubusernew.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment: Fragment() {
    private lateinit var adapter: FollowerAdapter
    private lateinit var followerViewModel: FollowerViewModel

    private var receiveDataUsername: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_follower, container, false)

        receiveDataUsername = arguments?.getString("passData")
        setupViewModel(receiveDataUsername)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showRecyclerListViewFollower()
    }

    private fun showRecyclerListViewFollower(){
        adapter = FollowerAdapter()
        adapter.notifyDataSetChanged()

        rv_follower.layoutManager = LinearLayoutManager(activity)
        rv_follower.adapter = adapter
    }

    private fun setupViewModel(username: String?){
        //menghubungkan viewModelFollower to this
        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)

        followerViewModel.setFollower(username)

        followerViewModel.getFollower().observe(viewLifecycleOwner, Observer { followerItems ->
            if (followerItems != null){
                adapter.setData(followerItems)
            }
        })
    }
}