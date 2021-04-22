package kz.iitu.diplom.crm.core

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot

interface QuerySnapshotCallback {
    fun onSuccess(result: QuerySnapshot)
    fun onFailure(e: Exception)
}

interface DocumentReferenceCallback {
    fun onSuccess(result: DocumentReference)
    fun onFailure(e: Exception)
}