package com.dicoding.githubuser.ui.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.ui.fragments.FollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var username: String = ""

    fun setUsername(name: String) {
        username = name
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position + 1)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount() = 2
}