package com.example.sky.android.models

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.Admin
import com.example.sky.android.models.data.Flat
import com.example.sky.android.screens.refreshModel
import com.example.sky.android.utils.connection.removeFromList
import com.example.sky.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

// Модель для редактора квартир
class FlatEditorViewModel(var flatId: String = "", var context: Context): ViewModel(){
    // Константы
    val maxDescriptionHeight = 150.dp
    val maxAddressLength = 255
    val maxCleaningCostLength = 16
    val maxDescriptionLength = 512

    // Переменные
    var newImageList by mutableStateOf(emptyList<Uri>())
        private set
    var oldImageList by mutableStateOf(emptyList<String>())
        private set
    private var deleteImageList by mutableStateOf(emptyList<String>())
    var downloadImageList by mutableStateOf(emptyList<Uri>())
        private set
    var admin by mutableStateOf(Admin())
        private set
    var flat by mutableStateOf(Flat())
        private set
    var address by mutableStateOf("")
        private set
    var cleaningCost by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var internet by mutableStateOf(true)
        private set
    var isFlatLoading by mutableStateOf(false)
        private set
    var isShowLittleError by mutableStateOf(false)
        private set
    var isEditable by mutableStateOf(false)
        private set
    var isEditableDialogShow by mutableStateOf(false)
        private set
    var isUpdation by mutableStateOf(false)
        private set
    var isUpdationToastShow by mutableStateOf(false)
        private set
    var dialogMsg by mutableStateOf("")
        private set
    var dialogShow by mutableStateOf(false)
        private set
    var isMaxImageCountToast by mutableStateOf(false)
        private set

    // Валидация
    val isAddressValid by derivedStateOf { address.length < maxAddressLength}
    val isCleaningCostValid by derivedStateOf { cleaningCost.length < maxCleaningCostLength }
    val isDescriptionValid by derivedStateOf { description.length < maxDescriptionLength  }

    // Добавить новое изображение
    fun addNewImage(uri: Uri){
        newImageList += uri
    }

