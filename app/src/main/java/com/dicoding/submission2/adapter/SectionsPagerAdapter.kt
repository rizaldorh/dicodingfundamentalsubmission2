package com.dicoding.submission2.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.submission2.fragment.FollowersFragment
import com.dicoding.submission2.fragment.FollowingFragment
import com.dicoding.submission2.R

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun getItem(position: Int): Fragment {
        var fragments: Fragment? = null
        when (position) {
            0 -> fragments = FollowersFragment()
            1 -> fragments = FollowingFragment()
        }
        return fragments as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

}