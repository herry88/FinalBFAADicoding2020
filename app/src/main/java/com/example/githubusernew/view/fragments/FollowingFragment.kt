package com.example.githubusernew.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.R
import com.example.githubusernew.adapter.FollowingAdapter
import com.example.githubusernew.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment(){
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowerViewModel

    private var receiveDataUsername: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_following, container, false)
        receiveDataUsername = arguments?.getString("passData")
        setupViewModel(receiveDataUsername)
        return rootView
    }

    private fun setupViewModel(username: String?) {
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showRecyclerListViewFollowing()
    }

    private fun showRecyclerListViewFollowing() {
        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        rvFollowing.layoutManager = LinearLayoutManager(activity)
        rvFollowing.adapter = adapter
    }
}