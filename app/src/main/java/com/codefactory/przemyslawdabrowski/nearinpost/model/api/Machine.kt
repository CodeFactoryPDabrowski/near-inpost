package com.codefactory.przemyslawdabrowski.nearinpost.model.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "machine", strict = false)
class Machine() {

    /**
     * Name of inPost machine, e.g. WAW42A.
     */
    @field:Element(name = "name")
    var name: String? = null

    /**
     * Postcode of inPost machine location, e.g. 02-495.
     */
    @field:Element(name = "postcode")
    var postcode: String? = null

    /**
     * Street of inPost machine location, e.g Sosnowskiego.
     */
    @field:Element(name = "street")
    var street: String? = null

    /**
     * Number of building of inPost machine location, e.g 1C.
     */
    @field:Element(name = "buildingnumber", required = false)
    var buildingnumber: String? = null

    /**
     * City of inPost machine location, e.g Warszawa.
     */
    @field:Element(name = "town")
    var town: String? = null

    /**
     * Latitude of inPost machine location, e.g 52.18346.
     */
    @field:Element(name = "latitude")
    var latitude: Float? = null

    /**
     * Longitude of inPost machine location, e.g 20.89266.
     */
    @field:Element(name = "longitude")
    var longitude: Float? = null

    /**
     * Distance inPost machine from passed in request postcode, e.g. 0.3.
     */
    @field:Element(name = "distance")
    var distance: Double? = null

    /**
     * Type of payment, e.g 1.
     */
    @field:Element(name = "paymenttype")
    var paymenttype: Int? = null

    /**
     * Description of inPost location, e.g. Przy budynku jakimśtam.
     */
    @field:Element(name = "locationdescription")
    var locationdescription: String? = null
}