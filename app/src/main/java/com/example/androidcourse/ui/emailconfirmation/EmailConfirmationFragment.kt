package com.example.androidcourse.ui.emailconfirmation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidcourse.R
import com.example.androidcourse.databinding.FragmentEmailConfirmationBinding
import com.example.androidcourse.infra.BaseFragment
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {
    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationFragmentViewModel by viewModels()

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToFormFields()
    }

    private fun subscribeToFormFields() {
        viewBinding.smsCodeView.onChangeListener =
            SmsConfirmationView.OnChangeListener { code, isComplete ->
                viewModel.updateCode(code)
                if (isComplete) {
                    if (!viewModel.tryToApprove()) {
                        viewBinding.smsCodeView.enteredCode = ""
                    }
                }
            }
    }
}