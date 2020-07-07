package com.example.githubusernew.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubusernew.R
import com.example.githubusernew.view.fragments.FollowerFragment
import com.example.githubusernew.view.fragments.FollowingFragment
import com.loopj.android.http.AsyncHttpClient.log

class SectionPageAdapter(private val context: Context, passDataFromDetailActivity: String?, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)  {
    @StringRes
    private val tabTitles = intArrayOf(R.string.tab_follower, R.string.tab_following)

    private val passDataUsername = passDataFromDetailActivity

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> {
                fragment = FollowerFragment()
                val bundle1 = Bundle()
                bundle1.putString("passData", passDataUsername)
                fragment.arguments = bundle1
                log.d("PassFollower", fragment.arguments.toString())
            }
            1 -> {
                fragment = FollowingFragment()
                val bundle2 = Bundle()
                bundle2.putString("passData", passDataUsername)
                fragment.arguments = bundle2
                log.d("PassFollowing", fragment.arguments.toString())
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }
}