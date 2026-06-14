package com.smartpocket.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.smartpocket.R
import com.smartpocket.databinding.ActivityLockdownBinding
import com.smartpocket.ui.faceauth.FaceAuthActivity
import com.smartpocket.utils.ArmState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LockdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLockdownBinding
    private val timeFormat = SimpleDateFormat("HH:mm:ss · dd MMM", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())
    private var attempts = 0
    private val MAX_ATTEMPTS = 3
    private var isStripeVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show over lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        binding = ActivityLockdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        startAlertAnimation()
        updateTimestamp()
    }

    private fun setupUI() {
        binding.tvLockdownTime.text = timeFormat.format(Date())

        binding.btnVerifyIdentity.setOnClickListener {
            if (attempts >= MAX_ATTEMPTS) {
                binding.btnVerifyIdentity.isEnabled = false
                binding.tvAttempts.text = "Too many attempts. Contact emergency."
                return@setOnClickListener
            }
            startActivity(Intent(this, FaceAuthActivity::class.java).apply {
                putExtra("mode", "verify_lockdown")
            })
        }

        binding.btnEmergency.setOnClickListener {
            // Dial emergency contact
            val emergencyNumber = "112" // default; should be user-configured
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$emergencyNumber"))
            startActivity(intent)
        }

        updateAttemptsText()
    }

    private fun updateAttemptsText() {
        binding.tvAttempts.text = when (attempts) {
            0 -> ""
            1 -> "1 failed attempt"
            else -> "$attempts failed attempts"
        }
    }

    private fun updateTimestamp() {
        binding.tvLockdownTime.text = timeFormat.format(Date())
        handler.postDelayed({ updateTimestamp() }, 1000)
    }

    // ─────────────────────────── ANIMATIONS ───────────────────────────

    private fun startAlertAnimation() {
        startStripePulse()
        startIconPulse()
    }

    private fun startStripePulse() {
        val pulseRunnable = object : Runnable {
            override fun run() {
                if (isFinishing) return
                val alpha = if (isStripeVisible) 0.2f else 0.8f
                binding.viewStripeTop.animate().alpha(alpha).setDuration(600).start()
                binding.viewStripeBottom.animate().alpha(alpha).setDuration(600).start()
                isStripeVisible = !isStripeVisible
                handler.postDelayed(this, 600)
            }
        }
        handler.post(pulseRunnable)
    }

    private fun startIconPulse() {
        binding.layoutLockIcon.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(800)
            .withEndAction {
                binding.layoutLockIcon.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(800)
                    .withEndAction { startIconPulse() }
                    .start()
            }
            .start()
    }

    override fun onResume() {
        super.onResume()
        // Check if user successfully verified face in FaceAuthActivity
        // In real implementation, use shared ViewModel or BroadcastReceiver
    }

    /** Called by FaceAuthActivity via broadcast when face is verified */
    fun onFaceVerifiedSuccessfully() {
        handler.removeCallbacksAndMessages(null)
        finish()
    }

    override fun onBackPressed() {
        // Disable back button during lockdown
        // Do not call super
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
