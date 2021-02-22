package kz.iitu.diplom.crm.utils

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kz.iitu.diplom.crm.core.BaseActivity

//Extension for Task<QuerySnapshot>(get method)
fun Task<QuerySnapshot>.onSuccess(context: Context, listener: (document: QuerySnapshot) -> Unit): Task<QuerySnapshot> {
    addOnSuccessListener {
        (context as BaseActivity).unlock()
        listener.invoke(it)
    }
    return this
}

fun Task<QuerySnapshot>.onFailure(context: Context, listener: (exception: Exception) -> Unit): Task<QuerySnapshot> {
    addOnFailureListener {
        (context as BaseActivity).unlock()
        listener.invoke(it)
    }
    return this
}

//Extension for Task<Void>(update method)
fun Task<Void>.onUpdateSuccess(context: Context, listener: () -> Unit): Task<Void> {
    addOnSuccessListener {
        (context as BaseActivity).unlock()
        listener.invoke()
    }
    return this
}

fun Task<Void>.onUpdateFailure(context: Context, listener: (exception: Exception) -> Unit): Task<Void> {
    addOnFailureListener {
        (context as BaseActivity).unlock()
        listener.invoke(it)
    }
    return this
}


