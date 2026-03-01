package com.example.androidassessment.presentation.users

import com.example.androidassessment.domain.model.User
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.androidassessment.databinding.FragmentUserDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    user?.let { bindUser(it) }
                }
            }
        }
    }

    private fun bindUser(user: User) {
        binding.textViewName.text = user.name
        binding.textViewUsername.text = user.username
        binding.textViewEmail.text = user.email
        binding.textViewPhone.text = user.phone
        binding.textViewWebsite.text = user.website
        val address = listOfNotNull(
            user.addressStreet.takeIf { it.isNotEmpty() },
            user.addressCity.takeIf { it.isNotEmpty() },
            user.addressZipcode.takeIf { it.isNotEmpty() }
        ).joinToString(", ")
        binding.textViewAddress.text = address.ifEmpty { "—" }
        binding.textViewCompany.text = user.companyName.ifEmpty { "—" }
    }
}
