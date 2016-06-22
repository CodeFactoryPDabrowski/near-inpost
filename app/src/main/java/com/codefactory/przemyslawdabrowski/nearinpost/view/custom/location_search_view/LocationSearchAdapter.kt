package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.SimpleLocationSearchResultUi

class LocationSearchAdapter(listener: LocationSearchHolder.LocationSearchHolderListener) : RecyclerView.Adapter<LocationSearchHolder>() {

    /**
     * List of search location results.
     */
    var searchResultList = emptyList<SimpleLocationSearchResultUi>()

    /**
     * Listener to communicate with parent component. Holder with parent element.
     */
    lateinit var holderListener: LocationSearchHolder.LocationSearchHolderListener

    init {
        holderListener = listener
    }

    override fun onBindViewHolder(holder: LocationSearchHolder?, position: Int) {
        holder?.bindItem(searchResultList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationSearchHolder? {
        return LocationSearchHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.custom_location_search_holder, parent, false), holderListener)
    }

    override fun getItemCount(): Int = searchResultList.size

    /**
     * Refresh adapter items.
     */
    fun addSearchResult(searchResult: List<SimpleLocationSearchResultUi>) {
        searchResultList = emptyList()
        searchResultList += searchResult
        notifyDataSetChanged()
    }
}