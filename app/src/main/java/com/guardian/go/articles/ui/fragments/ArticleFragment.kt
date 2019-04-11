package com.guardian.go.articles.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.guardian.go.R
import com.guardian.go.articles.ui.WebAppInterface
import com.guardian.go.articles.ui.models.ArticleItem
import com.guardian.go.articles.ui.viewmodels.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article.*
import java.io.File


class ArticleFragment : Fragment() {

    private lateinit var articleViewModel: ArticleViewModel

    private lateinit var args: ArticleFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = ArticleFragmentArgs.fromBundle(requireArguments())
        /*
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        articleViewModel.model.observe(this, Observer { model ->
            if (model != null) {
                if (model.isLoading) {
                    pbArticleLoading.visibility = View.VISIBLE
                    wvArticle.visibility = View.GONE
                } else {
                */
        pbArticleLoading.visibility = View.GONE
        wvArticle.settings.setJavaScriptEnabled(true);
        wvArticle.settings.setDomStorageEnabled(true);
        wvArticle.settings.setLoadWithOverviewMode(true);
        wvArticle.settings.setUseWideViewPort(true);
        wvArticle.settings.setBuiltInZoomControls(true);
        wvArticle.settings.setDisplayZoomControls(false);
        wvArticle.settings.setSupportZoom(true);
        wvArticle.settings.setDefaultTextEncodingName("utf-8");
        wvArticle.setVerticalScrollBarEnabled(false);
        wvArticle.visibility = View.VISIBLE
        wvArticle.loadData(createHtml(args.article), "text/html", "UTF-8")
        val jsInterface = WebAppInterface(requireContext()) {
            findNavController(requireView()).popBackStack()
        }
        wvArticle.addJavascriptInterface(jsInterface, "Android")
        /*
                }
            }
        })
        articleViewModel.loadContent(args.article)
        */
    }


    private fun createHtml(article: ArticleItem): String {
        val wrapperTemplate = HtmlTemplate.article.getTemplate(requireContext())
        val templateDir = getTemplateRoot(requireContext()).path
        // wrapper
        return wrapperTemplate
            .replace("__TEMPLATES_DIRECTORY__", "file:///android_asset/guardian-go-templates/build/")
            .replace("__ARTICLE_CONTAINER__", createArticleBody(article))
    }

    private fun createArticleBody(article: ArticleItem): String {
        val replacements = HashMap<String, String>()
        val articleTemplate = HtmlTemplate.articleContainer.getTemplate(requireContext())
        return articleTemplate
            .replace("__BODY__", article.body ?: "")
            .replace("__TITLE__", article.title)
            .replace("__STANDFIRST__", article.standFirst)
            .replace("__BYLINE__", article.byline)
            .replace("__PUBDATE__", article.webPublicationDate.toString())
            .replace("__MAIN_MEDIA__", article.headerImage?.mediumUrl ?: "")
            .replace("__NIGHT_MODE__", if (isNightMode) "night-mode" else "")
    }

    private val isNightMode: Boolean get() =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}


enum class HtmlTemplate(private val templateName: String) {
    article("articleTemplate.html"),
    articleContainer("articleTemplateContainer.html");

    fun getTemplate(context: Context): String {
        return context.assets.open("guardian-go-templates/build/$templateName").bufferedReader()
            .use { it.readText() }
    }
}


fun getTemplateRoot(context: Context): File {
    val templatesDir = File(context.filesDir, "guardian-go-templates/build")
    templatesDir.mkdir()
    return templatesDir
}