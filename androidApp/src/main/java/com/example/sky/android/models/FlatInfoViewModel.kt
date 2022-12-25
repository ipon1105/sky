package com.example.sky.android.models

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.sky.android.models.data.Client
import com.example.sky.android.models.data.Deal
import com.example.sky.android.models.data.DetailList
import com.example.sky.android.models.data.Flat
import com.example.sky.android.screens.refreshModel
import com.example.sky.android.utils.connection.getDateTime
import com.example.sky.navigation.NavRoute
import com.example.sky.ui.theme.FlatGreen
import com.example.sky.ui.theme.FlatRed
import com.example.sky.ui.theme.FlatYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.time.Duration.Companion.seconds

class DealViewModel(): ViewModel(){
    private lateinit var context: Context
    val priceValid by derivedStateOf{ price.isNotEmpty() }

    var priceToast by mutableStateOf(false)
        private set
    var isClientInfoUpdating by mutableStateOf(false)
        private set
    var isNewDealCreating by mutableStateOf(false)
        private set

    // Информация сделки
    var price by mutableStateOf("")
        private set
    var colorIndex by mutableStateOf(0)
        private set
    var dateIn  by mutableStateOf(0L)
        private set
    var dateOut  by mutableStateOf(0L)
        private set

    // Информация клиента
    var clientIdM by mutableStateOf("")
        private set
    var image by mutableStateOf("")
        private set
    var fio by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set
    var passport by mutableStateOf("")
        private set
    var registration by mutableStateOf("")
        private set
    var imageLocalUri by mutableStateOf(Uri.EMPTY)
        private set
    var imageLoadUri by mutableStateOf(Uri.EMPTY)
        private set

    var isSelectedDeal by mutableStateOf(false)
        private set

    // Список всех сделок
    private var detailListId by mutableStateOf("")
        private set
    private var detailList by mutableStateOf(DetailList())
        private set
    var dealList by mutableStateOf(emptyList<Deal>())
        private set
    var isDealListLoading by mutableStateOf(false)
        private set

    fun start(detailListId: String, context: Context){
        this.detailListId = detailListId
        this.context = context

        downloadAllDealList()
    }

    fun updateClientInfo(){
        isClientInfoUpdating = true
        viewModelScope.launch(Dispatchers.Main) {
            Log.d("viewModel", "Start updating")

            try {
                // Создаём клиента
                var client = Client(
                    image = image,
                    fio = fio,
                    phone = phone,
                    passport = passport,
                    registration = registration,
                )

                // Обновляем изображение
                if (imageLocalUri != Uri.EMPTY)
                    client.image = updateClientImage(imageLocalUri, getFileName(imageLocalUri))

                // Создаём клиента в базе
                saveClient(clientIdM, client)
            } catch (e: Exception) {
                Log.d("viewModel", "Error updating: $e")
            }

            Log.d("viewModel", "Stop updating")
            isClientInfoUpdating = false
        }
    }

    private fun createNewDeal(dateStart: Timestamp, dateStop: Timestamp){
        isNewDealCreating = true
        viewModelScope.launch(Dispatchers.Main) {

            // Создаём клиента
            var client = Client(
                image = image,
                fio = fio,
                phone = phone,
                passport = passport,
                registration = registration,
            )

            // Обновляем изображение
            if (imageLocalUri != Uri.EMPTY)
                client.image = updateClientImage(imageLocalUri, getFileName(imageLocalUri))

            // Создаём клиента в базе
            val clientId = createClient(client)

            var tmp = colorIndex

            // Разбираемся в цветах
            when (colorIndex){
                0 -> colorIndex = FlatYellow.toArgb()
                1 -> colorIndex = FlatRed.toArgb()
                2 -> colorIndex = FlatGreen.toArgb()
            }

            // Создаём сделку
            val deal = Deal(
                color = colorIndex,
                dateStart = Timestamp(SimpleDateFormat("yyyy-MM-dd").parse(dateStart.toString()).time)  ,
                dateStop = Timestamp(SimpleDateFormat("yyyy-MM-dd").parse(dateStop.toString()).time ),
                pricePerDay = price,
                detail = clientId,
            )
            colorIndex = tmp

            var newDealId = createDealToFirebase(deal)
            dealList += deal

            detailList.dealIdList += newDealId
            saveDetail(detailListId, detailList)

            isNewDealCreating = false
        }
    }

