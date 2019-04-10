package com.guardian.go.articles.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guardian.go.R
import com.guardian.go.articles.ui.models.Card
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card.view.*

class CardListAdapter(
    private val cardClickListener: (Card) -> Unit
) : ListAdapter<Card, CardViewHolder>(Card.ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(parent, cardClickListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CardViewHolder(
    parent: ViewGroup,
    private val articleClickListener: (Card) -> Unit
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)),
    View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    private val vTopBorder: View = itemView.vTopBorder
    private val tvTitle: TextView = itemView.tvTitle
    private val tvByline: TextView = itemView.tvByline
    private val ivMainImage: ImageView = itemView.ivMainImage

    private var currentCard: Card? = null

    fun bind(card: Card) {
        tvTitle.text = formatTitle(card)
        tvByline.text = card.byline?.title.orEmpty()
        Picasso.get()
            .load(card.mainImage?.mediumUrl)
            .into(ivMainImage)
        currentCard = card
    }

    private fun formatTitle(card: Card): String {
        return card.item.title
    }

    override fun onClick(v: View?) {
        currentCard?.let(articleClickListener)
    }
}