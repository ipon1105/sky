package com.example.sky.android.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sky.android.R
import com.example.sky.ui.theme.*

@Composable
fun NoInternet(onDismissRequest: ()->Unit, btnOnClick: ()->Unit){
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text( text = stringResource(id = R.string.error), color = FlatRed) },
        text =  { Text( text = stringResource(id = R.string.noConnection)) },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = alertDialogArea)
                    .padding(top = 0.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = btnOnClick,
                    shape = RoundedCornerShape(size = largeShape),
                    colors = ButtonDefaults.buttonColors( backgroundColor = mainColor),
                ) {
                    Text( text = stringResource(id = R.string.alertOkay), color = Color.White, modifier = Modifier.padding(all = TextTopSmall))
                }
            }
        }
    )
}