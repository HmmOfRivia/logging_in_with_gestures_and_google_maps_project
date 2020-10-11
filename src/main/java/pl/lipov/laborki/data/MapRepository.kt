package pl.lipov.laborki.data

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import pl.lipov.laborki.R
import pl.lipov.laborki.data.model.FacilityType

class MapRepository(
    //private val mapApi: MapApi
){
     val facilityTypes = MutableLiveData(arrayOf(FacilityType.MARKET, FacilityType.RESTAURANT, FacilityType.BANK))
     var buildingList: Array<Building> = arrayOf(
          Building(
               R.drawable.gal_wil,
               "Galeria Wilenska",
               LatLng(52.2550, 21.0378)
          ),
          Building(
               R.drawable.gal_mok,
               "Galeria Mokotow",
               LatLng(52.1797645, 21.0035337)
          ),
          Building(
               R.drawable.arkadia,
               "Westfield Arkadia",
               LatLng(52.2571543, 20.9823334)
          )
     )
}