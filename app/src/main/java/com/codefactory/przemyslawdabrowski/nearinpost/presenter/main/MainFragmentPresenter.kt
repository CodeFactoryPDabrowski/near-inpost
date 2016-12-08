package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.BuildConfig
import com.codefactory.przemyslawdabrowski.nearinpost.api.GoogleGeocodeService
import com.codefactory.przemyslawdabrowski.nearinpost.api.NearestMachinesService
import com.codefactory.przemyslawdabrowski.nearinpost.controller.PreferenceController
import com.codefactory.przemyslawdabrowski.nearinpost.data.MachineRepository
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.AddressComponentType
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.ReverseGeocodedAddressStatusType
import com.codefactory.przemyslawdabrowski.nearinpost.model.data.MachineDb
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.navigation.Navigator
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.BasePresenter
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import retrofit2.Retrofit
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

@FragmentScope
class MainFragmentPresenter @Inject constructor(retrofit: Retrofit
                                                , val navigator: Navigator
                                                , val reactiveLocationProvider: ReactiveLocationProvider,
                                                val preferenceController: PreferenceController,
                                                val machineRepository: MachineRepository) : BasePresenter<MainFragmentView>() {

    /**
     * Service for getting nearest InPost machines.
     */
    var nearestMachineService: NearestMachinesService

    /**
     * Service for getting geocoding data from google apis.
     */
    var googleGeocodeService: GoogleGeocodeService

    /**
     * TODO: create common object for list of machines and postal code UI
     * List of cached last near in post machines response.
     */
    var nearInPostMachinesCache: List<Machine>? = null

    /**
     * Cached searched last postal code.
     */
    var postalCodeUiCache: PostalCodeUi? = null

    init {
        nearestMachineService = retrofit.create(NearestMachinesService::class.java)
        googleGeocodeService = retrofit.create(GoogleGeocodeService::class.java)
    }

    /**
     * Search nearest in post machines.
     * @param postalCodeUi Postal code object contains postal code of searching location.
     */
    fun searchForNearestInPost(postalCodeUi: PostalCodeUi) {
        //TODO: Check if valid post code. Do some cache after rotation mechanism.
        val postalCode = postalCodeUi.postalCode
        if (postalCode.isEmpty()) {
            view.onEmptyPostalCode()
            return
        }
        val subscription = nearestMachineService.findNearestMachines(postalCode, preferenceController.maxInPostMachinesResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    paczkomaty ->
                    postalCodeUiCache = postalCodeUi
                    if (paczkomaty.machine != null) {
                        nearInPostMachinesCache = paczkomaty.machine
                        saveLastMachineResult(postalCodeUiCache!!, nearInPostMachinesCache!!)
                        view.onNearestInPostResult(postalCodeUi, paczkomaty.machine!!)
                    } else {
                        nearInPostMachinesCache = emptyList()
                        view.onNearestInPostResult(postalCodeUi, emptyList())
                    }
                }, {
                    error ->
                    view.onNearestInPostError(error)
                })
        subscriptions!!.add(subscription)
    }

    /**
     * Create view callback of host fragment.
     */
    fun onCreateView() {
        subscribeRx()
        if (nearInPostMachinesCache != null && postalCodeUiCache != null) {
            view.onNearestInPostResult(postalCodeUiCache as PostalCodeUi, nearInPostMachinesCache as List<Machine>)
        } else {
            val subscription = machineRepository.getMachines().
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe({
                        val lastSearched = it.map {
                            fromDb(it)
                        }
                        if (lastSearched.isEmpty()) {
                            view.onFreshStart()
                        } else {
                            view.onNearestInPostResult(PostalCodeUi(it[0].searchPostalCode), lastSearched)
                        }
                    }, {
                        view.onFreshStart()
                    })
            subscriptions!!.add(subscription)
        }
    }

    /**
     * Destroy view callback of host fragment.
     */
    fun onDestroyView() {
        unsubscribeRx()
    }

    /**
     * Show details of in post machine.
     * @param machineUi InPost machine UI details.
     * @param postalCodeUi Postal code.
     */
    fun showDetails(machineUi: MachineUi, postalCodeUi: PostalCodeUi) {
        navigator.navigateToMachineDetails(view.getActivityContext(), machineUi, postalCodeUi)
    }

    /**
     * Find nearest in post machines for current location.
     */
    fun findCurrentLocation() {
        val subscription = reactiveLocationProvider.lastKnownLocation.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap { it ->
                    googleGeocodeService.reverseGeocodeCoordinates("${it.latitude},${it.longitude}"
                            , BuildConfig.GOOGLE_GEOCODING_API_KEY)
                }
                .filter {
                    it.status == ReverseGeocodedAddressStatusType.OK.responseType && it.results.isNotEmpty()
                }
                .map { it ->
                    return@map it.results
                            .flatMap { it.addressComponents }
                            .firstOrNull { it.types.size == 1 && it.types[0] == AddressComponentType.POSTAL_CODE.componentType }
                            ?.longName
                }
                .filter { it != null }
                .flatMap {
                    val postalCode = it!!
                    nearestMachineService.findNearestMachines(postalCode, preferenceController.maxInPostMachinesResult)
                            .map { Pair(PostalCodeUi(postalCode), it) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    postalCodeUiCache = it.first
                    if (it.second.machine != null) {
                        nearInPostMachinesCache = it.second.machine
                        saveLastMachineResult(postalCodeUiCache!!, nearInPostMachinesCache!!)
                        view.onNearestInPostResult(postalCodeUiCache!!, nearInPostMachinesCache!!)
                    } else {
                        nearInPostMachinesCache = emptyList()
                        view.onNearestInPostResult(postalCodeUiCache!!, emptyList())
                    }
                }, {
                    err ->
                    view.onFindCurrentLocationError(err)
                }, {
                    view.onFindCurrentLocationCompleted()
                })
        subscriptions!!.add(subscription)
    }

    /**
     * Use in functions that searching for nearest inPost machines.
     */
    private fun saveLastMachineResult(postalCodeUi: PostalCodeUi, machines: List<Machine>) {
        machineRepository.saveMachines(postalCodeUi, machines)
    }

    /**
     * todo: should be in other place.
     */
    private fun fromDb(machineDb: MachineDb): Machine {
        val machine = Machine()
        machine.name = machineDb.name
        machine.postcode = machineDb.postcode
        machine.street = machineDb.street
        machine.buildingnumber = machineDb.buildingNumber
        machine.town = machineDb.town
        machine.latitude = machineDb.latitude
        machine.longitude = machineDb.longitude
        machine.distance = machineDb.distance
        machine.locationdescription = machineDb.locationDescription

        return machine
    }
}
