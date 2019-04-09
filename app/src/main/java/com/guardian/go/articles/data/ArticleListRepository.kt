package com.guardian.go.articles.data

import io.reactivex.Single
import java.util.concurrent.TimeUnit

interface ArticleListRepository {

    fun getContent(): Single<List<Content>>
}

class TestArticleListRepository : ArticleListRepository {
    override fun getContent(): Single<List<Content>> {
        return Single.just(
            listOf(
                Content(
                    "Libya: UK urges France to condemn Khalifa Haftar’s assault on Tripoli ",
                    "2m",
                    "https://www.theguardian.com/world/2019/apr/08/eu-uk-urges-france-to-condemn-khalifa-haftar-assault-on-tripoli"
                ),
                Content(
                    "Russia moves to free nearly 100 captive whales after outcry",
                    "1m",
                    "https://www.theguardian.com/world/2019/apr/08/russia-free-nearly-100-captive-whales-outcry-cousteau"
                ),
                Content(
                    "Sudan security forces use teargas against peaceful protesters ",
                    "10m",
                    "https://www.theguardian.com/world/2019/apr/08/sudan-security-forces-use-teargas-against-peaceful-protesters"
                ),
                Content(
                    "I ditched my iPhone and it changed my life ",
                    "3m 30s",
                    "https://www.theguardian.com/commentisfree/2019/apr/08/charlotte-church-ditched-iphone-saved-my-life"
                )
            )
        ).delay(1, TimeUnit.SECONDS)
    }
}