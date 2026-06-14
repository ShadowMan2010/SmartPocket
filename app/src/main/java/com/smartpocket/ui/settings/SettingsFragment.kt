package com.smartpocket.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.smartpocket.databinding.FragmentSettingsBinding
import com.smartpocket.ui.home.SecurityViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SecurityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.switchAutoArm.setOnCheckedChangeListener { _, checked ->
            // Persist via DataStore in production
        }

        binding.switchSilentAlarm.setOnCheckedChangeListener { _, checked ->
            // Toggle vibration-only mode
        }

        binding.sliderDelay.addOnChangeListener { _, value, _ ->
            val secs = value.toInt()
            binding.tvDelayValue.text = when (secs) {
                0 -> "Immediate"
                1 -> "1 second"
                else -> "$secs seconds"
            }
            viewModel.setAlarmDelay(secs)
        }

        binding.switchNotifBag.setOnCheckedChangeListener { _, _ -> }
        binding.switchNotifBt.setOnCheckedChangeListener { _, _ -> }
    }

    private fun observeViewModel() {
        viewModel.securityState.observe(viewLifecycleOwner) { state ->
            binding.switchAutoArm.isChecked = false // load from prefs
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
