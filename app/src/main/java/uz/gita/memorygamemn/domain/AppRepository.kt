package uz.gita.memorygamemn.domain

import uz.gita.memorygamemn.R
import uz.gita.memorygamemn.data.CardData

class AppRepository {
    private val cardList = ArrayList<CardData>()

    init {
        cardList.add(CardData(R.drawable.p_1, 1))
        cardList.add(CardData(R.drawable.p_2, 2))
        cardList.add(CardData(R.drawable.p_3, 3))
        cardList.add(CardData(R.drawable.p_4, 4))
        cardList.add(CardData(R.drawable.p_5, 5))
        cardList.add(CardData(R.drawable.p_6, 6))
        cardList.add(CardData(R.drawable.p_7, 7))
        cardList.add(CardData(R.drawable.p_8, 8))
        cardList.add(CardData(R.drawable.p_9, 9))
        cardList.add(CardData(R.drawable.p_10, 10))
    }

    fun getData(count: Int): List<CardData> {
        cardList.shuffle()
        var lc = cardList.subList(0, count / 2)
        var result = ArrayList<CardData>(count)
        result.addAll(lc)
        result.addAll(lc)

        result.shuffle()

        return result
    }
}