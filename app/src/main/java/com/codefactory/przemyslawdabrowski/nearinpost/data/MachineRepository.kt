package com.codefactory.przemyslawdabrowski.nearinpost.data

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.model.data.MachineDb
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import io.realm.Realm
import io.realm.RealmResults
import rx.Observable
import javax.inject.Inject

@AppScope
class MachineRepository @Inject constructor(val realm: Realm) {

    /**
     * Save machine Json response in database. Remove previous entries first.
     */
    fun saveMachines(postalCodeUi: PostalCodeUi, machinesUI: List<Machine>) {
        realm.executeTransaction {
            realm.delete(MachineDb::class.java)
            for (machine in machinesUI) {
                val machineDb = realm.createObject(MachineDb::class.java)
                machineDb.fromUI(postalCodeUi, machine)
            }
        }
    }

    /**
     * Get all json machines from database. Get only once and don't observe changes on query.
     */
    fun getMachines(): Observable<RealmResults<MachineDb>> = realm.
            where(MachineDb::class.java).
            findAllAsync().
            asObservable().
            filter { it.isLoaded }.
            first()


    /**
     * Extension to create machineDb from {@link model.api.Machine}.
     */
    fun MachineDb.fromUI(postalCodeUi: PostalCodeUi, machineAPI: Machine) {
        name = machineAPI.name!!
        postcode = machineAPI.postcode
        street = machineAPI.street
        buildingNumber = machineAPI.buildingnumber
        town = machineAPI.town
        latitude = machineAPI.latitude
        longitude = machineAPI.longitude
        distance = machineAPI.distance
        locationDescription = machineAPI.locationdescription
        searchPostalCode = postalCodeUi.postalCode
    }
}
