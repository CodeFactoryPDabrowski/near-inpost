package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine

interface MainFragmentView {

    /**
     * Results of in post machines query.
     */
    fun onNearestInPostResult(machines: List<Machine>)

    /**
     * Error if any execute in search nearest machines query.
     */
    fun onNearestInPostError(error: Throwable?)
}