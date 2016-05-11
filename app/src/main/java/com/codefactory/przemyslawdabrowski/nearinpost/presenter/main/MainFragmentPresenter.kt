package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.api.NearestMachinesService
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.navigation.Navigator
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.BasePresenter
import d
import retrofit2.Retrofit
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

@FragmentScope
class MainFragmentPresenter @Inject constructor(retrofit: Retrofit, val navigator: Navigator) : BasePresenter<MainFragmentView>() {

    /**
     * Service for getting nearest InPost machines.
     */
    lateinit var nearestMachineService: NearestMachinesService

    init {
        nearestMachineService = retrofit.create(NearestMachinesService::class.java)
    }

    /**
     * Subscription of retrofit observable.
     */
    private var subscription: Subscription? = null

    /**
     * Search nearest in post machines.
     * @param postalCodeUi Postal code object contains postal code of searching location.
     * @param limit Limit of searching machines.
     */
    fun searchForNearestInPost(postalCodeUi: PostalCodeUi, limit: Int = 5) {
        //TODO: Check if valid post code. Do some cache after rotation mechanism.
        var postalCode = postalCodeUi.postalCode
        if (postalCode.length == 0) {
            return
        }
        subscription = nearestMachineService.findNearestMachines(postalCode, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    paczkomaty ->
                    paczkomaty.machine?.let { view.onNearestInPostResult(postalCodeUi, paczkomaty.machine  as List<Machine>) }
                            ?: view.onNearestInPostResult(postalCodeUi, emptyList())
                }, {
                    error ->
                    d { "some error ${error.message}" }
                    view.onNearestInPostError(error)
                })
    }

    /**
     * Destroy view callback of host fragment.
     */
    fun onDestroyView() {
        subscription?.let { (subscription as Subscription).unsubscribe() }
    }

    /**
     * Show details of in post machine.
     * @param machineUi InPost machine UI details.
     */
    fun showDetails(machineUi: MachineUi) {
        navigator.navigateToMachineDetails(view.getActivityContext(), machineUi)
    }
}