package com.codefactory.przemyslawdabrowski.nearinpost.presenter.machine_details

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.BasePresenter
import javax.inject.Inject

@FragmentScope
class MachineDetailsFragmentPresenter @Inject constructor() : BasePresenter<MachineDetailsFragmentView>() {

    /**
     * Details of displayed machine.
     */
    lateinit var machineDetails: MachineUi

    /**
     * Set details of machine.
     * @param machineUi InPost machine details.
     */
    fun setDetails(machineUi: MachineUi) {
        machineDetails = machineUi
        view.showDetails(machineDetails)
    }

}