    // Добавить изображение
    fun btnAddImageClick(launcher: ManagedActivityResultLauncher<String, Uri?>){
        if (isUpdation) {
            isUpdationToastShow = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isUpdationToastShow = false
            }
        } else if (oldImageList.size + newImageList.size >= 5) {
            isMaxImageCountToast = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isMaxImageCountToast = false
            }
        } else {
            launcher.launch("image/*")
            isEditable = true
        }
    }

    // Удалить изображение
    fun btnDeleteImageClick(index: Int){
        if (isUpdation) {
            isUpdationToastShow = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isUpdationToastShow = false
            }
        } else {
            isEditable = true
            viewModelScope.launch(Dispatchers.Main){
                try{
                    Log.d("viewModel", "index = $index")

                    if (index <= (downloadImageList.size - 1)){
                        deleteImageList += oldImageList.get(index)
                        oldImageList = removeFromList(oldImageList, index) as List<String>
                        downloadImageList = removeFromList(downloadImageList, index) as List<Uri>
                    } else {
                        newImageList = removeFromList(newImageList, index - downloadImageList.size) as List<Uri>
                    }


                // Неудачная попытка
                } catch (e: Exception){
                    isUpdation = false
                    dialogShow = true
                    isEditable = false
                    dialogMsg = e.message.toString()
                }
            }
        }
    }

    init {
        if (flatId.equals("null"))
            flatId = ""

        getAdmin()
        loadFlat()
    }

    // Загрукза административных данных
    private fun getAdmin(){
        viewModelScope.launch( Dispatchers.Main ) {
            admin = getAdminFromFirestore( )
        }
    }

    // Загрузить данные
    fun loadFlat(){
        if (!internet){
            dialogShow = true
            return
        }
        isFlatLoading = true
        viewModelScope.launch(Dispatchers.Main) {

            if (!flatId.equals(""))
                flat = getFlatFromFirestore(flatId)
            else
                flat = Flat()

            address = flat.address
            cleaningCost = flat.cleaningCost
            description = flat.description
            oldImageList = flat.photos
            for (s in oldImageList)
                downloadImageList += getUriLink(s)

            isFlatLoading = false
        }
    }

    // Обновить данные
    fun updateFlat(){
        if (!internet){
            dialogShow = true
            return
        }

        isUpdation = true
        viewModelScope.launch(Dispatchers.Main) {
            // Попытка обновить данные
            try {
                // Добавляем к списку изображений новые
                for (s in newImageList) {
                    var name = getFileName(s)

                    // Проверка имени файла на уникальность
                    while (true){
                        for (imageName in oldImageList)
                            if (imageName.equals("${getUserId()}/flats/$flatId/$name")){
                                name += "(1).jpg"
                                break
                            }
                        break
                    }

                    oldImageList += updateFlatImage(s, name, flatId)
                }
                newImageList = emptyList<Uri>()

                // Загружаем новый список квартир
                downloadImageList = emptyList<Uri>()
                for (s in oldImageList)
                    downloadImageList += getUriLink(s)

                // Обновляем данные квартиры
                flat.address = address
                flat.description = description
                flat.cleaningCost = cleaningCost
                flat.photos = oldImageList

                // Вносим изменения
                if (!flatId.equals(""))
                    updateFlatToFirestore(flatId, flat)
                else {
                    admin.flatList += createFlatInFirestore(flat)
                    updateAdminInFirestore(admin)
                }

                // Удаляем изображения из базы
                for (s in deleteImageList)
                    deleteImage(s)
                deleteImageList = emptyList()

                isUpdation = false
                isEditable = false

            // Неудачная попытка
            } catch (e: Exception){
                isUpdation = false
                dialogShow = true
                isEditable = false
                dialogMsg = e.message.toString()
            }
        }
    }

    // Вернуться на предыдущий экран
    fun btnBackClick(navController: NavHostController){
        if (!internet){
            dialogShow = true
            return
        }

        if (isUpdation) {
            isUpdationToastShow = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isUpdationToastShow = false
            }
        }else if (isEditable)
            isEditableDialogShow = true
        else {
            refreshModel()
            navController.popBackStack()
        }
    }

    // Вернуться на гланый экран
    fun btnCancelClick(navController: NavHostController){
        if (!internet){
            dialogShow = true
            return
        }

        if (isUpdation) {
            isUpdationToastShow = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                isUpdationToastShow = false
            }
        }else if (isEditable)
            isEditableDialogShow = true
        else {
            refreshModel()
            navController.navigate(NavRoute.Main.route){popUpTo(0)}
        }
    }

    // Принять изменения
    fun btnApplyClick(){
        if (!internet){
            dialogShow = true
            return
        }

        isShowLittleError = address.isEmpty() || description.isEmpty()

        if (!isShowLittleError && isAddressValid && isDescriptionValid)
            updateFlat()
    }

    @JvmName("setInternet1")
    fun setInternet(value: Boolean){
        internet = value
    }

    @JvmName("setAddress1")
    fun setAddress(value: String){
        address = value
        isEditable = true
    }

//   Скрытый функционал
//    @JvmName("setCleaningCost1")
//    fun setCleaningCost(value: String){
//        cleaningCost = value
//        isEditable = true
//    }

    @JvmName("setDescription1")
    fun setDescription(value: String){
        description = value
        isEditable = true
    }

    @JvmName("setDialogShow1")
    fun setDialogShow(value:Boolean){
        dialogShow = value
    }

    @JvmName("setEditableDialogShow1")
    fun setEditableDialogShow(value:Boolean){
        isEditableDialogShow = value
    }

    @JvmName("setShowLittleError1")
    fun setShowLittleError(value:Boolean){
        isShowLittleError = value
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return if (fileName == null) "" else fileName
    }

}
