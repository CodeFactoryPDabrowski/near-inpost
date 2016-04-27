package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity

interface MainFragmentView {

    /**
     * Results of in post machines query.
     */
    fun onNearestInPostResult(machines: List<Machine>)

    /**
     * Error if any execute in search nearest machines query.
     */
    fun onNearestInPostError(error: Throwable?)

    /**
     * Get context of activity, needs for starting new activity.
     */
    fun getActivityContext(): BaseActivity
}