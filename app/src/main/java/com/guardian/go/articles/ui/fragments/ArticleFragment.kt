package com.guardian.go.articles.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.guardian.go.R
import com.guardian.go.articles.ui.viewmodels.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment() {

    private lateinit var articleViewModel: ArticleViewModel

    private lateinit var args: ArticleFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = ArticleFragmentArgs.fromBundle(requireArguments())
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        articleViewModel.model.observe(this, Observer { model ->
            if (model != null) {
                wvArticle.loadUrl(model.url)
            }
        })
        articleViewModel.loadContent(args.content)
    }
}