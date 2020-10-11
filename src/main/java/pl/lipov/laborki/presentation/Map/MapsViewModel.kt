package pl.lipov.laborki.presentation.Map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import pl.lipov.laborki.common.utils.MapUtils
import pl.lipov.laborki.data.MapRepository
import pl.lipov.laborki.data.Building
import pl.lipov.laborki.data.model.FacilityType

class MapsViewModel(
    private val mapUtils: MapUtils,
    private val mapRepository: MapRepository

): ViewModel() {
    //var mapView: SupportMapFragment? = null
    val buttons: MutableLiveData<FacilityType> = mapUtils.onEvent

    fun changeCamera(coord:LatLng){
        mapUtils.changeCamera(coord)
    }

    fun prepareMap(googleMap: GoogleMap, building: Building){
        mapUtils.prepareMap(googleMap, building)
    }

    fun fabChanged(facilityType: FacilityType){
        mapUtils.fabChanged(facilityType)
    }

    fun deleteMarker(facilityType: FacilityType){
        mapUtils.deleteMarker(facilityType)
    }


//    private fun GoogleMap.addHeatMapTileOverlay() {
//        var list: List<LatLng?>? = null
//        try {
//            list = readItems()
//        } catch (e: JSONException) {
//            Log.e(TAG, e.localizedMessage, e)
//        }
//        val mProvider = HeatmapTileProvider
//            .Builder()
//            .radius(50)
//            .data(list)
//            .build()
//        addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
//    }
//
//    @Throws(JSONException::class)
//    private fun readItems(resource: Int = R.raw.police_stations): ArrayList<LatLng?>? {
//        val list = ArrayList<LatLng?>()
//        val inputStream: InputStream = context.resources.openRawResource(resource)
//        val json: String = Scanner(inputStream).useDelimiter("\\A").next()
//        val array = JSONArray(json)
//        for (i in 0 until array.length()) {
//            val `object` = array.getJSONObject(i)
//            val lat = `object`.getDouble("lat")
//            val lng = `object`.getDouble("lng")
//            list.add(LatLng(lat, lng))
//        }
//        return list
//    }
}