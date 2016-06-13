package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.BuildConfig
import com.codefactory.przemyslawdabrowski.nearinpost.api.GoogleGeocodeService
import com.codefactory.przemyslawdabrowski.nearinpost.api.NearestMachinesService
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.AddressComponentType
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.ReverseGeocodedAddressStatusType
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
                                                , val reactiveLocationProvider: ReactiveLocationProvider) : BasePresenter<MainFragmentView>() {

    /**
     * Service for getting nearest InPost machines.
     */
    lateinit var nearestMachineService: NearestMachinesService

    /**
     * Service for getting geocoding data from google apis.
     */
    lateinit var googleGeocodeService: GoogleGeocodeService

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
     * @param limit Limit of searching machines.
     */
    fun searchForNearestInPost(postalCodeUi: PostalCodeUi, limit: Int = 5) {
        //TODO: Check if valid post code. Do some cache after rotation mechanism.
        val postalCode = postalCodeUi.postalCode
        if (postalCode.length == 0) {
            return
        }
        val subscription = nearestMachineService.findNearestMachines(postalCode, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    paczkomaty ->
                    postalCodeUiCache = postalCodeUi
                    if (paczkomaty.machine != null) {
                        nearInPostMachinesCache = paczkomaty.machine
                        view.onNearestInPostResult(postalCodeUi, paczkomaty.machine  as List<Machine>)
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
     * @param limit Limit of nearest in post machine results.
     */
    fun findCurrentLocation(limit: Int = 5) {
        val subscription = reactiveLocationProvider.lastKnownLocation.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap { it ->
                    googleGeocodeService.reverseGeocodeCoordinates("${it.latitude},${it.longitude}"
                            , BuildConfig.GOOGLE_GEOCODING_API_KEY)
                }
                .filter {
                    it.status == ReverseGeocodedAddressStatusType.OK.responseType && it.results.size > 0
                }
                .map { it ->
                    for (result in it.results) {
                        for (addressComponent in result.addressComponents) {
                            if (addressComponent.types.size == 1
                                    && addressComponent.types[0].equals(AddressComponentType.POSTAL_CODE.componentType)) {
                                return@map addressComponent.longName
                            }
                        }
                    }
                    return@map null
                }
                .filter { it != null }
                .flatMap {
                    val postalCode = it!!
                    nearestMachineService.findNearestMachines(postalCode, limit)
                            .map { Pair(PostalCodeUi(postalCode), it) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    postalCodeUiCache = it.first
                    if (it.second.machine != null) {
                        nearInPostMachinesCache = it.second.machine
                        view.onNearestInPostResult(postalCodeUiCache as PostalCodeUi, nearInPostMachinesCache  as List<Machine>)
                    } else {
                        nearInPostMachinesCache = emptyList()
                        view.onNearestInPostResult(postalCodeUiCache as PostalCodeUi, emptyList())
                    }
                }, {
                    err ->
                    view.onFindCurrentLocationError(err)
                }, {
                    view.onFindCurrentLocationCompleted()
                })
        subscriptions!!.add(subscription)
    }
}