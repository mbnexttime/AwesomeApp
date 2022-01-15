package com.example.androidcourse.ui.userlist

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidcourse.R
import com.example.androidcourse.contacts.ContactsRecyclerViewAdapter
import com.example.androidcourse.contacts.User
import com.example.androidcourse.databinding.FragmentUserListBinding
import com.example.androidcourse.infra.BaseFragment
import com.google.android.material.internal.TextWatcherAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserListFragment @Inject constructor() : BaseFragment(R.layout.fragment_user_list) {
    private val viewBinding by viewBinding(FragmentUserListBinding::bind)

    private val viewModel: UserListViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.searchPeopleTextInputLayout.isVisible = false
        viewBinding.searchPersonFloatingActionButton.setOnClickListener {
            viewBinding.searchPeopleTextInputLayout.isVisible =
                viewBinding.searchPeopleTextInputLayout.isVisible xor true
        }
        setupRecycler()
        subscribeToData()
        subscribeToSearch()
    }

    @InternalCoroutinesApi
    private fun subscribeToSearch() {
        viewBinding.searchPersonEditText.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(editable: Editable) {
                viewModel.currentFilter = editable.toString()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.uiState.collect {
                        display(it)
                    }
                }
            }
        })
    }

    @InternalCoroutinesApi
    private fun subscribeToData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(
                    object : FlowCollector<UserListViewModel.ViewState> {
                        override suspend fun emit(value: UserListViewModel.ViewState) {
                            display(value)
                        }
                    }
                )
            }
        }
    }

    private fun setupRecycler() {
        val adapter = ContactsRecyclerViewAdapter()
        viewBinding.userListRecyclerView.adapter = adapter
    }

    private fun display(state: UserListViewModel.ViewState) {
        when (state) {
            is UserListViewModel.ViewState.Loading -> {
                viewBinding.userListRecyclerView.isVisible = false
                viewBinding.progressBar.isVisible = true
            }
            is UserListViewModel.ViewState.Data -> {
                viewBinding.userListRecyclerView.isVisible = true
                val adapter = viewBinding.userListRecyclerView.adapter as ContactsRecyclerViewAdapter
                adapter.userData = filter(state.userList)
                adapter.notifyDataSetChanged()
                viewBinding.progressBar.isVisible = false
            }
            is UserListViewModel.ViewState.Error -> {
                viewBinding.userListRecyclerView.isVisible = false
                viewBinding.progressBar.isVisible = false
            }
            is UserListViewModel.ViewState.Empty -> {
                viewBinding.progressBar.isVisible = false
                viewBinding.errorTextView.text = getString(R.string.empty_list_response)
            }
        }
    }

    private fun filter(userList: List<User>): List<User> {
        val key = viewModel.currentFilter ?: return userList
        return userList.filter { user ->
            listOf(user.userName, user.groupName).map {
                it.lowercase().contains(key)
            }.contains(true)
        }
    }
}