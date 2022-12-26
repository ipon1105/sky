package com.example.sky.android.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.sky.android.R
import com.example.sky.ui.theme.*

// Класс для перечисления элементов пользовательского соглашения
private data class TermsAndConditionsCouple(
    val Hid: Int,   //HeaderId(Hid)
    val Bid: Int,   //BodyId  (Bid)
)

@Composable
fun TermsAndConditionsScreen(navController: NavHostController) {

    // Содержимое пользовательского соглашения
    val idList: List<TermsAndConditionsCouple> = listOf(
        TermsAndConditionsCouple(R.string.TCHeader1, R.string.TCText1),
        TermsAndConditionsCouple(R.string.TCHeader2, R.string.TCText2),
        TermsAndConditionsCouple(R.string.TCHeader3, R.string.TCText3),
        TermsAndConditionsCouple(R.string.TCHeader4, R.string.TCText4),
        TermsAndConditionsCouple(R.string.TCHeader5, R.string.TCText5),
        TermsAndConditionsCouple(R.string.TCHeader6, R.string.TCText6),
        TermsAndConditionsCouple(R.string.TCHeader7, R.string.TCText7),
        TermsAndConditionsCouple(R.string.TCHeader8, R.string.TCText8),
        TermsAndConditionsCouple(R.string.TCHeader9, R.string.TCText9),
        TermsAndConditionsCouple(R.string.TCHeader10, R.string.TCText10),
        TermsAndConditionsCouple(R.string.TCHeader11, R.string.TCText11),
        TermsAndConditionsCouple(R.string.TCHeader12, R.string.TCText12),
        TermsAndConditionsCouple(R.string.TCHeader13, R.string.TCText13),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(ScreenArea)
    ) {
        // Кнопка назад
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.ArrowBack),
                contentDescription = stringResource(id = R.string.imageDescriptionBack),
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        // Заголовок страницы
        Text(
            text = stringResource(id = R.string.TCHeaderMain),
            color = mainColor,
            fontSize = LargeFont,
            modifier = Modifier.padding(top = ComponentDiffNormal)
        )

        // Колонка всех элементов
        LazyColumn(content = {
            itemsIndexed(items = idList){ index, item ->
                HeaderText(id = item.Hid)
                BodyText(id = item.Bid)
            }
        })

    }
}

@Composable
private fun HeaderText(@StringRes id: Int){
    HeaderText(stringResource(id = id))
}

@Composable
private fun BodyText(@StringRes id: Int){
    BodyText(stringResource(id = id))
}

@Composable
private fun HeaderText(text: String){
    Text(
        text = "\t" + text,
        color = DarkGray,
        fontSize = NormalFont,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = ComponentDiffNormal)
    )
}

@Composable
private fun BodyText(text: String){
    for (str in text.split('\n'))
        Text(
            text = str,
            color = DarkGray,
            fontSize = SmallFont,
            modifier = Modifier.padding(top = ComponentDiffNormal),
            textAlign = TextAlign.Justify,
        )

}
