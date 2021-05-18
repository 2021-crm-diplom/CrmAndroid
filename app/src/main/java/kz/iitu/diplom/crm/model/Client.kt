package kz.iitu.diplom.crm.model

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Client(
    val id: String?,
    val docId: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?
) {
    constructor(document: QueryDocumentSnapshot) : this(
        id = document.getString("id"),
        docId = document.id,
        firstName = document.getString("firstName"),
        lastName = document.getString("lastName"),
        phone = document.getString("phone")
    )
}