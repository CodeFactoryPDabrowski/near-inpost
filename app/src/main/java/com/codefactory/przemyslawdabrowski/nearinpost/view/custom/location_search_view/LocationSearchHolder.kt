package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.SimpleLocationSearchResultUi
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder

class LocationSearchHolder(itemView: View?, listener: LocationSearchHolderListener) : BaseHolder<SimpleLocationSearchResultUi>(itemView) {

    val title: TextView by bindView(R.id.customLocationViewHolderTitle)

    val subTitle: TextView by bindView(R.id.customLocationViewHolderSubTitle)

    val resultLayout: LinearLayout by bindView(R.id.customLocationViewHolderLayout)

    /**
     * Search result address associated with view holder.
     */
    lateinit var searchResult: SimpleLocationSearchResultUi

    /**
     * Listener to communicate with parent component.
     */
    lateinit var holderListener: LocationSearchHolderListener

    init {
        holderListener = listener
        resultLayout.setOnClickListener { view -> holderListener.onResultClick(searchResult.postalCodeUi) }
    }

    override fun bindItem(it: SimpleLocationSearchResultUi) {
        searchResult = it
        title.text = it.formattedAddress
        subTitle.text = it.postalCodeUi.postalCode
    }

    /**
     * Interface to communicate with parent component.
     */
    interface LocationSearchHolderListener {

        /**
         * Action after click on view holder.
         * @param postalCodeUi Clicked address associated with holder.
         */
        fun onResultClick(postalCodeUi: PostalCodeUi)

    }
}