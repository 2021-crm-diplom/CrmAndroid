package kz.iitu.diplom.crm.modules.trades.models

import android.os.Parcelable
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    val docId: String,
    val id: String?,
    val tradeId: String?,
    val title: String?,
    val isCompleted: Boolean?
) : Parcelable {
    constructor(document: QueryDocumentSnapshot): this(
        docId = document.id,
        id = document.getString("id"),
        tradeId = document.getString("tradeId"),
        title = document.getString("title"),
        isCompleted = document.getBoolean("isCompleted")
    )
}