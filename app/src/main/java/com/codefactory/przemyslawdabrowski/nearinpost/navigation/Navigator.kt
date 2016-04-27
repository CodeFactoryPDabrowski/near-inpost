package com.codefactory.przemyslawdabrowski.nearinpost.navigation

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity
import com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details.MachineDetailsActivity
import javax.inject.Inject

@AppScope
class Navigator @Inject constructor() {

    /**
     * Go MachineDetailsActivity.
     *
     * @param activity Activity to start new activity.
     * @param machineUi Machine InPost details.
     */
    fun navigateToMachineDetails(activity: BaseActivity, machineUi: MachineUi) {
        MachineDetailsActivity.newInstance(activity, machineUi)
    }
}
