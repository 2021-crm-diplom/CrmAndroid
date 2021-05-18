package kz.iitu.diplom.crm.model

import com.google.firebase.firestore.QueryDocumentSnapshot

data class Employee(
    val firstName: String?,
    val lastName: String?,
    val password: String?,
    val phone: String?,
    val position: String?
) {
    constructor(document: QueryDocumentSnapshot): this(
        firstName = document.getString("firstName"),
        lastName= document.getString("lastName"),
        password = document.getString("password"),
        phone = document.getString("phone"),
        position = document.getString("position")
    )
}