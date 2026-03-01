package com.example.androidassessment.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.androidassessment.R
import com.example.androidassessment.databinding.FragmentSettingsBinding
import com.example.androidassessment.databinding.ItemSettingsRowBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private val timestampFormatter = DateTimeFormatter
        .ofPattern("dd MMM yyyy  HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStaticFields()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupStaticFields() {
        row(binding.rowFlavor,        R.string.label_flavor,             viewModel.flavor)
        row(binding.rowDogApi,        R.string.label_dog_api_url,        SettingsViewModel.DOG_API_BASE_URL)
        row(binding.rowUsersApi,      R.string.label_users_api_url,      SettingsViewModel.USERS_API_BASE_URL)
        row(binding.rowPostsApi,      R.string.label_posts_api_url,      SettingsViewModel.POSTS_API_BASE_URL)
        row(binding.rowRandomUserApi, R.string.label_random_user_api_url, SettingsViewModel.RANDOM_USER_API_URL)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isPollingActive.collect { active ->
                        row(binding.rowPolling, R.string.label_polling_active, booleanString(active))
                    }
                }
                launch {
                    viewModel.lastFetchTime.collect { instant ->
                        val text = if (instant == null) getString(R.string.value_never)
                                   else timestampFormatter.format(instant)
                        row(binding.rowLastFetch, R.string.label_last_fetch, text)
                    }
                }
            }
        }
    }

    private fun row(binding: ItemSettingsRowBinding, labelRes: Int, value: String) {
        binding.textLabel.setText(labelRes)
        binding.textValue.text = value
    }

    private fun booleanString(value: Boolean): String =
        getString(if (value) R.string.value_yes else R.string.value_no)
}
