package com.example.androidassessment.presentation.posts

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
import com.example.androidassessment.databinding.FragmentPostsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostsViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        setupErrorRetry()
        observeUiState()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter { post ->
            findNavController().navigate(
                PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(postId = post.id)
            )
        }
        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
    }

    private fun setupErrorRetry() {
        binding.errorLayout.buttonRetry.setOnClickListener { viewModel.refresh() }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.posts.isNotEmpty()) postAdapter.submitList(state.posts)
                    val showShimmer = state.isLoading && state.posts.isEmpty()
                    binding.shimmerLayout.isVisible = showShimmer
                    binding.recyclerViewPosts.isVisible = !showShimmer && state.error == null
                    if (showShimmer) binding.shimmerLayout.startShimmer()
                    else binding.shimmerLayout.stopShimmer()
                    binding.swipeRefreshLayout.isRefreshing = state.isLoading && state.posts.isNotEmpty()
                    val showError = state.error != null && state.posts.isEmpty()
                    binding.errorLayout.root.isVisible = showError
                    if (showError) binding.errorLayout.textViewErrorMessage.text = state.error
                }
            }
        }
    }
}
