package kz.iitu.diplom.crm.core

import com.google.firebase.firestore.QuerySnapshot

interface AsyncCallback {
    fun onSuccess(result: QuerySnapshot)
    fun onFailure(e: Exception)
}