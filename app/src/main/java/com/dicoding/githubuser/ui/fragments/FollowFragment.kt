package com.dicoding.githubuser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.Result
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.ui.adapters.FollowAdapter
import com.dicoding.githubuser.ui.viewmodels.FollowViewModel
import com.dicoding.githubuser.ui.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val followViewModel: FollowViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var position: Int = 0
    private var username: String = ""

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

        if (savedInstanceState == null) {
            if (position == 1) {
                followViewModel.getUserFollowers(username).observe(viewLifecycleOwner) {
                    setFollow(it)
                }
            } else {
                followViewModel.getUserFollowings(username).observe(viewLifecycleOwner) {
                    setFollow(it)
                }

            }
        }
    }

    private fun setFollow(user: Result<List<User>?>) {
        when (user) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                showLoading(false)
                val adapter = FollowAdapter().apply {
                    submitList(user.data)
                }
                binding.rvFollowList.adapter = adapter
            }

            is Result.Error -> {
                showLoading(false)
                Snackbar.make(binding.root, user.error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = ""
    }
}