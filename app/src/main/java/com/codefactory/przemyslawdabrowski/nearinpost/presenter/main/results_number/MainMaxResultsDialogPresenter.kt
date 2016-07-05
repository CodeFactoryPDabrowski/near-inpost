package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.results_number

import com.codefactory.przemyslawdabrowski.nearinpost.controller.PreferenceController
import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.BasePresenter
import javax.inject.Inject

@FragmentScope
class MainMaxResultsDialogPresenter @Inject constructor(val preferenceController: PreferenceController)
: BasePresenter<MainMaxResultsDialogView>() {

    /**
     * Lifecycle method wrapper.
     */
    fun onCreateDialog() {
        view.checkMaxResults(preferenceController.maxInPostMachinesResult)
    }

    /**
     * Save max number of searching inPost machines.
     */
    fun saveMaxResultsNumber(maxResults: Int?) {
        maxResults?.let { preferenceController.maxInPostMachinesResult = maxResults }
    }
}