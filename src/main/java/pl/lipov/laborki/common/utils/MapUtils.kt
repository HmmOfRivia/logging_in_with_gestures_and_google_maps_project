package pl.lipov.laborki.common.utils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import pl.lipov.laborki.data.MapRepository
import pl.lipov.laborki.data.Building
import pl.lipov.laborki.data.model.FacilityType



class MapUtils(
    var context: Context,
    var mapRepository: MapRepository
): GoogleMap.OnMapLongClickListener, GoogleMap.OnIndoorStateChangeListener {

    private var buildingHasFocus: Boolean = false
    lateinit var  googleMap: GoogleMap
    var actualLevel: Int = 0
    private var markerArray = mutableListOf<Marker>()
    val onEvent = MutableLiveData<FacilityType>()


    fun fabChanged(facilityType: FacilityType){
        mapRepository.facilityTypes.postValue(arrayOf(facilityType.apply {
                isActive = true
            }))
        onEvent.postValue(facilityType)

    }

    fun prepareMap(
        googleMap: GoogleMap,
        building: Building
    ){
        googleMap.apply {
            setOnIndoorStateChangeListener(this@MapUtils)
            setOnMapLongClickListener(this@MapUtils)
        }
        this.googleMap = googleMap
        changeCamera(building.coord)
    }

    fun deleteMarker(
    facilityType: FacilityType
    ){
        val markerArrayCopy = mutableListOf<Marker>().apply { addAll(markerArray) }
        markerArray.removeAll {
            it.tag == facilityType
        }
        markerArrayCopy.findLast {
            it.tag == facilityType
        }?.remove()
    }

    fun changeCamera(
        coord: LatLng
    ){
        val camera = CameraPosition.Builder()
            .target(coord)
            .zoom(18F)
            .build()
        googleMap.apply {
            this.animateCamera(CameraUpdateFactory.newCameraPosition(camera))
        }
    }


    override fun onIndoorBuildingFocused() {
        buildingHasFocus = !buildingHasFocus
    }

    override fun onIndoorLevelActivated(
        indoorBuilding:IndoorBuilding
    ){
        actualLevel = indoorBuilding.levels[indoorBuilding.activeLevelIndex].name.toInt()
        markerArray.forEach {
            it.isVisible = (it.tag as FacilityType).level == actualLevel
        }
    }

    override fun onMapLongClick(
        latLng: LatLng
    ){
        if (buildingHasFocus){
            mapRepository.facilityTypes.value?.findLast { it.isActive }?.apply{
                if(markerArray.findLast { (it.tag as FacilityType).name == name} == null){
                    this.latLng= latLng
                    level = actualLevel
                    inserted = true
                    addMarker(this)
                    mapRepository.facilityTypes.let {
                        it.postValue(it.value)
                    }
                }
            }
        }
    }

    private fun addMarker(
        facilityType: FacilityType
    ){
        facilityType.latLng?.let { latLng ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(context.getString(facilityType.nameResId))
                    .icon(BitmapDescriptorFactory.fromResource(facilityType.iconResId))
                    .draggable(true)
            )
            marker.tag = facilityType
            markerArray.add(marker)
        }
    }
}


