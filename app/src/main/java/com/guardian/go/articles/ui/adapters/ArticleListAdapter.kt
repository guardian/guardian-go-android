package com.guardian.go.articles.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guardian.go.R
import com.guardian.go.articles.data.Content
import kotlinx.android.synthetic.main.item_content.view.*

class ArticleListAdapter(
    private val articleClickListener: (Content) -> Unit
) : RecyclerView.Adapter<ArticleListAdapter.ContentViewHolder>() {

    private val content: MutableList<Content> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(parent, articleClickListener)
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

    class ContentViewHolder(
        parent: ViewGroup,
        private val articleClickListener: (Content) -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
    ), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        private val tvTitle: TextView = itemView.tvTitle

        private val tvDuration: TextView = itemView.tvDuration

        private var content: Content? = null

        fun bind(content: Content) {
            tvTitle.text = content.title
            tvDuration.text = content.duration
            this.content = content
        }

        override fun onClick(v: View?) {
            val currentContent = content
            if (currentContent != null) {
                articleClickListener.invoke(currentContent)
            }
        }
    }
}