    // Кнопка принять
    fun btnApplyClick(dateStart: Timestamp, dateStop: Timestamp){
        if (isSelectedDeal)
            updateClientInfo()
        else if (!priceValid) {
            priceToast = true

            viewModelScope.launch(Dispatchers.Main) {
                delay(10.seconds)
                priceToast = false
            }
        } else
            createNewDeal(dateStart, dateStop)
    }

    // Загружаю список всех сделок
    fun downloadAllDealList(){
        isDealListLoading = true
        viewModelScope.launch(Dispatchers.Main) {
            Log.d("viewModel", "Start Load deal list")

            try {
                // Загружаю айдишники сделок
                detailList = loadDetail(detailListId)

                // Выгружаю список всех сделок
                for (dealId in detailList.dealIdList)
                    dealList += loadDeal(dealId)

            } catch (e: Exception){
                Log.d("viewModel", "Error Load deal list: $e")
            }

            Log.d("viewModel", "Stop Load deal list")
            isDealListLoading = false
        }
    }

    // Сравнивает день с списокм сделок
    fun isBetween(day: Date) : Deal?{
        for (deal in dealList){
            val start = getDateTime(deal.dateStart.time)
            val stop = getDateTime(deal.dateStop.time)

            if (day >= start && day <= stop)
                return deal
        }
        return null
    }

    // Проверочки
    fun isInside(dayStart: Date, dayStop: Date) : Deal?{
        Log.d("viewModel", "start = $dayStart ; stop = $dayStop")
        for (deal in dealList){
            val start = getDateTime(deal.dateStart.time)
            val stop = getDateTime(deal.dateStop.time)

            if (dayStart <= start && dayStart >= start)
                return deal
            if (dayStart <= start && dayStop >= stop)
                return deal
        }
        return null
    }

    @JvmName("setDealGot1")
    fun setSelectedDeal(value: Boolean, deal: Deal?){
        this.isSelectedDeal = value
        if (isSelectedDeal){
            viewModelScope.launch(Dispatchers.Main) {
                Log.d("viewModel", "DEAL: $deal")
                dateIn = deal?.dateStart?.time ?: 0L
                if (deal != null) {
                    dateOut = deal.dateStop.time
                }
                if (deal != null) {
                    price = deal.pricePerDay
                }

                if (deal != null) {
                    clientIdM = deal.detail
                }

                var client = deal?.let { loadClient(it.detail) }
                Log.d("viewModel", "CLIENT: $client")
                if (client != null) {
                    fio = client.fio
                }
                if (client != null) {
                    phone = client.phone
                }
                if (client != null) {
                    passport = client.passport
                }
                if (client != null) {
                    registration = client.registration
                }
                if (client != null) {
                    image = client.image
                }

                imageLoadUri = getUriLink(image)
            }
        }
        else {
            imageLoadUri = Uri.EMPTY
            imageLocalUri = Uri.EMPTY
        }
    }

    @JvmName("setRegistration1")
    fun setRegistration(value: String){
        this.registration = value
    }

    @JvmName("setPassport1")
    fun setPassport(value: String){
        this.passport = value
    }

    @JvmName("setPhone1")
    fun setPhone(value: String){
        this.phone = value
    }

    @JvmName("setFio1")
    fun setFio(value: String){
        this.fio = value
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

    @JvmName("setPrice1")
    fun setPrice(value: String){
        this.price = value
    }

    fun addLocalImageUri(uri: Uri){
        imageLocalUri = uri
    }

    @JvmName("setColorIndex1")
    fun setColorIndex(value: Int){
        this.colorIndex = value
    }
}

// Модель для редактора квартир
class FlatInfoViewModel(): ViewModel(){
    private var flatId: String = ""
    lateinit var context: Context

    // Ссылка на текущего клиента
    private var clientId by mutableStateOf("")
    private val pricePerDayNonValid = "Невозможно сделать сделку, без цены за день."

    // objects variable
    var flat by mutableStateOf(Flat())
        private set
    var detail by mutableStateOf(DetailList())
        private set

