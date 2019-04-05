package com.guardian.go.articlepicker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guardian.go.R
import com.guardian.go.articlepicker.data.Content
import kotlinx.android.synthetic.main.item_content.view.*

class PickerAdapter : RecyclerView.Adapter<PickerAdapter.ContentViewHolder>() {

    private val content: MutableList<Content> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(parent)
    }

    override fun getItemCount(): Int = content.size

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(content[position])
    }

    fun setContent(content: List<Content>) {
        this.content.clear()
        this.content.addAll(content)
        notifyDataSetChanged()
    }

    class ContentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
    ) {

        private val tvTitle: TextView = itemView.tvTitle

        private val tvDuration: TextView = itemView.tvDuration

        fun bind(content: Content) {
            tvTitle.text = content.title
            tvDuration.text = content.duration
        }
    }
}