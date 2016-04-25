package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.location.Address
import android.view.View
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder

//TODO: Implement logic.
class LocationSearchHolder(itemView: View?) : BaseHolder<Address>(itemView) {

    val title: TextView by bindView(R.id.customLocationViewHolderTitle)

    val subTitle: TextView by bindView(R.id.customLocationViewHolderSubTitle)

    override fun bindItem(it: Address) {
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
}