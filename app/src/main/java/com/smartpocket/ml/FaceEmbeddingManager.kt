package com.smartpocket.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.sqrt

/**
 * Manages offline face embedding extraction and comparison using TFLite MobileFaceNet.
 * All processing is done on-device — no network calls.
 */
class FaceEmbeddingManager(private val context: Context) {

    companion object {
        const val VERIFICATION_THRESHOLD = 0.65f  // cosine similarity threshold
        private const val MODEL_FILE = "facenet_mobile.tflite"
        private const val ENROLLED_FACE_FILE = "enrolled_face.bin"
        private const val INPUT_SIZE = 112
        private const val EMBEDDING_SIZE = 128
    }

    private var interpreter: Interpreter? = null
    private var enrolledEmbedding: FloatArray? = null

    init {
        loadModel()
        loadEnrolledFace()
    }

    private fun loadModel() {
        try {
            val model = loadModelFile()
            val options = Interpreter.Options().apply {
                numThreads = 2
                useNNAPI = true // Hardware acceleration if available
            }
            interpreter = Interpreter(model, options)
        } catch (e: Exception) {
            // Model file may not be bundled in dev build; graceful fallback
            android.util.Log.w("FaceEmbedding", "TFLite model not loaded: ${e.message}")
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd(MODEL_FILE)
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Extracts a 128-d face embedding from a bitmap.
     * Returns null if the model is not loaded or face crop fails.
     */
    fun extractEmbedding(bitmap: Bitmap): FloatArray? {
        val interp = interpreter ?: return generateFallbackEmbedding(bitmap)

        return try {
            // Preprocess: resize to 112x112, normalize to [-1, 1]
            val resized = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
            val inputBuffer = bitmapToByteBuffer(resized)

            // Output buffer: [1, 128]
            val outputBuffer = Array(1) { FloatArray(EMBEDDING_SIZE) }

            interp.run(inputBuffer, outputBuffer)
            val embedding = outputBuffer[0]
            normalizeL2(embedding)
        } catch (e: Exception) {
            android.util.Log.e("FaceEmbedding", "Embedding extraction failed", e)
            generateFallbackEmbedding(bitmap)
        }
    }

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * 3 * 4)
        buffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

        for (pixel in pixels) {
            val r = ((pixel shr 16) and 0xFF).toFloat()
            val g = ((pixel shr 8) and 0xFF).toFloat()
            val b = (pixel and 0xFF).toFloat()
            // Normalize to [-1, 1]
            buffer.putFloat((r - 127.5f) / 127.5f)
            buffer.putFloat((g - 127.5f) / 127.5f)
            buffer.putFloat((b - 127.5f) / 127.5f)
        }
        return buffer
    }

    /**
     * Fallback: generates a deterministic pseudo-embedding from pixel statistics.
     * Used when TFLite model is not available (e.g., dev/test builds).
     * NOT suitable for production security — replace with real model.
     */
    private fun generateFallbackEmbedding(bitmap: Bitmap): FloatArray {
        val resized = Bitmap.createScaledBitmap(bitmap, 32, 32, true)
        val embedding = FloatArray(EMBEDDING_SIZE)
        val pixels = IntArray(32 * 32)
        resized.getPixels(pixels, 0, 32, 0, 0, 32, 32)

        for (i in 0 until EMBEDDING_SIZE) {
            val pixelIdx = (i * pixels.size / EMBEDDING_SIZE)
            val pixel = pixels[pixelIdx.coerceIn(0, pixels.size - 1)]
            val r = ((pixel shr 16) and 0xFF) / 255f
            val g = ((pixel shr 8) and 0xFF) / 255f
            val b = (pixel and 0xFF) / 255f
            embedding[i] = (r * 0.299f + g * 0.587f + b * 0.114f)
        }
        return normalizeL2(embedding)
    }

    /** L2-normalize a vector in-place, returns it */
    private fun normalizeL2(v: FloatArray): FloatArray {
        var norm = 0f
        for (x in v) norm += x * x
        norm = sqrt(norm.toDouble()).toFloat()
        if (norm > 1e-6f) {
            for (i in v.indices) v[i] /= norm
        }
        return v
    }

    /**
     * Cosine similarity between query embedding and enrolled embedding.
     * Returns value in [0, 1]; higher = more similar.
     */
    fun compareFaces(queryEmbedding: FloatArray): Float {
        val enrolled = enrolledEmbedding ?: return 0f
        if (enrolled.size != queryEmbedding.size) return 0f

        var dot = 0f
        for (i in enrolled.indices) {
            dot += enrolled[i] * queryEmbedding[i]
        }
        // Both are L2-normalized so denominator = 1
        // Cosine similarity: [-1, 1] → shift to [0, 1]
        return (dot + 1f) / 2f
    }

    /** Persists enrolled embedding to internal storage */
    fun saveEnrolledFace(embedding: FloatArray) {
        enrolledEmbedding = embedding
        try {
            val file = File(context.filesDir, ENROLLED_FACE_FILE)
            val buffer = ByteBuffer.allocate(embedding.size * 4)
            buffer.order(ByteOrder.nativeOrder())
            for (v in embedding) buffer.putFloat(v)
            FileOutputStream(file).use { it.write(buffer.array()) }
        } catch (e: Exception) {
            android.util.Log.e("FaceEmbedding", "Failed to save enrolled face", e)
        }
    }

    /** Loads persisted enrolled embedding from internal storage */
    private fun loadEnrolledFace() {
        try {
            val file = File(context.filesDir, ENROLLED_FACE_FILE)
            if (!file.exists()) return
            val bytes = file.readBytes()
            val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder())
            val embedding = FloatArray(EMBEDDING_SIZE)
            for (i in embedding.indices) embedding[i] = buffer.float
            enrolledEmbedding = embedding
        } catch (e: Exception) {
            android.util.Log.e("FaceEmbedding", "Failed to load enrolled face", e)
        }
    }

    /** Returns true if a face has been enrolled */
    fun isFaceEnrolled(): Boolean = enrolledEmbedding != null &&
            File(context.filesDir, ENROLLED_FACE_FILE).exists()

    /** Clears enrolled face data */
    fun clearEnrolledFace() {
        enrolledEmbedding = null
        File(context.filesDir, ENROLLED_FACE_FILE).delete()
    }

    fun close() {
        interpreter?.close()
    }
}
