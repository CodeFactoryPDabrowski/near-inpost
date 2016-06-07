package com.codefactory.przemyslawdabrowski.nearinpost.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Response from google apis, response not full, only necessary parts.
 */
class ReverseGeocodedAddresses() {

    /**
     * Results of reverse geo coding addresses.
     */
    @SerializedName("results")
    @Expose
    var results: List<Result> = emptyList()

    /**
     * Status of response, OK if everything it's good.
     */
    @SerializedName("status")
    @Expose
    var status: String? = null
}

class Result() {

    /**
     * Address components like postal code, country, etc.
     */
    @SerializedName("address_components")
    @Expose
    var adressComponents: List<AddressComponent> = emptyList()

    /**
     * Formatted address, e.g Rutki 13, 18-430 Wizna, Polska.
     */
    @SerializedName("formatted_address")
    @Expose
    var formattedAddress: String? = null
}

class AddressComponent() {

    /**
     * Long name of component part, e.g Polska.
     */
    @SerializedName("long_name")
    @Expose
    var longName: String? = null

    /**
     * Short name of component part, e.g Pl.
     */
    @SerializedName("short_name")
    @Expose
    var shortName: String? = null

    /**
     * List of component types, e.g [country].
     */
    @SerializedName("types")
    @Expose
    var types: List<String> = emptyList()
}
