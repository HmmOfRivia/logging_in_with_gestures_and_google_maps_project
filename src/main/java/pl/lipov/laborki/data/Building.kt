package pl.lipov.laborki.data

import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng

data class Building(
    @DrawableRes val iconResId: Int,
    val name: String,
    val coord: LatLng
)