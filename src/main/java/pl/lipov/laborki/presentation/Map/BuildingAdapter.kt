package pl.lipov.laborki.presentation.Map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.building_component_layout.view.*
import pl.lipov.laborki.R
import pl.lipov.laborki.data.Building

class BuildingAdapter(
    var buildings: Array<Building>,
    var callback: (coordClicked: LatLng) -> Unit
) : RecyclerView.Adapter<BuildingViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder = BuildingViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.building_component_layout, parent, false)
    )

    override fun getItemCount(): Int = buildings.size

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val photo = holder.view.icon_image_view
        val gallery = holder.view.name_text_view

        gallery.text = buildings[position].name
        photo.setImageResource(buildings[position].iconResId)

        holder.view.setOnClickListener {
            callback.invoke(buildings[position].coord)
        }
    }

}class BuildingViewHolder(val view: View) : RecyclerView.ViewHolder(view)