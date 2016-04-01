package com.codefactory.przemyslawdabrowski.nearinpost.presenter.main

import com.codefactory.przemyslawdabrowski.nearinpost.presenter.Presenter
import javax.inject.Inject

class MainFragmentPresenter @Inject constructor() : Presenter<MainFragmentView> {

    /**
     * View to manipulate by presenter.
     */
    private lateinit var view: MainFragmentView

    override fun bind(view: MainFragmentView) {
        this.view = view
    }
}