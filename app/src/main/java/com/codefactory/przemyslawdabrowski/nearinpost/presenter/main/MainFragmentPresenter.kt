package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.api.NearestMachinesService
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.navigation.Navigator
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.BasePresenter
import d
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
                    d { "some error ${error.message}" }
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
     */
    fun findCurrentLocation() {
        val subscription = reactiveLocationProvider.lastKnownLocation.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //TODO: Check for postal code -> request for maps api.
                    val location = it
                }, {
                    err ->

                })
        subscriptions!!.add(subscription)
    }
}