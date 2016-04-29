package com.codefactory.przemyslawdabrowski.nearinpost.presenter.machine_details

import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi

interface MachineDetailsFragmentView {

    /**
     * Show details of in post machine.
     */
    fun showDetails(machineDetails: MachineUi)
}