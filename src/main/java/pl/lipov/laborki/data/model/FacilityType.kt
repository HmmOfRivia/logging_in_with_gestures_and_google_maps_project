package pl.lipov.laborki.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import pl.lipov.laborki.R

enum class FacilityType(
    @StringRes val nameResId: Int,
    @DrawableRes val iconResId: Int,
    var level: Int,
    var latLng: LatLng? = null,
    var isActive: Boolean = false,
    var inserted: Boolean = false
) {
    MARKET(R.string.iconMarket, R.drawable.market_icon, 0, isActive = true),
    RESTAURANT(R.string.iconRestaurant, R.drawable.restaurant_icon, 0),
    BANK(R.string.iconBank, R.drawable.bank_icon, 0)
}