    var allDeal by mutableStateOf(emptyList<Deal>())
    var isFlatLoading by mutableStateOf(false)
        private set
    var expanded by mutableStateOf(false)
        private set
    var isDeleting by mutableStateOf(false)
        private set
    var showConfirmDialog by mutableStateOf(false)
        private set
    var showDialog by mutableStateOf(false)
        private set
    var showInfoBlock by mutableStateOf(false)
        private set
    var dialogMsg by mutableStateOf("")
        private set

    var status by mutableStateOf(0)
        private set

    // moreDetailBlockVariable
    var downloadImageList by mutableStateOf(emptyList<Uri>())
        private set
    // deal variable
    var price by mutableStateOf("")
        private set
    var colorIndex by mutableStateOf(0)
        private set

    val priceValid by derivedStateOf { price.isNotEmpty() }
    // infoDealBlock variable
    var isInfoDealShow by mutableStateOf(false)
        private set
    var infoDeal by mutableStateOf(Deal())
        private set
    var detailShow by mutableStateOf(false)
        private set

    // editDealBlock variable
    var isEditDealShow by mutableStateOf(true)
        private set

    // addDealBlock variable
    var isAddDealShow by mutableStateOf(true)
        private set
    var image by mutableStateOf("")
        private set
    var fio by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set
    var passport by mutableStateOf("")
        private set
    var registration by mutableStateOf("")
        private set
    var imageLocalUri by mutableStateOf(Uri.EMPTY)
        private set
    var imageLoadUri by mutableStateOf(Uri.EMPTY)
        private set

    fun start(flatId: String, context: Context){
        this.flatId = flatId
        this.context = context

        loadFlat()
    }

