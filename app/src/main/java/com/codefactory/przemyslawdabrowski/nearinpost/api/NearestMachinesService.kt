package com.codefactory.przemyslawdabrowski.nearinpost.api

import com.codefactory.przemyslawdabrowski.nearinpost.api.response_converter.JsonAndXmlConverter
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Paczkomaty
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface NearestMachinesService {

    /**
     * Get nearest inPost machines.
     * @param postCode Post code for search query, find nearest this post code machines.
     * @param limit Limit of result query.
     * @param paymentAvailable Value determinate if search for machines handling payment. Handle values: t/f.
     */
    @GET("/?do=findnearestmachines") @JsonAndXmlConverter.Xml
    fun findNearestMachines(@Query("postcode") postCode: String
                            , @Query("limit") limit: Int
                            , @Query("paymentavailable") paymentAvailable: String = "t"): Observable<Paczkomaty>
}