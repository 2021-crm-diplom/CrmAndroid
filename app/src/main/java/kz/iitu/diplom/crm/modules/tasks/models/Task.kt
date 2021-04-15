package kz.iitu.diplom.crm.modules.tasks.models

import com.google.firebase.firestore.QueryDocumentSnapshot
import kz.iitu.diplom.crm.utils.parse
import java.util.*

data class Task (
    val id: String,
    val title: String,
    val startDate: Date,
    val deadline: Date,
    val description: String?,
    val employee: String?,
    val status: TaskStatus
) {

    constructor(document: QueryDocumentSnapshot) : this(
        id = document.getString("id") ?: throw Exception("Id cannot be null"),
        title = document.getString("title") ?: throw Exception("Title cannot be null"),
        startDate = document.getString("startDate")?.parse() ?: throw Exception("startDate cannot be null"),
        deadline = document.getString("deadline")?.parse() ?: throw Exception("deadline cannot be null"),
        description = document.getString("description"),
        employee = document.getString("employee"),
        status = TaskStatus.fromStringValue(document.getString("status")) ?: throw Exception("status cannot be null")
    )
}