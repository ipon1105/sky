package com.example.sky.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 11f)
    }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(cameraPositionState = cameraPositionState)
        Button(onClick = {
            // Move the camera to a new zoom level
            cameraPositionState.move(CameraUpdateFactory.zoomIn())
        }) {
            Text(text = "Zoom In")
        }
    }

}