package com.example.sky.android.composables

import android.content.Context
import android.widget.Toast

fun mToast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}