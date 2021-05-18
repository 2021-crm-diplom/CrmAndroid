package kz.iitu.diplom.crm.modules.trades.models

import com.google.firebase.firestore.QueryDocumentSnapshot
import kz.iitu.diplom.crm.utils.isToday
import kz.iitu.diplom.crm.utils.parse
import java.text.SimpleDateFormat
import java.util.*

class Comment (
    val docId: String,
    val id: String?,
    val tradeId: String?,
    val text: String?,
    val author: String?,
    val date: String?
) {
    constructor(document: QueryDocumentSnapshot): this(
        docId = document.id,
        id = document.getString("id"),
        tradeId = document.getString("tradeId"),
        text = document.getString("text"),
        author = document.getString("author"),
        date =  document.getString("date")
    )

    val firstName = author?.split(" ")?.get(0)
    val lastName = author?.split(" ")?.get(1)

    companion object {
        @JvmStatic
        fun toCommentDate(dateString: String): String {
            val date = dateString.parse()
            return when {
                date.isToday() -> "Сегодня в ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)}"
                else -> SimpleDateFormat("dd MMM yyyy в HH:mm", Locale.getDefault()).format(date)
            }
        }
    }
}