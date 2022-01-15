package com.example.androidcourse.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androidcourse.R
import com.example.androidcourse.auth.Profile
import com.example.androidcourse.databinding.FragmentProfileBinding
import com.example.androidcourse.infra.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    @InternalCoroutinesApi
    private val viewModel: ProfileViewModel by viewModels()

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToProfile()
        setClickListeners()
    }

    private fun setClickListeners() {
        viewBinding.feedbackButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://yandex.ru/dev/feedback/?from=main")
                )
            )
        }
        viewBinding.aboutButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
        }
    }

    @InternalCoroutinesApi
    private fun subscribeToProfile() {
        requireActivity().lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileFlow().collect {
                    it?.let { adjust(it) }
                }
            }
        }
        viewBinding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun adjust(profile: Profile) {
        Glide.with(viewBinding.profileImageView)
            .load(profile.imageUrl)
            .circleCrop()
            .into(viewBinding.profileImageView)
        viewBinding.userNameTextView.text = profile.name
        viewBinding.userLikesTextView.text = String.format(
            requireContext().getString(R.string.profile_likes_prefix),
            profile.likeCount
        )
        viewBinding.userPostsTextView.text = String.format(
            requireContext().getString(R.string.profile_posts_prefix),
            profile.postsCount
        )
    }
}