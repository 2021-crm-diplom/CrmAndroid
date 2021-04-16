package kz.iitu.diplom.crm.modules.tasks.models

import android.os.Parcelable
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.parcel.Parcelize
import kz.iitu.diplom.crm.utils.parse
import java.util.*

@Parcelize
data class Task (
    val documentId: String,
    val id: String,
    val title: String,
    val startDate: Date,
    val deadline: Date,
    val description: String?,
    val employee: String?,
    val status: TaskStatus
) : Parcelable {

    constructor(document: QueryDocumentSnapshot) : this(
        documentId = document.id,
        id = document.getString("id") ?: throw Exception("Id cannot be null"),
        title = document.getString("title") ?: throw Exception("Title cannot be null"),
        startDate = document.getString("startDate")?.parse() ?: throw Exception("startDate cannot be null"),
        deadline = document.getString("deadline")?.parse() ?: throw Exception("deadline cannot be null"),
        description = document.getString("description"),
        employee = document.getString("employee"),
        status = TaskStatus.fromStringValue(document.getString("status")) ?: throw Exception("status cannot be null")
    )
}