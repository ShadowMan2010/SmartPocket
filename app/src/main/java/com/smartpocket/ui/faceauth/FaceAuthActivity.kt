package com.smartpocket.ui.faceauth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.smartpocket.R
import com.smartpocket.databinding.ActivityFaceAuthBinding
import com.smartpocket.ml.FaceEmbeddingManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FaceAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceAuthBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var faceDetector: FaceDetector
    private lateinit var embeddingManager: FaceEmbeddingManager
    private var mode = "verify"
    private var verificationDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getStringExtra("mode") ?: "verify"

        embeddingManager = FaceEmbeddingManager(this)
        cameraExecutor = Executors.newSingleThreadExecutor()
        setupFaceDetector()
        startCamera()
        setupUI()
    }

    private fun setupFaceDetector() {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .build()
        faceDetector = FaceDetection.getClient(options)
    }

    private fun setupUI() {
        binding.tvFaceHint.text = "Look at the camera to verify your identity"
        binding.btnEnroll.visibility = View.GONE
        binding.btnVerify.visibility = View.VISIBLE
        binding.btnReEnroll.visibility = View.GONE

        binding.btnVerify.setOnClickListener {
            binding.tvFaceHint.text = "Verifying…"
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, VerifyAnalyzer())
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                android.util.Log.e("FaceAuthActivity", "Camera bind failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    inner class VerifyAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            if (verificationDone) { imageProxy.close(); return }

            val bitmap = imageProxy.toBitmap()
            val inputImage = InputImage.fromBitmap(bitmap, imageProxy.imageInfo.rotationDegrees)

            faceDetector.process(inputImage)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val embedding = embeddingManager.extractEmbedding(bitmap) ?: return@addOnSuccessListener
                        val similarity = embeddingManager.compareFaces(embedding)
                        if (similarity > FaceEmbeddingManager.VERIFICATION_THRESHOLD) {
                            verificationDone = true
                            onVerificationSuccess()
                        }
                    }
                }
                .addOnCompleteListener { imageProxy.close() }
        }
    }

    private fun onVerificationSuccess() {
        runOnUiThread {
            binding.tvFaceHint.text = getString(R.string.face_success)
            // Broadcast success back
            sendBroadcast(Intent("com.smartpocket.FACE_VERIFIED"))
        }
        // Close after a short delay
        binding.root.postDelayed({
            setResult(RESULT_OK)
            finish()
        }, 1200)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        faceDetector.close()
        embeddingManager.close()
    }
}
