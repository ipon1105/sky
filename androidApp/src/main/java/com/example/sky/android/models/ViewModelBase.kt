package com.example.sky.android.models

import android.util.Log
import com.example.sky.android.models.data.Admin
import com.example.sky.android.models.data.Flat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

private const val TAG = "ViewModelBase"

// Получаем модель квартиры из базы данных
suspend fun getFlatFromFirestore(flatId: String): Flat {
    var flat = Flat()

    try {
        Firebase.firestore.collection("Flat").document(flatId).get()
            .addOnCompleteListener(){ task ->
                Log.i(TAG, "getFlatFromFirestore load is successful")
                val document = task.result.toObject(Flat::class.java)
                if (document != null) {
                    flat = document
                }
            }
            .addOnSuccessListener {
                Log.i(TAG, "getFlatFromFirestore load is successful")
            }.addOnFailureListener{
                Log.e(TAG, "getFlatFromFirestore load is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "getFlatFromFirestore load is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.d(TAG, "getFlatFromFirestore: $e")
    }

    return flat
}

// Изменяем модель квартиры в базе данных
suspend fun updateFlatToFirestore(flatId: String, data: Flat){
    try {
        Firebase.firestore
            .collection("Flat")
            .document(flatId)
            .set(data)
            .addOnCompleteListener(){
                Log.i(TAG, "updateFlatToFirestore update is successful")
            }.addOnSuccessListener {
                Log.i(TAG, "updateFlatToFirestore update is successful")
            }.addOnFailureListener{
                Log.e(TAG, "updateFlatToFirestore update is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "updateFlatToFirestore update is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.d(TAG, "updateFlatToFirestore: $e")
    }
}

// Создать модель квартиры в базе данных
suspend fun createFlatInFirestore(data: Flat, admin: Admin):String{
    var id = Firebase.auth.currentUser?.uid ?: ""

    try {
        data.owner = id
        Firebase.firestore.collection("Flat").add(data)
            .addOnCompleteListener(){
                id = it.result.id
                Log.i(TAG, "createFlatInFirestore is complete")
            }
            .addOnSuccessListener {
                Log.i(TAG, "createFlatInFirestore save is successful")
            }
            .addOnFailureListener{
                Log.e(TAG, "createFlatInFirestore save is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "createFlatInFirestore save is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.d(TAG, "createFlatInFirestore: $e")
    }

    admin.flatList += id
    updateAdminInFirestore(Firebase.auth.currentUser?.uid ?: "" , admin)

    return id
}

// Получаем модель квартиры из базы данных
suspend fun getAdminFromFirestore(adminId: String): Admin {
    var admin = Admin()
    Log.d(TAG, "admin: $adminId")

    try {
        Firebase.firestore.collection("Admin").document(adminId).get()
            .addOnCompleteListener(){ task ->
                Log.i(TAG, "getAdminFromFirestore load is complete")
                val document = task.result.toObject(Admin::class.java)
                if (document != null) {
                    admin = document
                }
            }.addOnSuccessListener {
                Log.i(TAG, "getAdminFromFirestore save is successful")
            }.addOnFailureListener{
                Log.e(TAG, "getAdminFromFirestore save is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "getAdminFromFirestore save is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.d(TAG, "getAdminFromFirestore: $e")
    }

    return admin
}

// Изменяем модель квартиры в базе данных
suspend fun updateAdminInFirestore(adminId: String, data: Admin){
    try {
        Firebase.firestore.collection("Admin").document(adminId).set(data)
            .addOnCompleteListener(){
                Log.i(TAG, "updateAdminInFirestore save is complete")
            }
            .addOnSuccessListener {
                Log.i(TAG, "updateAdminInFirestore save is successful")
            }.addOnFailureListener{
                Log.e(TAG, "updateAdminInFirestore save is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "updateAdminInFirestore save is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.d(TAG, "updateAdminInFirestore: $e")
    }
}

// Изменяем модель квартиры в базе данных
suspend fun getFlatListFromFirestore(adminId: String) : List<Flat> {
    val admin = getAdminFromFirestore(adminId)
    var list: List<Flat> = listOf()

    try {
        Firebase.firestore.collection("Flat").get()
            .addOnCompleteListener(){
                Log.i(TAG, "getFlatListFromFirestore geting is start complete")

                for (i in it.result)
                    if (admin.flatList.contains(i.id)) {
                        val f = i.toObject(Flat::class.java)
                        f.flatId = i.id;
                        list += f
                    }

                Log.i(TAG, "getFlatListFromFirestore geting is stop complete")
                Log.i(TAG, "getFlatListFromFirestore list = $list")
            }.addOnSuccessListener {
                Log.i(TAG, "getFlatListFromFirestore get is successful")
            }.addOnFailureListener{
                Log.e(TAG, "getFlatListFromFirestore get is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "getFlatListFromFirestore get is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.d(TAG, "getFlatListFromFirestore: $e")
    }

    return list
}