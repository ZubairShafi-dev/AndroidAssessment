package com.example.androidassessment.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidassessment.R
import com.example.androidassessment.databinding.FragmentUsersBinding
import com.example.androidassessment.domain.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsersViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        setupErrorRetry()
        observeUiState()
        loadInitialData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter { user -> navigateToUserDetail(user) }
        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupErrorRetry() {
        binding.errorLayout.buttonRetry.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun navigateToUserDetail(user: User) {
        findNavController().navigate(
            UsersFragmentDirections.actionUsersFragmentToUserDetailFragment(userId = user.id)
        )
    }

    private fun loadInitialData() {
        viewModel.refresh()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun renderState(state: UsersUiState) {
        if (state.users.isNotEmpty()) {
            userAdapter.submitList(state.users)
        }

        val showShimmer = state.isLoading && state.users.isEmpty()
        binding.shimmerLayout.isVisible = showShimmer
        binding.recyclerViewUsers.isVisible = !showShimmer && state.error == null
        if (showShimmer) binding.shimmerLayout.startShimmer()
        else binding.shimmerLayout.stopShimmer()

        binding.swipeRefreshLayout.isRefreshing = state.isLoading && state.users.isNotEmpty()

        val showError = state.error != null && state.users.isEmpty()
        binding.errorLayout.root.isVisible = showError
        if (showError) {
            binding.errorLayout.textViewErrorMessage.text = state.error
        }
    }
}
