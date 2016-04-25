package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.location.Address
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder

class LocationSearchHolder(itemView: View?, listener: LocationSearchHolderListener) : BaseHolder<Address>(itemView) {

    val title: TextView by bindView(R.id.customLocationViewHolderTitle)

    val subTitle: TextView by bindView(R.id.customLocationViewHolderSubTitle)

    val resultLayout: LinearLayout by bindView(R.id.customLocationViewHolderLayout)

    /**
     * Search result address associated with view holder.
     */
    lateinit var searchResult: Address

    /**
     * Listener to communicate with parent component.
     */
    lateinit var holderListener: LocationSearchHolderListener

    init {
        holderListener = listener
        resultLayout.setOnClickListener { view -> holderListener.onResultClick(PostalCodeUi(searchResult.postalCode)) }
    }

    override fun bindItem(it: Address) {
        searchResult = it
        title.text = it.createAddress()
        subTitle.text = it.postalCode
    }

    /**
     * Create address text extension, e.g Polska, Rutki, 13.
     */
    private fun Address.createAddress(): String {
        //Func to create text divided with ',' sign.
        fun createAddressText(list: List<String>): String {
            var addressText = ""
            for (i in 0..list.size - 1) {
                if (i == list.size - 1) {
                    addressText += list[i]
                } else {
                    addressText += list[i]
                    addressText += ", "
                }
            }

            return addressText
        }

        var addressPartsList = emptyList<String>()
        if (!this.countryName.isNullOrEmpty()) {
            addressPartsList += this.countryName
        }

        if (!this.locality.isNullOrEmpty()) {
            addressPartsList += this.locality
        }

        if (!this.subThoroughfare.isNullOrEmpty()) {
            addressPartsList += this.subThoroughfare
        }

        if (!this.thoroughfare.isNullOrEmpty()) {
            addressPartsList += this.thoroughfare
        }

        if (addressPartsList.isEmpty()) {
            return "Empty Address"//TODO: Return from resources
        } else {
            return createAddressText(addressPartsList)
        }

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