package com.guardian.go.articles.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.guardian.go.R
import com.guardian.go.articles.data.TestArticleListRepository
import com.guardian.go.articles.ui.adapters.ArticleListAdapter
import com.guardian.go.articles.ui.viewmodels.ArticleListViewModel
import kotlinx.android.synthetic.main.fragment_article_list.*

class ArticleListFragment : Fragment() {

    private lateinit var viewModel: ArticleListViewModel

    private val articleListAdapter: ArticleListAdapter = ArticleListAdapter { content ->
        findNavController(requireView()).navigate(
            ArticleListFragmentDirections.actionArticleListFragmentToArticleFragment(content)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvArticles.layoutManager = LinearLayoutManager(requireContext())
        rvArticles.setHasFixedSize(true)
        rvArticles.adapter = articleListAdapter
        viewModel = ArticleListViewModel(TestArticleListRepository())
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
        val articles = model.articles
        articleListAdapter.setContent(articles)
    }

    companion object {
        fun newInstance(): ArticleListFragment {
            return ArticleListFragment()
        }
    }
}