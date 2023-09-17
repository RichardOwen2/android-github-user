package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.response.UserList
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.google.android.material.snackbar.Snackbar

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()
    private var position: Int = 0
    private var username: String = ""

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowList.addItemDecoration(itemDecoration)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) as String
        }

        followViewModel.user.observe(viewLifecycleOwner) {
            setFollow(it)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    view,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        if (savedInstanceState == null) {
            if (position == 1) {
                followViewModel.getUserFollowers(username)
            } else {
                followViewModel.getUserFollowings(username)
            }
        }
    }

    private fun setFollow(user: List<UserList>) {
        val adapter = FollowAdapter().apply {
            submitList(user)
        }
        binding.rvFollowList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}