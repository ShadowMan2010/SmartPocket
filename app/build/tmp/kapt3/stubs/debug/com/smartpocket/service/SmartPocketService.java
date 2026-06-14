package com.smartpocket.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\u0014\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\b\u0010\u000b\u001a\u00020\u0006H\u0016J\b\u0010\f\u001a\u00020\u0006H\u0016J\"\u0010\r\u001a\u00020\u000e2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000eH\u0016J\u0016\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013\u00a8\u0006\u0016"}, d2 = {"Lcom/smartpocket/service/SmartPocketService;", "Landroid/app/Service;", "()V", "buildServiceNotification", "Landroid/app/Notification;", "createNotificationChannels", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "sendAlertNotification", "title", "", "body", "Companion", "app_debug"})
public final class SmartPocketService extends android.app.Service {
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_SERVICE = "sp_service";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_ALERTS = "sp_alerts";
    public static final int NOTIF_SERVICE_ID = 1001;
    public static final int NOTIF_ALERT_ID = 1002;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_DISARM = "com.smartpocket.ACTION_DISARM";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_ALERT_BAG = "com.smartpocket.ALERT_BAG";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_ALERT_BT = "com.smartpocket.ALERT_BT";
    @org.jetbrains.annotations.NotNull
    public static final com.smartpocket.service.SmartPocketService.Companion Companion = null;
    
    public SmartPocketService() {
        super();
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return null;
    }
    
    private final void createNotificationChannels() {
    }
    
    private final android.app.Notification buildServiceNotification() {
        return null;
    }
    
    public final void sendAlertNotification(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String body) {
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/smartpocket/service/SmartPocketService$Companion;", "", "()V", "ACTION_ALERT_BAG", "", "ACTION_ALERT_BT", "ACTION_DISARM", "CHANNEL_ALERTS", "CHANNEL_SERVICE", "NOTIF_ALERT_ID", "", "NOTIF_SERVICE_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}