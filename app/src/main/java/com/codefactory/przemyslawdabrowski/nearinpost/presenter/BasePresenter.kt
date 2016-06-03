package com.codefactory.przemyslawdabrowski.nearinpost.presenter

import rx.subscriptions.CompositeSubscription

/**
 * Base implementation for presenter
 */
abstract class BasePresenter<V : Any> : Presenter<V> {

    /**
     * View for presenters.
     */
    protected lateinit var view: V

    /**
     * RxJava subscriptions. Subscribe rx actions and unsubscribe it.
     */
    protected var subscriptions: CompositeSubscription? = null

    override fun bind(view: V) {
        this.view = view
    }

    override fun subscribeRx() {
        subscriptions = null
        subscriptions = CompositeSubscription()
    }

    override fun unsubscribeRx() {
        subscriptions?.unsubscribe()
    }
}