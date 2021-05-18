package kz.iitu.diplom.crm.modules.trades.models

import android.os.Parcelable
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kz.iitu.diplom.crm.utils.parse
import java.util.*

@Parcelize
data class Trade (
    val documentId: String,
    val id: String,
    val title: String,
    val startDate: Date,
    val deadline: Date,
    val description: String?,
    val employee: String?,
    val status: TradeStatus,
    val client: String?,
    val clientFirstName: String?,
    val clientLastName: String?,
    val tasks: @RawValue List<Task> = listOf()
) : Parcelable {

    constructor(document: QueryDocumentSnapshot) : this(
        documentId = document.id,
        id = document.getString("id") ?: throw Exception("Id cannot be null"),
        title = document.getString("title") ?: throw Exception("Title cannot be null"),
        startDate = document.getString("startDate")?.parse() ?: throw Exception("startDate cannot be null"),
        deadline = document.getString("deadline")?.parse() ?: throw Exception("deadline cannot be null"),
        description = document.getString("description"),
        employee = document.getString("employee"),
        status = TradeStatus.fromStringValue(document.getString("status")) ?: throw Exception("status cannot be null"),
        client = document.getString("client"),
        clientFirstName = document.getString("clientFirstName"),
        clientLastName = document.getString("clientLastName"),
    )
}