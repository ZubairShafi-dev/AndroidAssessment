package com.example.androidassessment.presentation.randomusers

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidassessment.databinding.FragmentRandomUsersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomUsersFragment : Fragment() {

    private var _binding: FragmentRandomUsersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RandomUsersViewModel by viewModels()
    private lateinit var adapter: RandomUserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRandomUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RandomUserAdapter()
        binding.recyclerViewRandomUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@RandomUsersFragment.adapter
        }
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        binding.errorLayout.buttonRetry.setOnClickListener { viewModel.refresh() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.users.isNotEmpty()) adapter.submitList(state.users)
                    val showShimmer = state.isLoading && state.users.isEmpty()
                    binding.shimmerLayout.isVisible = showShimmer
                    binding.recyclerViewRandomUsers.isVisible = !showShimmer && state.error == null
                    if (showShimmer) binding.shimmerLayout.startShimmer() else binding.shimmerLayout.stopShimmer()
                    binding.swipeRefreshLayout.isRefreshing = state.isLoading && state.users.isNotEmpty()
                    val showError = state.error != null && state.users.isEmpty()
                    binding.errorLayout.root.isVisible = showError
                    if (showError) binding.errorLayout.textViewErrorMessage.text = state.error
                }
            }
        }
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
