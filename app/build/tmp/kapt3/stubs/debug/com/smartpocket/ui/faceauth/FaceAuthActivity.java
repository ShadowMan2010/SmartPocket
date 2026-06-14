package com.smartpocket.ui.faceauth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0018B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\u0010H\u0014J\b\u0010\u0014\u001a\u00020\u0010H\u0002J\b\u0010\u0015\u001a\u00020\u0010H\u0002J\b\u0010\u0016\u001a\u00020\u0010H\u0002J\b\u0010\u0017\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/smartpocket/ui/faceauth/FaceAuthActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/smartpocket/databinding/ActivityFaceAuthBinding;", "cameraExecutor", "Ljava/util/concurrent/ExecutorService;", "embeddingManager", "Lcom/smartpocket/ml/FaceEmbeddingManager;", "faceDetector", "Lcom/google/mlkit/vision/face/FaceDetector;", "mode", "", "verificationDone", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onVerificationSuccess", "setupFaceDetector", "setupUI", "startCamera", "VerifyAnalyzer", "app_debug"})
public final class FaceAuthActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.smartpocket.databinding.ActivityFaceAuthBinding binding;
    private java.util.concurrent.ExecutorService cameraExecutor;
    private com.google.mlkit.vision.face.FaceDetector faceDetector;
    private com.smartpocket.ml.FaceEmbeddingManager embeddingManager;
    @org.jetbrains.annotations.NotNull
    private java.lang.String mode = "verify";
    private boolean verificationDone = false;
    
    public FaceAuthActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupFaceDetector() {
    }
    
    private final void setupUI() {
    }
    
    private final void startCamera() {
    }
    
    private final void onVerificationSuccess() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0007"}, d2 = {"Lcom/smartpocket/ui/faceauth/FaceAuthActivity$VerifyAnalyzer;", "Landroidx/camera/core/ImageAnalysis$Analyzer;", "(Lcom/smartpocket/ui/faceauth/FaceAuthActivity;)V", "analyze", "", "imageProxy", "Landroidx/camera/core/ImageProxy;", "app_debug"})
    public final class VerifyAnalyzer implements androidx.camera.core.ImageAnalysis.Analyzer {
        
        public VerifyAnalyzer() {
            super();
        }
        
        @java.lang.Override
        public void analyze(@org.jetbrains.annotations.NotNull
        androidx.camera.core.ImageProxy imageProxy) {
        }
    }
}