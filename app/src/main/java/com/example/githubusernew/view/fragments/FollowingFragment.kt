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
import com.example.githubusernew.adapter.FollowingAdapter
import com.example.githubusernew.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment(){
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    private var receiveDataUsername: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_following, container, false)

        receiveDataUsername = arguments?.getString("passData")
        setupViewModel(receiveDataUsername)

        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showRecyclerListViewFollowing()
    }

    private fun showRecyclerListViewFollowing(){
        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        rvFollowing.layoutManager = LinearLayoutManager(activity)
        rvFollowing.adapter = adapter
    }

    private fun setupViewModel(username: String?){
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        followingViewModel.setFollowing(username)

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingItems ->
            if (followingItems != null){
                adapter.setData(followingItems)
            }
        })
    }
}