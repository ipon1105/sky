package com.example.sky.android.models

import android.net.Uri
import android.util.Log
import com.example.sky.android.models.data.Admin
import com.example.sky.android.models.data.Flat
import com.example.sky.android.models.data.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

private const val TAG = "ViewModelBase"

// Функци возвращает уникальный идентификатор пользователя
fun getUserId() : String{
    val res = Firebase.auth.currentUser?.uid
    return if (res == null) "" else res
}

// Авторизован ли пользователь
fun isUserAuth() : Boolean {
    return  if (getUserId().equals("")) false else true
}

// Удалить изображение из базы
suspend fun deleteImage(path: String){

    try {
        Firebase.storage.reference.child(path).delete()
            .addOnCompleteListener() {
                Log.i(TAG, "deleteImage is complete")
            }.addOnSuccessListener {
                Log.i(TAG, "deleteImage is successful")
            }.addOnFailureListener {
                Log.e(TAG, "deleteImage is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "deleteImage is cancel")
            }.await()
    } catch (e: Exception){
        Log.e(TAG, "deleteImage is cancel")
    }

}

// Получить ссылку на скичавание изобажения
suspend fun getUriLink(name: String): Uri {
    var res = Uri.parse("none")

    try {
        Firebase.storage.reference.child(name).downloadUrl
            .addOnCompleteListener() {
                Log.i(TAG, "getUriLink is complete")
                if (it.isSuccessful)
                    res = it.result
                Log.i(TAG, "getUriLink save to $res")
            }.addOnSuccessListener {
                Log.i(TAG, "getUriLink is successful")
            }.addOnFailureListener {
                Log.e(TAG, "getUriLink is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "getUriLink is cancel")
            }.await()
    } catch (e: Exception){
        Log.e(TAG, "getUriLink is cancel")
    }

    return res
}

// Обновить данные о квартире и возвращает ссылку на файл
suspend fun updateFlatImage(uri: Uri, name: String, flatId: String) : String{
    val res = "${getUserId()}/flats/$flatId/$name"

    try {
        Firebase
            .storage
            .reference
            .child(res)
            .putFile(uri)
            .addOnCompleteListener(){
                Log.i(TAG, "updateFlatImage is complete")
                Log.i(TAG, "updateFlatImage save to $res")
            }.addOnSuccessListener {
                Log.i(TAG, "updateFlatImage is successful")
            }.addOnFailureListener{
                Log.e(TAG, "updateFlatImage is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "updateFlatImage is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "updateFlatImage: $e")
    }

    return res
}

// Изменяем модель квартиры в базе данных
// если успешно, то сообщение вернёт идентификатор квартиры
suspend fun updateFlatToFirestore(flatId: String, data: Flat){
    try {
        Firebase.firestore
            .collection("Flat")
            .document(flatId)
            .set(data)
            .addOnCompleteListener(){
                Log.i(TAG, "updateFlatToFirestore update is complete")
            }.addOnSuccessListener {
                Log.i(TAG, "updateFlatToFirestore update is successful")
            }.addOnFailureListener{
                Log.e(TAG, "updateFlatToFirestore update is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "updateFlatToFirestore update is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "updateFlatToFirestore: $e")
    }
}

// Обновляем данные администратора
suspend fun updateAdminInFirestore(data: Admin){
    try {
        Firebase.firestore.collection("Admin").document(getUserId()).set(data)
            .addOnCompleteListener(){
                Log.i(TAG, "updateAdminInFirestore save is complete")
            }.addOnSuccessListener {
                Log.i(TAG, "updateAdminInFirestore save is successful")
            }.addOnFailureListener{
                Log.e(TAG, "updateAdminInFirestore save is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "updateAdminInFirestore save is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "updateAdminInFirestore: $e")
    }
}

// Получаем модель квартиры из базы данных
suspend fun getFlatFromFirestore(flatId: String) : Flat {
    var flat = Flat()

    try {
        Firebase.firestore.collection("Flat").document(flatId).get()
            .addOnCompleteListener(){ task ->
                Log.i(TAG, "getFlatFromFirestore load is complete")

                val document = task.result.toObject(Flat::class.java)
                if (document != null)
                    flat = document

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

// Создать модель квартиры в базе данных вернёт идентификатор квартиры
suspend fun createFlatInFirestore(data: Flat) : String{
    var id = getUserId()

    try {
        data.owner = id
        Firebase.firestore.collection("Flat").add(data)
            .addOnCompleteListener(){
                Log.i(TAG, "createFlatInFirestore is complete")
                id = it.result.id
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
        Log.e(TAG, "createFlatInFirestore: $e")
    }

    return id
}

// Удаляем данные о квартире из администратора
suspend fun deleteFlatFromAdminFromFirestore(flatId: String, admin: Admin){
    var newList: List<String> = emptyList()

    admin.flatList.forEach {
        if (!it.equals(flatId))
            newList += it
    }
    admin.flatList = newList

    try {
        Firebase.firestore.collection("Admin").document(admin.auth).update("flatList", newList)
            .addOnCompleteListener(){
                Log.i(TAG, "deleteFlatFromAdminFromFirestore updating is complete")
            }.addOnSuccessListener {
                Log.i(TAG, "deleteFlatFromAdminFromFirestore updating is successful")
            }.addOnFailureListener{
                Log.e(TAG, "deleteFlatFromAdminFromFirestore updating is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "deleteFlatFromAdminFromFirestore updating is cancel")
            }.await()

    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "deleteFlatFromAdminFromFirestore: $e")
    }
}

// Удаляем модель квартиры из базы данных и возвращаем идентификатор квартиры
suspend fun deleteFlatFromFirestore(flatId: String) : String{
    val res = flatId

    try {
        Firebase.firestore.collection("Flat").document(flatId).delete()
            .addOnCompleteListener(){
                Log.i(TAG, "deleteFlatFromFirestore deleting is complete")
            }.addOnSuccessListener {
                Log.i(TAG, "deleteFlatFromFirestore deleting is successful")
            }.addOnFailureListener{
                Log.e(TAG, "deleteFlatFromFirestore deleting is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "deleteFlatFromFirestore deleting is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "deleteFlatFromFirestore: $e")
    }

    return res
}

// Получаем запись администратора из базы данных
suspend fun getAdminFromFirestore() : Admin{
    var admin = Admin()

    try {
        Firebase.firestore.collection("Admin").document(getUserId()).get()
            .addOnCompleteListener(){ task ->
                Log.i(TAG, "getAdminFromFirestore load is complete")

                val document = task.result.toObject(Admin::class.java)
                if (document != null)
                    admin = document


                Log.i(TAG, "getAdminFromFirestore admin: $admin")
            }.addOnSuccessListener {
                Log.i(TAG, "getAdminFromFirestore save is successful")
            }.addOnFailureListener{
                Log.e(TAG, "getAdminFromFirestore save is fail")
            }.addOnCanceledListener {
                Log.e(TAG, "getAdminFromFirestore save is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "getAdminFromFirestore: $e")
    }

    return admin
}

// Получаем список квартир из администратора
suspend fun getFlatListFromFirestore(admin: Admin) : List<Flat> {
    var list: List<Flat> = listOf()

    try {
        Firebase.firestore.collection("Flat").get()
            .addOnCompleteListener(){
                Log.i(TAG, "getFlatListFromFirestore geting is start complete")

                for (i in it.result)
                    if (admin.flatList.contains(i.id)) {
                        val f = i.toObject(Flat::class.java)
                        f.flatId = i.id
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
        Log.e(TAG, "getFlatListFromFirestore: $e")
    }

    return list
}

// Производит вход и возвращает правду, если успешно
suspend fun signIn(email: String, password: String) : Boolean{
    var answer = false

    try{
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                Log.i(TAG, "signIn is start complete")

                if (it.isSuccessful && it.result.user != null)
                    answer = true

                Log.i(TAG, "signIn is stop complete")
            }.addOnSuccessListener {
                Log.i(TAG, "signIn is successful")
            }.addOnFailureListener{
                Log.e(TAG, "signIn is fail: ${it.message}")
            }.addOnCanceledListener {
                Log.e(TAG, "signIn is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "signIn: $e")
    }

    return answer
}

// Регистрирует нового пользователя и возвращает ссылку на него
suspend fun registrationNewUser(email: String, password: String) : String{
    var res = ""

    try {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                Log.i(TAG, "registrationNewUser is start complete")

                if (it.isSuccessful && it.result.user != null)
                    res = it.result.user!!.uid

                Log.i(TAG, "registrationNewUser is stop complete")
            }.addOnSuccessListener {
                Log.i(TAG, "registrationNewUser is successful")
            }.addOnFailureListener{
                Log.e(TAG, "registrationNewUser is fail: ${it.message}")
            }.addOnCanceledListener {
                Log.e(TAG, "registrationNewUser is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "registrationNewUser: $e")
    }

    return res
}

// Регистрирует Администратора и возврает ссылку на него
suspend fun createAdmin(userId: String, admin: Admin) : String{
    var res = ""

    try{
        Firebase.firestore.collection("Admin").document(userId).set(admin)
            .addOnCompleteListener(){
                Log.i(TAG, "createAdmin is start complete")

                if (it.isSuccessful)
                    res = userId

                Log.i(TAG, "createAdmin is stop complete")
            }.addOnSuccessListener {
                Log.i(TAG, "createAdmin is successful")
            }.addOnFailureListener{
                Log.e(TAG, "createAdmin is fail: ${it.message}")
            }.addOnCanceledListener {
                Log.e(TAG, "createAdmin is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "createAdmin: $e")
    }

    return res
}

// Регистрирует Работника и возврает ссылку на него
suspend fun createWorker(userId: String, worker: Worker) : String{
    var res = ""

    try{
        Firebase.firestore.collection("Worker").document(userId).set(worker)
            .addOnCompleteListener(){
                Log.i(TAG, "createWorker is start complete")

                if (it.isSuccessful)
                    res = userId

                Log.i(TAG, "createWorker is stop complete")
            }.addOnSuccessListener {
                Log.i(TAG, "createWorker is successful")
            }.addOnFailureListener{
                Log.e(TAG, "createWorker is fail: ${it.message}")
            }.addOnCanceledListener {
                Log.e(TAG, "createWorker is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "createWorker: $e")
    }

    return res
}

// Регистрирует пользовательские данные и возврает ссылку на них
suspend fun createUserData(data: UserData) : String {
    var res = ""

    try{
        Firebase.firestore.collection("UserData").add(data)
            .addOnCompleteListener(){
                Log.i(TAG, "createUserData is start complete")

                if (it.isSuccessful)
                    res = it.result.id

                Log.i(TAG, "createUserData is stop complete")
            }.addOnSuccessListener {
                Log.i(TAG, "createUserData is successful")
            }.addOnFailureListener{
                Log.e(TAG, "createUserData is fail: ${it.message}")
            }.addOnCanceledListener {
                Log.e(TAG, "createUserData is cancel")
            }.await()
    } catch (e: FirebaseFirestoreException){
        Log.e(TAG, "createUserData: $e")
    }

    return res
}