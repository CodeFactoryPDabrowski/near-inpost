package com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter

import android.view.View
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem

class MainFragmentHolder(itemView: View?) : BaseHolder<MachineItem>(itemView) {

    val machineName: TextView by bindView(R.id.mainMachineName)
    val machineStreet: TextView by bindView(R.id.mainMachineStreet)
    val machineDistance: TextView by bindView(R.id.mainMachineDistance)

    override fun bindItem(it: MachineItem) {
        //TODO: Implement this proper.
        machineName.text = it.item?.name
        machineStreet.text = it.item?.street
        machineDistance.text = it.item?.distance.toString()
    }
}