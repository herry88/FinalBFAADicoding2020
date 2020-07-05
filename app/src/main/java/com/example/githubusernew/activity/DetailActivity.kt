package com.example.githubusernew.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusernew.R
import com.example.githubusernew.model.Detail
import com.example.githubusernew.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

/*
    * Author : herry prasetyo
    * years : 2020
 */
class DetailActivity: AppCompatActivity() {
    companion object{
        const val EXTRA_USERNAME = "extra_username"
    }

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.title = "User Detail"
        supportActionBar?.elevation = 0f


        val usernameFromMainActivity = intent.getStringExtra(EXTRA_USERNAME)

        showLoading(true)

        setupTabs(usernameFromMainActivity)
        setupViewModel(usernameFromMainActivity)
    }

    private fun setupTabs(username: String?){
        val sectionPagerAdapter = SectionPagerAdapter(this, username, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs_layout.setupWithViewPager(view_pager)
    }

    private fun setupViewModel(username: String?){
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        detailViewModel.setDetail(username)

        detailViewModel.getDetail().observe(this, Observer { dataDetail ->
            if (dataDetail != null){
                bind(dataDetail)
                showLoading(false) //loading finish
            }
        })
    }

    private fun bind(detail: Detail){
        Glide.with(this@DetailActivity).load(detail.avatarUrl).apply(RequestOptions().override(110,110)).into(img_avatar_user)
        tv_username.text = detail.login
        tv_name.text = detail.name
        tv_three_info.text = getString(R.string.otherinfo, detail.company, detail.location, detail.repository.toString())
    }

    private fun showLoading(state: Boolean){
        if (state){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }
}