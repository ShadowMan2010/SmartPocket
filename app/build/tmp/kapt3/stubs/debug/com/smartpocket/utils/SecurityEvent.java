package com.smartpocket.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001:\u0002!\"B3\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\nH\u00c6\u0003J;\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\nH\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006#"}, d2 = {"Lcom/smartpocket/utils/SecurityEvent;", "", "id", "", "type", "Lcom/smartpocket/utils/SecurityEvent$EventType;", "message", "", "timestamp", "severity", "Lcom/smartpocket/utils/SecurityEvent$Severity;", "(JLcom/smartpocket/utils/SecurityEvent$EventType;Ljava/lang/String;JLcom/smartpocket/utils/SecurityEvent$Severity;)V", "getId", "()J", "getMessage", "()Ljava/lang/String;", "getSeverity", "()Lcom/smartpocket/utils/SecurityEvent$Severity;", "getTimestamp", "getType", "()Lcom/smartpocket/utils/SecurityEvent$EventType;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "EventType", "Severity", "app_debug"})
public final class SecurityEvent {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull
    private final com.smartpocket.utils.SecurityEvent.EventType type = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String message = null;
    private final long timestamp = 0L;
    @org.jetbrains.annotations.NotNull
    private final com.smartpocket.utils.SecurityEvent.Severity severity = null;
    
    public SecurityEvent(long id, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.SecurityEvent.EventType type, @org.jetbrains.annotations.NotNull
    java.lang.String message, long timestamp, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.SecurityEvent.Severity severity) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.SecurityEvent.EventType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMessage() {
        return null;
    }
    
    public final long getTimestamp() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.SecurityEvent.Severity getSeverity() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.SecurityEvent.EventType component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.SecurityEvent.Severity component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.SecurityEvent copy(long id, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.SecurityEvent.EventType type, @org.jetbrains.annotations.NotNull
    java.lang.String message, long timestamp, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.SecurityEvent.Severity severity) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u000b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/smartpocket/utils/SecurityEvent$EventType;", "", "(Ljava/lang/String;I)V", "BAG_BREACH", "FACE_VERIFIED", "FACE_FAILED", "BT_LOST", "BT_RECONNECTED", "ARMED", "DISARMED", "LOCKDOWN_ACTIVATED", "LOCKDOWN_CLEARED", "app_debug"})
    public static enum EventType {
        /*public static final*/ BAG_BREACH /* = new BAG_BREACH() */,
        /*public static final*/ FACE_VERIFIED /* = new FACE_VERIFIED() */,
        /*public static final*/ FACE_FAILED /* = new FACE_FAILED() */,
        /*public static final*/ BT_LOST /* = new BT_LOST() */,
        /*public static final*/ BT_RECONNECTED /* = new BT_RECONNECTED() */,
        /*public static final*/ ARMED /* = new ARMED() */,
        /*public static final*/ DISARMED /* = new DISARMED() */,
        /*public static final*/ LOCKDOWN_ACTIVATED /* = new LOCKDOWN_ACTIVATED() */,
        /*public static final*/ LOCKDOWN_CLEARED /* = new LOCKDOWN_CLEARED() */;
        
        EventType() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.smartpocket.utils.SecurityEvent.EventType> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/smartpocket/utils/SecurityEvent$Severity;", "", "(Ljava/lang/String;I)V", "INFO", "WARNING", "CRITICAL", "app_debug"})
    public static enum Severity {
        /*public static final*/ INFO /* = new INFO() */,
        /*public static final*/ WARNING /* = new WARNING() */,
        /*public static final*/ CRITICAL /* = new CRITICAL() */;
        
        Severity() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.smartpocket.utils.SecurityEvent.Severity> getEntries() {
            return null;
        }
    }
}