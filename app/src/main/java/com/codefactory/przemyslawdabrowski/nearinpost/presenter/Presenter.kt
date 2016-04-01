package com.codefactory.przemyslawdabrowski.nearinpost.presenter

/**
 * Base presenter to implement.
 */
interface Presenter<V> {

    /**
     * Bind view to presenter.
     */
    fun bind(view: V)
}