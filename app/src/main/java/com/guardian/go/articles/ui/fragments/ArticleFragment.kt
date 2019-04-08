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

class ArticleFragment : Fragment() {

    private lateinit var splashViewModel: ArticleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splashViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        splashViewModel.model.observe(this, Observer { model ->
            if (model != null) {

            }
        })
    }
}