package com.example.task

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.MainViewModel
import com.example.tasks.R
import com.google.android.material.card.MaterialCardView

/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [Affirmation] data object.
 */
class ItemAdapter(
    private val context: Context,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val localViewModel: MainViewModel = viewModel

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
        val item = localViewModel.tasks.value!![position]
        holder.textView.text = item
        var isRunning = false
        var elapsedTime = 0L
        val handler: Handler = Handler()
        holder.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val startTime = System.currentTimeMillis()
                isRunning = true
                handler.post(object : Runnable {
                    override fun run() {
                        val currentTime = System.currentTimeMillis()
                        elapsedTime = currentTime - startTime
                        holder.textView.text = formatTime(elapsedTime)
                        if (isRunning) {
                            handler.postDelayed(this, 10)
                        }
                    }
                })
            } else {
                isRunning = false
                handler.removeCallbacksAndMessages(null)
                localViewModel.addTaskTime(item, elapsedTime.toInt())
                elapsedTime = 0
                holder.textView.text = item
            }
        }
        holder.cardView.setOnClickListener {
            localViewModel.removeTask(item)
            notifyItemRemoved(position)
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
    override fun getItemCount() = localViewModel.tasks.value!!.size
}
