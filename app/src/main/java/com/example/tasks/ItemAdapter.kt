package com.example.task

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat.registerReceiver
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.TimerService
import com.google.android.material.card.MaterialCardView
import java.util.logging.Handler

/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [Affirmation] data object.
 */
class ItemAdapter(
    private val context: Context,
    private val dataset: List<String>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private var time = 0.0

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val toggleButton: ToggleButton = view.findViewById(R.id.toggleButton)
        val cardView: MaterialCardView = view.findViewById(R.id.item_card_view)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = item
        var isRunning = false
        var elapsedTime = 0L
        var handler: Handler
        holder.toggleButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                elapsedTime = if (isRunning) elapsedTime else 0L
                isRunning = true

            } else {
                isRunning = false
            }
        }
    }


    private fun formatTime(time: Long): String {
        val minutes = time / 60000
        val seconds = (time / 1000) % 60
        val milliseconds = time % 1000 / 10
        return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size
}
