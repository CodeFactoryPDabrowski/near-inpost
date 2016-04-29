package com.codefactory.przemyslawdabrowski.nearinpost.presenter

/**
 * Base implementation for presenter
 */
abstract class BasePresenter<V : Any> : Presenter<V> {

    /**
     * View for presenters.
     */
    protected lateinit var view: V

    override fun bind(view: V) {
        this.view = view
    }
}