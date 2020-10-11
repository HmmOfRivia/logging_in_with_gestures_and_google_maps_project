package pl.lipov.laborki.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.buildings_menu.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lipov.laborki.R
import pl.lipov.laborki.data.MapRepository
import pl.lipov.laborki.data.model.FacilityType
import pl.lipov.laborki.presentation.Map.BuildingAdapter
import pl.lipov.laborki.presentation.Map.MapsViewModel

class BuildingsMenuFragment() : BottomSheetDialogFragment() {

    private  val viewModel: MapsViewModel by viewModel()


    companion object{
        val TAG = BuildingsMenuFragment::class.java.simpleName
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflate = inflater.inflate(R.layout.buildings_menu, container, false)
        inflate.findViewById<RecyclerView>(R.id.my_recycler_view).apply{
            layoutManager = LinearLayoutManager(Activity())
            adapter = BuildingAdapter(MapRepository().buildingList) {coordClicked:LatLng -> changeMarket(coordClicked)}
        }
        return inflate
    }

    fun changeMarket(coord:LatLng){
        viewModel.changeCamera(coord)
        viewModel.fabChanged(FacilityType.MARKET)

    }
}