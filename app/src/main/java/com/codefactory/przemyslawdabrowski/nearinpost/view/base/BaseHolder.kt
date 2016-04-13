package com.codefactory.przemyslawdabrowski.nearinpost.view.base

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Base implementation for view holder.
 */
abstract class BaseHolder<I>(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    /**
     * Bind item to view holder.
     *
     * @param it Item to bind into holder views.
     */
    abstract fun bindItem(it: I)
}