package com.guardian.go.articles.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.guardian.go.R
import com.guardian.go.articles.data.CodeMapiCardsRepository
import com.guardian.go.articles.ui.adapters.CardListAdapter
import com.guardian.go.articles.ui.viewmodels.ArticleListViewModel
import kotlinx.android.synthetic.main.fragment_article_list.*

class CardListFragment : Fragment() {

    private lateinit var viewModel: ArticleListViewModel

    private val cardListAdapter: CardListAdapter = CardListAdapter { card ->
        findNavController(requireView()).navigate(
            CardListFragmentDirections.actionArticleListFragmentToArticleFragment(card.item)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //rvArticles.layoutManager = LinearLayoutManager(requireContext())
        //rvArticles.layoutManager = SpannedGridLayoutManager(context, spanLookup, 3, 1.0f, 0)
        rvArticles.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvArticles.setHasFixedSize(true)
        rvArticles.adapter = cardListAdapter
        viewModel = ViewModelProviders.of(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ArticleListViewModel(CodeMapiCardsRepository(requireContext())) as T
            }
        }).get(ArticleListViewModel::class.java)
        viewModel.model.observe(this, Observer { model ->
            if (model != null) {
                loadModel(model)
            }
        })
        viewModel.loadArticles()
    }

    private fun loadModel(model: ArticleListViewModel.Model) {
        if (model.loading) {
            rvArticles.visibility = View.GONE
            pbArticlesLoading.visibility = View.VISIBLE
        } else {
            rvArticles.visibility = View.VISIBLE
            pbArticlesLoading.visibility = View.GONE
        }
        cardListAdapter.submitList(model.cards)
    }

    companion object {
        fun newInstance(): CardListFragment {
            return CardListFragment()
        }
    }
}

private val spanLookup = SpannedGridLayoutManager.GridSpanLookup { position ->
    val patternLength = 12
    val positionInPattern = position % patternLength

    when (positionInPattern) {
        0 -> SpannedGridLayoutManager.SpanInfo(2, 1)
        1 -> SpannedGridLayoutManager.SpanInfo(1, 1)
        2 -> SpannedGridLayoutManager.SpanInfo(1, 2)
        3, 4 -> SpannedGridLayoutManager.SpanInfo(2, 1)
        else -> SpannedGridLayoutManager.SpanInfo.SINGLE_CELL
    }
}
