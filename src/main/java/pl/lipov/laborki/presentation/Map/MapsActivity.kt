package pl.lipov.laborki.presentation.Map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_maps.*
import pl.lipov.laborki.R
import pl.lipov.laborki.data.model.FacilityType
import pl.lipov.laborki.presentation.BuildingsMenuFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lipov.laborki.data.MapRepository

class MapsActivity : Fragment(), OnMapReadyCallback{

    private  val viewModel:  MapsViewModel by viewModel()
    private lateinit var mMap: GoogleMap



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.supportFragmentManager.let {
            BuildingsMenuFragment().show(it!!,
                BuildingsMenuFragment.TAG
            )
        }
/*
        viewModel.mapView = (childFragmentManager.findFragmentById(R.id.map_google) as SupportMapFragment?)?.apply {
            this.onViewCreated(view, savedInstanceState)
            getMapAsync(this@MapsActivity)
        }*/

        map_google.onCreate(savedInstanceState)
        map_google.onResume()
        map_google.getMapAsync(this)

        switchButtons()
        observeButtonsStatus()
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            mMap=p0
            provideRemoveButton()
            viewModel.prepareMap(p0, MapRepository().buildingList[0])
        }

    }

    @SuppressLint("RestrictedApi")
    fun provideRemoveButton(){
        mMap.setOnMarkerClickListener { marker ->
            floating_remove.visibility = View.VISIBLE
            marker.showInfoWindow()
            floating_remove.setOnClickListener {
                viewModel.deleteMarker(marker.tag as FacilityType)
                floating_remove.visibility = View.INVISIBLE
            }
            true
        }
    }



    private fun switchButtons(){
        changeColorOfButtons(floating_market)

        floating_bank.setOnClickListener{
            viewModel.fabChanged(FacilityType.BANK)
        }
        floating_market.setOnClickListener{
            viewModel.fabChanged(FacilityType.MARKET)
        }
        floating_restaurant.setOnClickListener{
            viewModel.fabChanged(FacilityType.RESTAURANT)
        }

        floating_building_list.setOnClickListener {
            activity?.supportFragmentManager.let {
                BuildingsMenuFragment().show(it!!,
                    BuildingsMenuFragment.TAG
                )
            }
        }
    }

    fun changeColorOfButtons(view: FloatingActionButton){
        val buttons =
            mutableListOf<FloatingActionButton>(floating_market,floating_bank,floating_restaurant)

        view.setColorFilter(resources.getColor(R.color.error))
        buttons.removeAll {
            it==view }.apply { buttons.forEach {
            it.setColorFilter(resources.getColor(R.color.wave))
        } }
    }

    fun observeButtonsStatus(){
        viewModel.run {
            buttons.observe(::getLifecycle){
                if (buttons.value == FacilityType.RESTAURANT){
                    changeColorOfButtons(floating_restaurant)
                }
                if(buttons.value == FacilityType.BANK){
                    changeColorOfButtons(floating_bank)
                }
                if(buttons.value == FacilityType.MARKET){
                    changeColorOfButtons(floating_market)
                }
            }
        }
    }
}



