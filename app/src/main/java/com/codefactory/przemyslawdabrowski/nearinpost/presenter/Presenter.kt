package com.codefactory.przemyslawdabrowski.nearinpost.presenter

/**
 * Base presenter to implement.
 */
//TODO: Add unbind(view: V) and unbind in on destroy view!!!
interface Presenter<V> {

    /**
     * Bind view to presenter.
     */
    fun bind(view: V)
}