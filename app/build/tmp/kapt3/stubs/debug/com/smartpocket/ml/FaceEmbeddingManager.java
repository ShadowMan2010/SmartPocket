package com.smartpocket.ml;

/**
 * Manages offline face embedding extraction and comparison using TFLite MobileFaceNet.
 * All processing is done on-device — no network calls.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u000eJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0006J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\u0017\u001a\u00020\u000eH\u0002J\b\u0010\u0018\u001a\u00020\u000eH\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J\u0010\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u0006H\u0002J\u000e\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/smartpocket/ml/FaceEmbeddingManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "enrolledEmbedding", "", "interpreter", "Lorg/tensorflow/lite/Interpreter;", "bitmapToByteBuffer", "Ljava/nio/ByteBuffer;", "bitmap", "Landroid/graphics/Bitmap;", "clearEnrolledFace", "", "close", "compareFaces", "", "queryEmbedding", "extractEmbedding", "generateFallbackEmbedding", "isFaceEnrolled", "", "loadEnrolledFace", "loadModel", "loadModelFile", "Ljava/nio/MappedByteBuffer;", "normalizeL2", "v", "saveEnrolledFace", "embedding", "Companion", "app_debug"})
public final class FaceEmbeddingManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    public static final float VERIFICATION_THRESHOLD = 0.65F;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String MODEL_FILE = "facenet_mobile.tflite";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String ENROLLED_FACE_FILE = "enrolled_face.bin";
    private static final int INPUT_SIZE = 112;
    private static final int EMBEDDING_SIZE = 128;
    @org.jetbrains.annotations.Nullable
    private org.tensorflow.lite.Interpreter interpreter;
    @org.jetbrains.annotations.Nullable
    private float[] enrolledEmbedding;
    @org.jetbrains.annotations.NotNull
    public static final com.smartpocket.ml.FaceEmbeddingManager.Companion Companion = null;
    
    public FaceEmbeddingManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    private final void loadModel() {
    }
    
    private final java.nio.MappedByteBuffer loadModelFile() {
        return null;
    }
    
    /**
     * Extracts a 128-d face embedding from a bitmap.
     * Returns null if the model is not loaded or face crop fails.
     */
    @org.jetbrains.annotations.Nullable
    public final float[] extractEmbedding(@org.jetbrains.annotations.NotNull
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    private final java.nio.ByteBuffer bitmapToByteBuffer(android.graphics.Bitmap bitmap) {
        return null;
    }
    
    /**
     * Fallback: generates a deterministic pseudo-embedding from pixel statistics.
     * Used when TFLite model is not available (e.g., dev/test builds).
     * NOT suitable for production security — replace with real model.
     */
    private final float[] generateFallbackEmbedding(android.graphics.Bitmap bitmap) {
        return null;
    }
    
    /**
     * L2-normalize a vector in-place, returns it
     */
    private final float[] normalizeL2(float[] v) {
        return null;
    }
    
    /**
     * Cosine similarity between query embedding and enrolled embedding.
     * Returns value in [0, 1]; higher = more similar.
     */
    public final float compareFaces(@org.jetbrains.annotations.NotNull
    float[] queryEmbedding) {
        return 0.0F;
    }
    
    /**
     * Persists enrolled embedding to internal storage
     */
    public final void saveEnrolledFace(@org.jetbrains.annotations.NotNull
    float[] embedding) {
    }
    
    /**
     * Loads persisted enrolled embedding from internal storage
     */
    private final void loadEnrolledFace() {
    }
    
    /**
     * Returns true if a face has been enrolled
     */
    public final boolean isFaceEnrolled() {
        return false;
    }
    
    /**
     * Clears enrolled face data
     */
    public final void clearEnrolledFace() {
    }
    
    public final void close() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/smartpocket/ml/FaceEmbeddingManager$Companion;", "", "()V", "EMBEDDING_SIZE", "", "ENROLLED_FACE_FILE", "", "INPUT_SIZE", "MODEL_FILE", "VERIFICATION_THRESHOLD", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}