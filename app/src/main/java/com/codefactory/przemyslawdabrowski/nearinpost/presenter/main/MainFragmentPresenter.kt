package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.api.NearestMachinesService
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.Presenter
import d
import retrofit2.Retrofit
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

    override fun bind(view: MainFragmentView) {
        this.view = view
    }

    /**
     * TODO: Proper implement. Method wrong implemented!!!! change it.
     */
    fun create() {
        nearestMachineService.findNearestMachines("02-22222497ssss", 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    paczkomatyy ->
                    var machiny = paczkomatyy
                    var yolo = ""

                }, {
                    error ->
                    d { "some error ${error.message}" }
                })
    }
}