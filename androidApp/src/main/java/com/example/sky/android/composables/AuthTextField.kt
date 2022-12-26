package com.example.sky.android.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.sky.android.R
import com.example.sky.ui.theme.ComponentDiffNormal
import com.example.sky.ui.theme.ErrorStart

const val nameMin = 3
const val nameMax = 50
const val surnameMin = 3
const val surnameMax = 50
const val patronymicMin = 3
const val patronymicMax = 50

@Composable
fun PasswordTextField(
    password: String,
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
    error: String? = null,
    placeholder: String? = null
){
    var isHidePass by remember{ mutableStateOf(true) }

    GeneralTextFiled(
        text = password,
        placeholder = if (placeholder != null) placeholder else stringResource(id = R.string.password),
        valid = valid,
        isErrorShow = isErrorShow,
        onValueChange = onValueChange,
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = stringResource(id = R.string.imageDescriptionPassword)) },
        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password ),
        error =  if (error != null) error else stringResource(id = R.string.passwordError),
        trailingIcon = { Icon(
            imageVector = ImageVector.vectorResource(id = if (isHidePass) R.drawable.eye_hide else R.drawable.eye_show),
            contentDescription = stringResource(id = R.string.imageDescriptionHideShowPassword),
            modifier = Modifier.clickable(onClick = { isHidePass = !isHidePass }),
        )},
        visualTransformation = if (isHidePass) PasswordVisualTransformation() else VisualTransformation.None,
    )
}

@Composable
fun EmailTextField(
    email: String,
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
){
    GeneralTextFiled(
        text = email,
        placeholder = stringResource(id = R.string.email),
        valid = valid,
        isErrorShow = isErrorShow,
        onValueChange = onValueChange,
        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = stringResource(id = R.string.imageDescriptionEmail)) },
        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Email ),
        error = stringResource(id = R.string.emailError)
    )
}

@Composable
fun TelephoneTextField(
    telephone: String,
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
){
    GeneralTextFiled(
        text = telephone,
        placeholder = stringResource(id = R.string.phone),
        valid = valid,
        isErrorShow = isErrorShow,
        onValueChange = onValueChange,
        leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = stringResource(id = R.string.imageDescriptionPhone)) },
        keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Phone ),
        error = stringResource(id = R.string.phoneError)
    )
}

@Composable
fun PatronymicTextFiled(
    patronymic: String,
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
){
    NamesTextFiled(
        text = patronymic,
        placeholder = stringResource(id = R.string.patronymic),
        valid = valid,
        isErrorShow = isErrorShow,
        onValueChange = onValueChange,
        min = patronymicMin,
        max =  patronymicMax,
        leadingIcon = { Icon(Icons.Filled.Face, contentDescription = stringResource(id = R.string.imageDescriptionAccount)) }
    )
}

@Composable
fun SurnameTextFiled(
    surname: String,
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
){
    NamesTextFiled(
        text = surname,
        placeholder = stringResource(id = R.string.surname),
        valid = valid,
        isErrorShow = isErrorShow,
        onValueChange = onValueChange,
        min = nameMin,
        max =  nameMax,
        leadingIcon = { Icon(Icons.Filled.Face, contentDescription = stringResource(id = R.string.imageDescriptionAccount)) }
    )
}

@Composable
fun NameTextFiled(
    name: String,
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
){
    NamesTextFiled(
        text = name,
        placeholder = stringResource(id = R.string.name),
        valid = valid,
        isErrorShow = isErrorShow,
        onValueChange = onValueChange,
        min = nameMin,
        max =  nameMax,
        leadingIcon = { Icon(Icons.Filled.Face, contentDescription = stringResource(id = R.string.imageDescriptionAccount)) }
    )
}

@Composable
fun NamesTextFiled(
    text: String,
    placeholder:String = "",
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
    min: Int = 0,
    max: Int = 100,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
){
    GeneralTextFiled(
        text = text,
        placeholder = placeholder,
        valid = valid,
        isErrorShow = isErrorShow,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        error = placeholder + " " +stringResource(id = if (text.length < min) R.string.signUpLittleError else if (text.length > max) R.string.signUpLargeError else R.string.signUpNumberError),
    )
}

@Composable
fun GeneralTextFiled(
    visualTransformation: VisualTransformation = VisualTransformation.None,
    text: String,
    placeholder:String = "",
    valid:Boolean,
    isErrorShow:Boolean,
    onValueChange:(String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    error: String = ""
){
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = ComponentDiffNormal),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            leadingIcon = leadingIcon,
            isError = isErrorShow && !valid,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
        )

        if (isErrorShow && !valid)
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = ErrorStart)
                    .fillMaxWidth()
            )
    }
}