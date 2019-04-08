package com.guardian.go.articlepicker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.guardian.go.R
import com.guardian.go.articlepicker.data.TestPickerContentRepository
import com.guardian.go.articlepicker.ui.adapters.PickerAdapter
import com.guardian.go.articlepicker.ui.viewmodels.PickerViewModel
import kotlinx.android.synthetic.main.fragment_picker.*

class PickerFragment : Fragment() {

    private lateinit var viewModel: PickerViewModel

    private val pickerAdapter: PickerAdapter = PickerAdapter { content ->
        findNavController(requireView()).navigate(PickerFragmentDirections.actionPickerFragmentToArticleFragment(content))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvArticles.layoutManager = LinearLayoutManager(requireContext())
        rvArticles.setHasFixedSize(true)
        rvArticles.adapter = pickerAdapter
        viewModel = PickerViewModel(TestPickerContentRepository())
        viewModel.model.observe(this, Observer { model ->
            if (model != null) {
                loadModel(model)
            }
        })
        viewModel.loadArticles()
    }

    private fun loadModel(model: PickerViewModel.Model) {
        if (model.loading) {
            rvArticles.visibility = View.GONE
            pbArticlesLoading.visibility = View.VISIBLE
        } else {
            rvArticles.visibility = View.VISIBLE
            pbArticlesLoading.visibility = View.GONE
        }
        val articles = model.articles
        pickerAdapter.setContent(articles)
    }

    companion object {
        fun newInstance(): PickerFragment {
            return PickerFragment()
        }
    }
}