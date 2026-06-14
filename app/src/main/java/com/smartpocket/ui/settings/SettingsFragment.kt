package com.smartpocket.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.smartpocket.databinding.FragmentSettingsBinding
import com.smartpocket.ui.home.SecurityViewModel
import com.smartpocket.utils.UpdateManager
import kotlinx.coroutines.launch

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
        }

        binding.switchSilentAlarm.setOnCheckedChangeListener { _, checked ->
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

        binding.btnCheckUpdate.setOnClickListener {
            checkForUpdate()
        }
    }

    private fun observeViewModel() {
        viewModel.securityState.observe(viewLifecycleOwner) { state ->
            binding.switchAutoArm.isChecked = false
        }
    }

    private fun checkForUpdate() {
        val mgr = UpdateManager(requireContext())
        lifecycleScope.launch {
            binding.btnCheckUpdate.isEnabled = false
            binding.btnCheckUpdate.text = "Checking…"

            val update = mgr.checkForUpdate()

            if (update == null) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Up to Date")
                    .setMessage("You're running the latest version of SmartPocket.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Update Available")
                    .setMessage("${update.latestVersion} is available.\n\n${update.releaseNotes}")
                    .setPositiveButton("Download & Install") { _, _ ->
                        downloadUpdate(mgr, update)
                    }
                    .setNegativeButton("Later", null)
                    .show()
            }

            binding.btnCheckUpdate.isEnabled = true
            binding.btnCheckUpdate.text = "Check for Updates"
        }
    }

    private fun downloadUpdate(mgr: UpdateManager, update: com.smartpocket.utils.UpdateInfo) {
        val progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Downloading Update")
            .setMessage("Downloading ${update.latestVersion}…")
            .setView(progressBar)
            .setCancelable(false)
            .show()

        lifecycleScope.launch {
            val success = mgr.downloadAndInstall(update) { pct ->
                progressBar.progress = pct
                dialog.setMessage("Downloading ${update.latestVersion}… $pct%")
            }
            if (!success) {
                dialog.dismiss()
                AlertDialog.Builder(requireContext())
                    .setTitle("Download Failed")
                    .setMessage("Could not download the update. Check your internet connection and try again.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                dialog.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
