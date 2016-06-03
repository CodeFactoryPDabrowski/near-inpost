package com.codefactory.przemyslawdabrowski.nearinpost.presenter.machine_details

import android.os.Bundle
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.BasePresenter
import com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details.MachineDetailsFragment
import javax.inject.Inject

@FragmentScope
class MachineDetailsFragmentPresenter @Inject constructor() : BasePresenter<MachineDetailsFragmentView>() {

    /**
     * Set details of machine.
     * @param arguments Bundle that should contain inPost machine details.
     */
    fun setDetails(arguments: Bundle) {
        val machineDetails: MachineUi? = arguments.getParcelable<MachineUi>(MachineDetailsFragment.MACHINE_DETAILS_KEY) ?:
                throw IllegalArgumentException("Machine details cannot be null")
        //TODO: Check .getString method for api 17
        val postalCode = arguments.getString(MachineDetailsFragment.POSTAL_CODE_KEY) ?:
                throw IllegalArgumentException("Postal code cannot be null")

        view.showDetails(postalCode, machineDetails!!)
    }

}