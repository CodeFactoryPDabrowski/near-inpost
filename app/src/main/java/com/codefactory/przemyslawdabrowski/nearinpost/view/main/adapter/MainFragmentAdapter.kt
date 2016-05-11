package com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItemType

class MainFragmentAdapter(val listener: MainFragmentHolder.MainFragmentHolderListener) : RecyclerView.Adapter<BaseHolder<MachineItem>>() {

    /**
     * List of inPost items displayed on list view.
     */
    var nearestInPostItemList: List<MachineItem> = emptyList()

    override fun onBindViewHolder(holder: BaseHolder<MachineItem>?, position: Int) {
        //TODO: Simplify?
        if (holder != null && holder is MainFragmentHolder) {
            holder.bindItem(nearestInPostItemList[position])
        } else if (holder != null && holder is MainFragmentDisplayTextHolder
                && nearestInPostItemList[position].itemType == MachineItemType.POSTAL_CODE) {
            holder.bindItem(nearestInPostItemList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseHolder<MachineItem>? {
        when (viewType) {
            MachineItemType.ITEM.ordinal ->
                return MainFragmentHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.main_fragment_holder, parent, false), listener)
            MachineItemType.EMPTY.ordinal ->
                return MainFragmentDisplayTextHolder(R.string.main_fragment_empty_text, parent)
            MachineItemType.FRESH_START.ordinal ->
                return MainFragmentDisplayTextHolder(R.string.main_fragment_fresh_text, parent)
            MachineItemType.POSTAL_CODE.ordinal ->
                return MainFragmentDisplayTextHolder(parent)
            else
            -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun getItemCount(): Int = nearestInPostItemList.size

    override fun getItemViewType(position: Int): Int {
        return nearestInPostItemList[position].itemType.ordinal
    }

    /**
     * Refresh adapter items.
     */
    fun addInPostItemList(inPostItemList: List<MachineItem>) {
        nearestInPostItemList = emptyList()
        nearestInPostItemList += inPostItemList
        notifyDataSetChanged()
    }
}