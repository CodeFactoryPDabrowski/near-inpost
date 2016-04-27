package com.codefactory.przemyslawdabrowski.nearinpost.navigation

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.AppScope
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity
import com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details.MachineDetailsActivity
import javax.inject.Inject

@AppScope
class Navigator @Inject constructor() {

    /**
     * Go MachineDetailsActivity.
     */
    fun navigateToMachineDetails(activity: BaseActivity) {
        MachineDetailsActivity.newInstance(activity)
    }
}