    // Загрузить данные
    private fun loadFlat(){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                status = getStatus(getUserId())
                isFlatLoading = true

                // Загружаем информацию о квартире
                flat = getFlatFromFirestore(flatId)

                // Создаём ссылки
                for (el in flat.photos)
                    downloadImageList += getUriLink(el)

                // Загружаем список сделок
                if (flat.detail.isEmpty()) {
                    flat.detail = createDetail()
                    updateFlatToFirestore(flatId, flat)
                }

            } catch (e: Exception){
                showDialog = true
                dialogMsg = e.message.toString()
            }
            isFlatLoading = false
        }
    }

    fun deleting(navController: NavHostController){
        isDeleting = true
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val flatId = deleteFlatFromFirestore(flatId)
                val admin = getAdminFromFirestore()
                deleteFlatFromAdminFromFirestore(flatId, admin)

                isDeleting = false
                showDialog = false

                refreshModel()
                navController.popBackStack()
            } catch (e:Exception){
                isDeleting = false
                showDialog = true
                dialogMsg = e.message.toString()
            }

        }
    }

    // Редактировать запись
    fun btnEditClick(navController: NavHostController){
        if (isDeleting)
            showDialog = true
        else
            navController.navigate(route = NavRoute.Editor.route + "/${flatId}")
    }

    // Вернуться на предыдущую страницу
    fun btnBackClick(navController: NavHostController){
        if (isDeleting)
            showDialog = true
        else
            navController.popBackStack()
    }

    // Нажата кнопка добавить
    @RequiresApi(Build.VERSION_CODES.O)
    fun btnAddDealClick(dateStart: java.time.LocalDate, dateStop: java.time.LocalDate){
        if (!priceValid){
            showDialog = true
            dialogMsg = pricePerDayNonValid
        } else
            saveDeal(dateStart, dateStop)
    }

    // Загрузить клиента
    fun loadClientInfo(){
        viewModelScope.launch {
            // Попытка загрузить данные клиента
            try {
                Log.d("viewModel", "1")

                // Загружаем клиента из базы
                if (clientId.isNotEmpty()) {
                    val client = loadClient(clientId)

                    // Заполняем поля
                    image = client.image
                    fio = client.fio
                    phone = client.phone
                    passport = client.passport
                    registration = client.registration
                    Log.d("viewModel", "2 client = ${client}")

                    // Создаём ссылку на изображение
                    imageLoadUri = getUriLink(client.image)
                }
            // Неудачная попытка загрузить данные клиента
            } catch (e: Exception) {
                showDialog = true
                dialogMsg = e.message.toString()
            }
        }
    }

    // Сохранить изменения
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveDeal(dateStart: java.time.LocalDate, dateStop: java.time.LocalDate){
        Log.d("viewModel", "start save")
        viewModelScope.launch(Dispatchers.Main) {
            // Попытка сохранить изменения о клиента
            try {
                var client = Client(
                    image = image,
                    fio = fio,
                    phone = phone,
                    passport = passport,
                    registration = registration,
                )

                // Если мы добавили изображение
                if (imageLocalUri != Uri.EMPTY){
                    // Загружаем его в базу
                    client.image = createClientImage(imageLocalUri, getFileName(imageLocalUri), flatId)

                    // Создаём ссылку для скачивания
                    imageLocalUri = Uri.EMPTY
                    imageLoadUri = getUriLink(client.image)
                }

                // Создаём клиента в базе, если нет записи о нём
                if (clientId.isEmpty()) {
                    clientId = createClient(client)

                    var tmp = colorIndex
                    // Разбираемся в цветах
                    when (colorIndex){
                        0 -> colorIndex = FlatYellow.toArgb()
                        1 -> colorIndex = FlatRed.toArgb()
                        2 -> colorIndex = FlatGreen.toArgb()
                    }

                    // Создаём сделку
                    val deal = Deal(
                        color = colorIndex,
                        dateStart = Timestamp(SimpleDateFormat("yyyy-MM-dd").parse(dateStart.toString()).time)  ,
                        dateStop = Timestamp(SimpleDateFormat("yyyy-MM-dd").parse(dateStop.toString()).time ),
                        pricePerDay = price,
                        detail = clientId,
                    )

                    // востанавливаем цвет
                    colorIndex = tmp

                    // Создать запись в базе данных
                    val dealId = createDealToFirebase(deal)

                    // Загрузка всех сделок
                    if (flat.detail.isNotEmpty() && detail.dealIdList.isNotEmpty())
                        for (el in detail.dealIdList)
                            allDeal += loadDeal(el)

                    // Создаём список сделок, если первая сделка
                    if (flat.detail.isEmpty())
                        flat.detail = createDealList(DetailList(dealIdList = listOf()))

                    detail.dealIdList += dealId
                    saveDealList(flat.detail ,detail.dealIdList)

                    updateFlatToFirestore(flatId, flat)

                }
                // Сохраняем клиента в базе
                else
                    saveClient(clientId, client)

            // Неудачная попытка сохранить изменения о клиента
            } catch (e: Exception){
                Log.d("viewModel", "Exception: ${e.message.toString()}")
                showDialog = true
                dialogMsg = e.message.toString()
            }
        }
        Log.d("viewModel", "stop save")
    }

    // Удалить запись
    fun btnDeleteClick(){
        if (isDeleting || isFlatLoading)
            showDialog = true
        else
            showConfirmDialog = true
    }

    fun addImage(){

    }


    @JvmName("setAddDealShow1")
    fun setAddDealShow(value: Boolean){
        this.isAddDealShow = value
    }

    @JvmName("setEditDealShow1")
    fun setEditDealShow(value: Boolean){
        this.isEditDealShow = value
    }

    @JvmName("setInfoDealShow1")
    fun setInfoDealShow(value: Boolean){
        this.isInfoDealShow = value
    }

    @JvmName("setInfoDealShow1")
    fun setInfoDeal(value: Deal){
        this.detailShow = true
        this.infoDeal = value
        this.clientId = value.detail
        loadClientInfo()
    }

    @JvmName("setShowConfirmDialog1")
    fun setShowConfirmDialog(value: Boolean){
        showConfirmDialog = value
    }

    @JvmName("setShowInfoBlock1")
    fun setShowInfoBlock(value: Boolean){
        showInfoBlock = value
    }

    @JvmName("setShowDialog1")
    fun setShowDialog(value: Boolean){
        showDialog = value
    }

    @JvmName("setExpanded1")
    fun setExpanded(value: Boolean){
        expanded = value
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

    @JvmName("setPrice1")
    fun setPrice(value: String){
        this.price = value
    }

    fun addLocalImageUri(uri: Uri){
        imageLocalUri = uri
    }

}
