package com.example.nearbyrestaurants.screens.placesListScreen.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nearbyrestaurants.R
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.databinding.ItemViewPlacesListBinding
import com.google.gson.Gson

class PlacesListAdapter(
    private var context: Context?,
    private var places: MutableList<com.example.nearbyrestaurants.commonUtils.commonModels.Result>,
    private val listener: OnOptionClickListener
) :
    RecyclerView.Adapter<PlacesListAdapter.ViewHolder>() {

    fun setPlacesList(places: MutableList<Result>) {

        this.places = places

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemViewPlacesListBinding: ItemViewPlacesListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_view_places_list, parent, false
            )
        return ViewHolder(itemViewPlacesListBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        context?.let {

            if(place.categories.isNotEmpty()) {
                Glide.with(it)
                    .load(place.categories[0].icon.getIcon())
                    .error(R.drawable.no_data_placeholder)
                    .placeholder(R.drawable.no_data_placeholder)
                    .into(holder.binding.ivCategoryIcon)
            }

            holder.binding.tvName.setText(place.name)
        }

        holder.binding.rlMainContainer.setOnClickListener { listener.onPlaceItemClick(place) }
    }

    override fun getItemCount(): Int {
        Log.e("gggggggg" , "=33333="+ places.size)
        return places.size
    }

    interface OnOptionClickListener {
        fun onPlaceItemClick(result: Result)
    }

    inner class ViewHolder(itemView: ItemViewPlacesListBinding) :
        RecyclerView.ViewHolder(itemView.getRoot()) {
        var binding: ItemViewPlacesListBinding

        init {
            binding = itemView
        }
    }
}