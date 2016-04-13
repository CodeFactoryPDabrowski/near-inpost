package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.api.NearestMachinesService
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.Presenter
import d
import retrofit2.Retrofit
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class MainFragmentPresenter @Inject constructor(retrofit: Retrofit) : Presenter<MainFragmentView> {

    /**
     * Service for getting nearest InPost machines.
     */
    lateinit var nearestMachineService: NearestMachinesService

    init {
        nearestMachineService = retrofit.create(NearestMachinesService::class.java)
    }

    /**
     * View to manipulate by presenter.
     */
    private lateinit var view: MainFragmentView

    /**
     * Subscription of retrofit observable.
     */
    private var subscription: Subscription? = null

    override fun bind(view: MainFragmentView) {
        this.view = view
    }

    /**
     * Search nearest in post machines.
     * @param postCode Post code of searching location.
     * @param limit Limit of searching machines.
     */
    fun searchForNearestInPost(postCode: String, limit: Int = 3) {
        //TODO: Check if valid post code. Do some cache after rotation mechanism.
        if (postCode.length == 0) {
            return
        }
        subscription = nearestMachineService.findNearestMachines(postCode, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    paczkomaty ->
                    paczkomaty.machine?.let { view.onNearestInPostResult(paczkomaty.machine  as List<Machine>) }
                            ?: view.onNearestInPostResult(emptyList())
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
}