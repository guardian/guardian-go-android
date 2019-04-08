package com.guardian.go.home.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.guardian.go.R
import com.guardian.go.articles.ui.fragments.ArticleFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bArticle.setOnClickListener {
            val intent = Intent(requireContext(), ArticleFragment::class.java)
            startActivity(intent)
        }
        bSettings.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_toSettings))
        bPicker.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_toPicker))
    }
}