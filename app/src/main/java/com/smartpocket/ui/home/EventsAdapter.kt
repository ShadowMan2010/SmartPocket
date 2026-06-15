package com.smartpocket.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartpocket.R
import com.smartpocket.utils.SecurityEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventsAdapter : ListAdapter<SecurityEvent, EventsAdapter.EventViewHolder>(DiffCallback) {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), timeFormat)
    }

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvMessage: TextView = view.findViewById(R.id.tv_event_message)
        private val tvTime: TextView = view.findViewById(R.id.tv_event_time)
        private val ivIcon: ImageView = view.findViewById(R.id.iv_event_icon)
        private val viewSeverity: View = view.findViewById(R.id.view_severity_dot)

        fun bind(event: SecurityEvent, timeFormat: SimpleDateFormat) {
            tvMessage.text = event.message
            tvTime.text = timeFormat.format(Date(event.timestamp))

            val ctx = itemView.context

            // Icon
            val iconRes = when (event.type) {
                SecurityEvent.EventType.BAG_BREACH -> R.drawable.ic_bag
                SecurityEvent.EventType.FACE_VERIFIED -> R.drawable.ic_face
                SecurityEvent.EventType.FACE_FAILED -> R.drawable.ic_face
                SecurityEvent.EventType.BT_LOST -> R.drawable.ic_bluetooth
                SecurityEvent.EventType.BT_RECONNECTED -> R.drawable.ic_bluetooth
                SecurityEvent.EventType.ARMED -> R.drawable.ic_shield
                SecurityEvent.EventType.DISARMED -> R.drawable.ic_shield
                SecurityEvent.EventType.LOCKDOWN_ACTIVATED -> R.drawable.ic_lock
                SecurityEvent.EventType.LOCKDOWN_CLEARED -> R.drawable.ic_shield
                SecurityEvent.EventType.SNATCH_DETECTED -> R.drawable.ic_flash
            }
            ivIcon.setImageResource(iconRes)

            // Severity color
            val severityColor = when (event.severity) {
                SecurityEvent.Severity.INFO -> R.color.sp_accent_primary
                SecurityEvent.Severity.WARNING -> R.color.sp_warning
                SecurityEvent.Severity.CRITICAL -> R.color.sp_danger
            }
            viewSeverity.setBackgroundTintList(ContextCompat.getColorStateList(ctx, severityColor))
            ivIcon.setColorFilter(ContextCompat.getColor(ctx, severityColor))
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<SecurityEvent>() {
        override fun areItemsTheSame(a: SecurityEvent, b: SecurityEvent) = a.id == b.id
        override fun areContentsTheSame(a: SecurityEvent, b: SecurityEvent) = a == b
    }
}
