package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.location.Address
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R

class LocationSearchAdapter : RecyclerView.Adapter<LocationSearchHolder>() {

    /**
     * List of search location results.
     */
    var searchResultList = emptyList<Address>()

    override fun onBindViewHolder(holder: LocationSearchHolder?, position: Int) {
        holder?.bindItem(searchResultList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationSearchHolder? {
        return LocationSearchHolder(LayoutInflater.from(parent?.context).inflate(R.layout.custom_location_search_holder, null))
    }

    override fun getItemCount(): Int = searchResultList.size

    /**
     * Refresh adapter items.
     */
    fun addSearchResult(searchResult: List<Address>) {
        searchResultList = emptyList()
        searchResultList += searchResult
        notifyDataSetChanged()
    }
}