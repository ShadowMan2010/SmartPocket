package com.smartpocket.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartpocket.R
import com.smartpocket.databinding.FragmentHomeBinding
import com.smartpocket.utils.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SecurityViewModel by activityViewModels()
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        animateEntrance()
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter()
        binding.rvEvents.apply {
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }
    }

    private fun setupClickListeners() {
        binding.btnShield.setOnClickListener {
            val state = viewModel.securityState.value ?: return@setOnClickListener
            if (state.armState == ArmState.LOCKDOWN) return@setOnClickListener
            viewModel.toggleArm()
            animateShieldPress()
        }

        binding.switchBag.setOnCheckedChangeListener { _, checked ->
            viewModel.setBagSensorEnabled(checked)
        }

        binding.switchFace.setOnCheckedChangeListener { _, checked ->
            viewModel.setFaceAuthEnabled(checked)
        }

        binding.switchBluetooth.setOnCheckedChangeListener { _, checked ->
            viewModel.setBluetoothGuardEnabled(checked)
        }

        binding.switchLockdown.setOnCheckedChangeListener { _, checked ->
            viewModel.setLockdownEnabled(checked)
        }

        binding.switchSnatch.setOnCheckedChangeListener { _, checked ->
            viewModel.setSnatchEnabled(checked)
        }

        // Navigate to face auth on tap
        binding.cardFace.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_face)
        }

        // Navigate to BT on tap
        binding.cardBluetooth.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_bluetooth)
        }

        // Lockdown tap → launch lockdown screen for preview
        binding.cardLockdown.setOnLongClickListener {
            if (viewModel.securityState.value?.armState == ArmState.ARMED) {
                viewModel.triggerLockdown()
            }
            true
        }
    }

    private fun observeViewModel() {
        viewModel.securityState.observe(viewLifecycleOwner) { state ->
            updateShieldUI(state)
            updateCardStates(state)
        }

        viewModel.events.observe(viewLifecycleOwner) { events ->
            eventsAdapter.submitList(events.take(5))
        }

        viewModel.alertTrigger.observe(viewLifecycleOwner) { event ->
            event ?: return@observe
            if (event.type == SecurityEvent.EventType.LOCKDOWN_ACTIVATED) {
                startActivity(Intent(requireContext(), LockdownActivity::class.java))
            }
        }
    }

    private fun updateShieldUI(state: SecurityState) {
        val ctx = requireContext()

        // Shield background
        val (shieldBg, iconTint, statusText, statusColor) = when (state.armState) {
            ArmState.DISARMED -> Tuple4(
                R.drawable.bg_shield_normal,
                R.color.sp_accent_secondary,
                getString(R.string.status_disarmed),
                R.color.sp_text_secondary
            )
            ArmState.ARMED -> Tuple4(
                R.drawable.bg_shield_armed,
                R.color.sp_safe,
                getString(R.string.status_armed),
                R.color.sp_safe
            )
            ArmState.ALERT -> Tuple4(
                R.drawable.bg_shield_alert,
                R.color.sp_warning,
                getString(R.string.status_alert),
                R.color.sp_warning
            )
            ArmState.LOCKDOWN -> Tuple4(
                R.drawable.bg_shield_alert,
                R.color.sp_danger,
                getString(R.string.status_lockdown),
                R.color.sp_danger
            )
        }

        binding.btnShield.setBackgroundResource(shieldBg)
        binding.ivShieldIcon.setColorFilter(ContextCompat.getColor(ctx, iconTint))
        binding.tvArmStatus.text = statusText
        binding.tvArmStatus.setTextColor(ContextCompat.getColor(ctx, statusColor))

        // Status dot
        val dotColor = when (state.armState) {
            ArmState.DISARMED -> R.color.sp_text_tertiary
            ArmState.ARMED -> R.color.sp_safe
            ArmState.ALERT -> R.color.sp_warning
            ArmState.LOCKDOWN -> R.color.sp_danger
        }
        binding.viewStatusDot.setBackgroundTintList(
            ContextCompat.getColorStateList(ctx, dotColor)
        )

        // Hint text
        binding.tvArmHint.text = when (state.armState) {
            ArmState.DISARMED -> getString(R.string.tap_to_arm)
            ArmState.ARMED -> "Tap to disarm protection"
            ArmState.ALERT -> "Alert — tap to disarm"
            ArmState.LOCKDOWN -> "Lockdown active"
        }

        // Pulse animation for armed/alert states
        if (state.armState == ArmState.ARMED || state.armState == ArmState.ALERT) {
            startPulseAnimation()
        } else {
            stopPulseAnimation()
        }
    }

    private fun updateCardStates(state: SecurityState) {
        // Bag sensor
        binding.switchBag.isChecked = state.bagSensorActive
        binding.tvBagStatus.text = when (state.bagSensorState) {
            BagSensorState.INACTIVE -> getString(R.string.label_inactive)
            BagSensorState.MONITORING -> "Monitoring"
            BagSensorState.BREACHED -> "⚠ Breach detected!"
        }
        binding.tvBagStatus.setTextColor(
            ContextCompat.getColor(requireContext(), when (state.bagSensorState) {
                BagSensorState.BREACHED -> R.color.sp_danger
                BagSensorState.MONITORING -> R.color.sp_safe
                else -> R.color.sp_text_tertiary
            })
        )

        // Face
        binding.switchFace.isChecked = state.faceAuthEnabled
        binding.tvFaceStatus.text = when (state.faceAuthState) {
            FaceAuthState.NOT_ENROLLED -> getString(R.string.label_not_enrolled)
            FaceAuthState.ENROLLED -> getString(R.string.label_enrolled)
            FaceAuthState.VERIFYING -> "Verifying…"
            FaceAuthState.VERIFIED -> "Verified ✓"
            FaceAuthState.FAILED -> "Verification failed"
        }

        // Bluetooth
        binding.switchBluetooth.isChecked = state.bluetoothGuardActive
        binding.tvBtStatus.text = when (state.bluetoothGuardState) {
            BluetoothGuardState.INACTIVE -> getString(R.string.label_inactive)
            BluetoothGuardState.MONITORING -> "Scanning…"
            BluetoothGuardState.SIGNAL_STRONG -> "Strong signal"
            BluetoothGuardState.SIGNAL_MEDIUM -> "Medium signal"
            BluetoothGuardState.SIGNAL_WEAK -> "Weak — moving away"
            BluetoothGuardState.SIGNAL_LOST -> "⚠ Signal lost!"
        }

        // Lockdown
        binding.switchLockdown.isChecked = state.lockdownEnabled
        binding.tvLockdownStatus.text = if (state.lockdownEnabled) "Will activate on breach" else "Disabled"

        // Snatch detection
        binding.switchSnatch.isChecked = state.snatchActive
        binding.tvSnatchStatus.text = when (state.snatchState) {
            SnatchState.INACTIVE -> getString(R.string.label_inactive)
            SnatchState.MONITORING -> "Watching"
            SnatchState.TRIGGERED -> "⚠ Snatch detected!"
        }
        binding.tvSnatchStatus.setTextColor(
            ContextCompat.getColor(requireContext(), when (state.snatchState) {
                SnatchState.TRIGGERED -> R.color.sp_danger
                SnatchState.MONITORING -> R.color.sp_safe
                else -> R.color.sp_text_tertiary
            })
        )
    }

    // ─────────────────────────── ANIMATIONS ───────────────────────────

    private fun animateEntrance() {
        val cards = listOf(binding.cardBag, binding.cardFace, binding.cardBluetooth, binding.cardLockdown, binding.cardSnatch)
        cards.forEachIndexed { i, card ->
            card.alpha = 0f
            card.translationY = 40f
            card.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(350)
                .setStartDelay(100L + i * 60L)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    private fun animateShieldPress() {
        val scaleDown = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.btnShield, "scaleX", 1f, 0.92f),
                ObjectAnimator.ofFloat(binding.btnShield, "scaleY", 1f, 0.92f)
            )
            duration = 80
        }
        val scaleUp = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.btnShield, "scaleX", 0.92f, 1f),
                ObjectAnimator.ofFloat(binding.btnShield, "scaleY", 0.92f, 1f)
            )
            duration = 200
            interpolator = DecelerateInterpolator()
        }
        AnimatorSet().apply {
            playSequentially(scaleDown, scaleUp)
            start()
        }
    }

    private var pulseAnimator: AnimatorSet? = null

    private fun startPulseAnimation() {
        if (pulseAnimator?.isRunning == true) return
        pulseAnimator = AnimatorSet().apply {
            val outer = ObjectAnimator.ofFloat(binding.viewPulseOuter, "scaleX", 1f, 1.08f, 1f).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
            }
            val outerY = ObjectAnimator.ofFloat(binding.viewPulseOuter, "scaleY", 1f, 1.08f, 1f).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
            }
            val inner = ObjectAnimator.ofFloat(binding.viewPulseInner, "scaleX", 1f, 1.12f, 1f).apply {
                duration = 2000
                startDelay = 200
                repeatCount = ObjectAnimator.INFINITE
            }
            val innerY = ObjectAnimator.ofFloat(binding.viewPulseInner, "scaleY", 1f, 1.12f, 1f).apply {
                duration = 2000
                startDelay = 200
                repeatCount = ObjectAnimator.INFINITE
            }
            playTogether(outer, outerY, inner, innerY)
            start()
        }
    }

    private fun stopPulseAnimation() {
        pulseAnimator?.cancel()
        pulseAnimator = null
        binding.viewPulseOuter.scaleX = 1f
        binding.viewPulseOuter.scaleY = 1f
        binding.viewPulseInner.scaleX = 1f
        binding.viewPulseInner.scaleY = 1f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pulseAnimator?.cancel()
        _binding = null
    }
}

// Simple 4-tuple helper
data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

operator fun <A, B, C, D> Tuple4<A, B, C, D>.component1() = a
operator fun <A, B, C, D> Tuple4<A, B, C, D>.component2() = b
operator fun <A, B, C, D> Tuple4<A, B, C, D>.component3() = c
operator fun <A, B, C, D> Tuple4<A, B, C, D>.component4() = d
