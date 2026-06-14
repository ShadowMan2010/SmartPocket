package com.smartpocket.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0012\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\u000fH\u0014J\u0006\u0010\u0014\u001a\u00020\u000fJ\b\u0010\u0015\u001a\u00020\u000fH\u0014J\b\u0010\u0016\u001a\u00020\u000fH\u0002J\b\u0010\u0017\u001a\u00020\u000fH\u0002J\b\u0010\u0018\u001a\u00020\u000fH\u0002J\b\u0010\u0019\u001a\u00020\u000fH\u0002J\b\u0010\u001a\u001a\u00020\u000fH\u0002J\b\u0010\u001b\u001a\u00020\u000fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/smartpocket/ui/home/LockdownActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "MAX_ATTEMPTS", "", "attempts", "binding", "Lcom/smartpocket/databinding/ActivityLockdownBinding;", "handler", "Landroid/os/Handler;", "isStripeVisible", "", "timeFormat", "Ljava/text/SimpleDateFormat;", "onBackPressed", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onFaceVerifiedSuccessfully", "onResume", "setupUI", "startAlertAnimation", "startIconPulse", "startStripePulse", "updateAttemptsText", "updateTimestamp", "app_debug"})
public final class LockdownActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.smartpocket.databinding.ActivityLockdownBinding binding;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat timeFormat = null;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler handler = null;
    private int attempts = 0;
    private final int MAX_ATTEMPTS = 3;
    private boolean isStripeVisible = true;
    
    public LockdownActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void updateAttemptsText() {
    }
    
    private final void updateTimestamp() {
    }
    
    private final void startAlertAnimation() {
    }
    
    private final void startStripePulse() {
    }
    
    private final void startIconPulse() {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    /**
     * Called by FaceAuthActivity via broadcast when face is verified
     */
    public final void onFaceVerifiedSuccessfully() {
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
}