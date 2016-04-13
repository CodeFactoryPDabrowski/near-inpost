package com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem

class MainFragmentAdapter() : RecyclerView.Adapter<MainFragmentHolder>() {

    /**
     * List of inPost items displayed on list view.
     */
    var nearestInPostItemList: List<MachineItem> = emptyList()

    override fun onBindViewHolder(holder: MainFragmentHolder?, position: Int) {
        holder?.bindItem(nearestInPostItemList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainFragmentHolder? {
        return MainFragmentHolder(LayoutInflater.from(parent?.context).inflate(R.layout.main_fragment_holder, null))
    }

    override fun getItemCount(): Int = nearestInPostItemList.size

    /**
     * Refresh adapter items.
     */
    fun addInPostItemList(inPostItemList: List<MachineItem>) {
        nearestInPostItemList = emptyList()
        nearestInPostItemList += inPostItemList
        notifyDataSetChanged()
    }
}