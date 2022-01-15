package com.example.androidcourse.ui.about

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidcourse.BuildConfig
import com.example.androidcourse.R
import com.example.androidcourse.databinding.FragmentAboutBinding
import com.example.androidcourse.infra.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseFragment(R.layout.fragment_about) {
    private val viewBinding by viewBinding(FragmentAboutBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackButtonPressed()
                }
            }
        )
    }

    private fun onBackButtonPressed() {
        findNavController().popBackStack()
        return
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.setOnClickListener {
            onBackButtonPressed()
        }
        viewBinding.buildInfo.text = String.format(
            requireContext().getString(R.string.about_build_number),
            BuildConfig.VERSION_CODE
        )
    }
}