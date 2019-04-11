package com.guardian.go.articles.data

import android.content.Context
import com.guardian.go.articles.ui.models.Card
import com.guardian.go.articles.ui.models.Group
import com.guardian.go.articles.ui.models.MiniNewsraker
import io.reactivex.Single
import java.util.concurrent.TimeUnit

interface CardsRepository {
    fun getCards(): Single<List<Card>>
}

class CodeMapiCardsRepository(context: Context) : CardsRepository {

    private val newsraker = MiniNewsraker.get(context)

    override fun getCards() = newsraker
        .getGroup("https://mobile.code.dev-guardianapis.com/au/groups/collections/6bb3-9f76-43bd-4213")
        .map { it.cards }
}

class TestCardsRepository : CardsRepository {
    override fun getCards(): Single<List<Card>> {
        return Single.just(emptyList())
        /*
        return Single.just(
            listOf(
                Content(
                    "Libya: UK urges France to condemn Khalifa Haftar’s assault on Tripoli ",
                    "2m",
                    "https://www.theguardian.com/world/2019/apr/08/eu-uk-urges-france-to-condemn-khalifa-haftar-assault-on-tripoli",
                    0
                ),
                Content(
                    "Russia moves to free nearly 100 captive whales after outcry",
                    "1m",
                    "https://www.theguardian.com/world/2019/apr/08/russia-free-nearly-100-captive-whales-outcry-cousteau",
                    0
                ),
                Content(
                    "Sudan security forces use teargas against peaceful protesters ",
                    "10m",
                    "https://www.theguardian.com/world/2019/apr/08/sudan-security-forces-use-teargas-against-peaceful-protesters",
                    0
                ),
                Content(
                    "I ditched my iPhone and it changed my life ",
                    "3m 30s",
                    "https://www.theguardian.com/commentisfree/2019/apr/08/charlotte-church-ditched-iphone-saved-my-life",
                    0
                )
            )
        ).delay(1, TimeUnit.SECONDS)
        */
    }
}