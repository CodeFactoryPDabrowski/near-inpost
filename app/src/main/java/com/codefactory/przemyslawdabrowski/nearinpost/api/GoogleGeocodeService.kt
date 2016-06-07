package com.codefactory.przemyslawdabrowski.nearinpost.api

import com.codefactory.przemyslawdabrowski.nearinpost.api.response_converter.JsonAndXmlConverter
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.ReverseGeocodedAddresses
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Service to get geocoding data from google apis.
 */
interface GoogleGeocodeService {

    /**
     * Get reverse geo coding of location coordinates.
     * @param latlng Location coordinates.
     * @param key Key for google apis server.
     *
     * @return Observable with reverse geo coding results.
     */
    @GET("https://maps.googleapis.com/maps/api/geocode/json") @JsonAndXmlConverter.Json
    fun reverseGeocodeCoordinates(@Query("latlng") latlng: String
                                  , @Query("key") key: String): Observable<ReverseGeocodedAddresses>
}