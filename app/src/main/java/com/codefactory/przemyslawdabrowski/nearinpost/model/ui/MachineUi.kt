package com.codefactory.przemyslawdabrowski.nearinpost.model.ui

import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine

data class MachineUi(val name: String?
                     , val postcode: String?
                     , val street: String?
                     , val buildingNumber: String?
                     , val town: String?
                     , val latitude: Float?
                     , val longitude: Float?
                     , val distance: Double?
                     , val locationDescription: String?) {
    /**
     * Secondary constructor.
     * @param machine Machine to create MachineUi object.
     */
    constructor(machine: Machine) : this(machine.name
            , machine.postcode
            , machine.street
            , machine.buildingnumber
            , machine.town
            , machine.latitude
            , machine.longitude
            , machine.distance
            , machine.locationdescription)
}