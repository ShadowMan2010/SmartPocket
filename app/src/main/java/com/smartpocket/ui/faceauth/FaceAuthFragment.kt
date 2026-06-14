package com.smartpocket.ui.faceauth

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.smartpocket.R
import com.smartpocket.databinding.FragmentFaceAuthBinding
import com.smartpocket.ml.FaceEmbeddingManager
import com.smartpocket.ui.home.SecurityViewModel
import com.smartpocket.utils.FaceAuthState
import com.smartpocket.utils.LivenessStep
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FaceAuthFragment : Fragment() {

    private var _binding: FragmentFaceAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SecurityViewModel by activityViewModels()

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var faceDetector: FaceDetector
    private lateinit var faceEmbeddingManager: FaceEmbeddingManager

    private var currentMode = Mode.IDLE
    private var livenessStep = LivenessStep.BLINK
    private var livenessProgress = 0f
    private var lastBlinkTime = 0L
    private var faceEmbeddingCapture: FloatArray? = null

    private enum class Mode { IDLE, ENROLLING, VERIFYING }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFaceAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFaceDetector()
        setupCamera()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupFaceDetector() {
        faceEmbeddingManager = FaceEmbeddingManager(requireContext())

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        faceDetector = FaceDetection.getClient(options)
    }

    private fun setupCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, FaceAnalyzer())
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("FaceAuth", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupClickListeners() {
        binding.btnEnroll.setOnClickListener {
            startEnrollment()
        }

        binding.btnVerify.setOnClickListener {
            startVerification()
        }

        binding.btnReEnroll.setOnClickListener {
            faceEmbeddingManager.clearEnrolledFace()
            viewModel.setFaceAuthEnabled(false)
            resetUI()
        }
    }

    private fun observeViewModel() {
        viewModel.securityState.observe(viewLifecycleOwner) { state ->
            val isEnrolled = state.faceAuthState != FaceAuthState.NOT_ENROLLED
            binding.btnEnroll.visibility = if (isEnrolled) View.GONE else View.VISIBLE
            binding.btnVerify.visibility = if (isEnrolled && state.faceAuthEnabled) View.VISIBLE else View.GONE
            binding.btnReEnroll.visibility = if (isEnrolled) View.VISIBLE else View.GONE
        }
    }

    // ─────────────────────────── ENROLLMENT ───────────────────────────

    private fun startEnrollment() {
        currentMode = Mode.ENROLLING
        livenessStep = LivenessStep.BLINK
        livenessProgress = 0f
        updateLivenessHint()
        binding.layoutLiveness.visibility = View.VISIBLE
        binding.btnEnroll.isEnabled = false
        binding.tvFaceHint.text = getString(R.string.face_liveness_hint)
    }

    private fun updateLivenessHint() {
        binding.tvFaceHint.text = when (livenessStep) {
            LivenessStep.BLINK -> "Please blink slowly…"
            LivenessStep.TURN_LEFT -> "Turn your head slightly left"
            LivenessStep.TURN_RIGHT -> "Turn your head slightly right"
            LivenessStep.SMILE -> "Give a small smile"
        }
    }

    // ─────────────────────────── VERIFICATION ───────────────────────────

    private fun startVerification() {
        currentMode = Mode.VERIFYING
        binding.tvFaceHint.text = "Looking for your face…"
    }

    // ─────────────────────────── FACE ANALYSIS ───────────────────────────

    inner class FaceAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            if (currentMode == Mode.IDLE) {
                imageProxy.close()
                return
            }

            val bitmap = imageProxy.toBitmap()
            val inputImage = InputImage.fromBitmap(bitmap, imageProxy.imageInfo.rotationDegrees)

            faceDetector.process(inputImage)
                .addOnSuccessListener { faces ->
                    processFaces(faces, bitmap)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun processFaces(faces: List<Face>, bitmap: Bitmap) {
        if (faces.isEmpty()) {
            updateHint("Position your face within the oval")
            return
        }

        val face = faces[0]

        when (currentMode) {
            Mode.ENROLLING -> processEnrollmentFace(face, bitmap)
            Mode.VERIFYING -> processVerificationFace(face, bitmap)
            Mode.IDLE -> {}
        }
    }

    private fun processEnrollmentFace(face: Face, bitmap: Bitmap) {
        // Check liveness
        when (livenessStep) {
            LivenessStep.BLINK -> {
                val leftEyeProb = face.leftEyeOpenProbability ?: 1f
                val rightEyeProb = face.rightEyeOpenProbability ?: 1f
                val eyesClosed = leftEyeProb < 0.15f && rightEyeProb < 0.15f

                if (eyesClosed) {
                    val now = System.currentTimeMillis()
                    if (now - lastBlinkTime > 500) {
                        lastBlinkTime = now
                        advanceLiveness(bitmap)
                    }
                }
            }
            LivenessStep.TURN_LEFT -> {
                val yRot = face.headEulerAngleY
                if (yRot > 15f) advanceLiveness(bitmap)
            }
            LivenessStep.TURN_RIGHT -> {
                val yRot = face.headEulerAngleY
                if (yRot < -15f) advanceLiveness(bitmap)
            }
            LivenessStep.SMILE -> {
                val smileProb = face.smilingProbability ?: 0f
                if (smileProb > 0.7f) advanceLiveness(bitmap)
            }
        }
    }

    private fun advanceLiveness(bitmap: Bitmap) {
        livenessProgress += 0.25f
        updateProgress(livenessProgress)

        when (livenessStep) {
            LivenessStep.BLINK -> livenessStep = LivenessStep.TURN_LEFT
            LivenessStep.TURN_LEFT -> livenessStep = LivenessStep.TURN_RIGHT
            LivenessStep.TURN_RIGHT -> livenessStep = LivenessStep.SMILE
            LivenessStep.SMILE -> {
                // All liveness steps done — capture embedding
                completeEnrollment(bitmap)
                return
            }
        }
        updateLivenessHint()
    }

    private fun completeEnrollment(bitmap: Bitmap) {
        val embedding = faceEmbeddingManager.extractEmbedding(bitmap)
        if (embedding != null) {
            faceEmbeddingManager.saveEnrolledFace(embedding)
            viewModel.onFaceEnrolled()
            currentMode = Mode.IDLE
            showResultOverlay(success = true, message = getString(R.string.face_captured))
        } else {
            currentMode = Mode.IDLE
            showResultOverlay(success = false, message = "Enrollment failed — try again")
            resetUI()
        }
    }

    private fun processVerificationFace(face: Face, bitmap: Bitmap) {
        val queryEmbedding = faceEmbeddingManager.extractEmbedding(bitmap) ?: return
        val similarity = faceEmbeddingManager.compareFaces(queryEmbedding)

        if (similarity > FaceEmbeddingManager.VERIFICATION_THRESHOLD) {
            currentMode = Mode.IDLE
            viewModel.onFaceVerified()
            showResultOverlay(success = true, message = getString(R.string.face_success))
        } else if (similarity < 0.1f) {
            currentMode = Mode.IDLE
            viewModel.onFaceVerifyFailed()
            showResultOverlay(success = false, message = getString(R.string.face_fail))
        }
    }

    // ─────────────────────────── UI HELPERS ───────────────────────────

    private fun updateHint(text: String) {
        activity?.runOnUiThread { binding.tvFaceHint.text = text }
    }

    private fun updateProgress(progress: Float) {
        activity?.runOnUiThread {
            binding.progressLiveness.progress = (progress * 100).toInt()
        }
    }

    private fun showResultOverlay(success: Boolean, message: String) {
        activity?.runOnUiThread {
            binding.layoutResultOverlay.visibility = View.VISIBLE
            binding.ivResultIcon.setImageResource(
                if (success) R.drawable.ic_check_circle else R.drawable.ic_error_circle
            )
            binding.ivResultIcon.setColorFilter(
                ContextCompat.getColor(requireContext(),
                    if (success) R.color.sp_safe else R.color.sp_danger)
            )
            binding.tvResultMessage.text = message

            // Auto-dismiss after 2.5s
            binding.layoutResultOverlay.postDelayed({
                binding.layoutResultOverlay.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        binding.layoutResultOverlay.visibility = View.GONE
                        binding.layoutResultOverlay.alpha = 1f
                        if (!success) resetUI()
                    }
                    .start()
            }, 2500)
        }
    }

    private fun resetUI() {
        currentMode = Mode.IDLE
        binding.layoutLiveness.visibility = View.GONE
        binding.btnEnroll.isEnabled = true
        binding.tvFaceHint.text = getString(R.string.face_enroll_hint)
        binding.progressLiveness.progress = 0
        livenessProgress = 0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        faceDetector.close()
        _binding = null
    }
}
