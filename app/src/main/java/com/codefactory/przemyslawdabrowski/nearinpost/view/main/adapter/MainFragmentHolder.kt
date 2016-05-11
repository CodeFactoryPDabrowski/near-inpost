package com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem

class MainFragmentHolder(itemView: View?, listener: MainFragmentHolderListener) : BaseHolder<MachineItem>(itemView) {

    val machineName: TextView by bindView(R.id.mainMachineName)
    val machineStreet: TextView by bindView(R.id.mainMachineStreet)
    val machineDistance: TextView by bindView(R.id.mainMachineDistance)
    val holderLayout: RelativeLayout by bindView(R.id.mainFragmentHolderLayout)

    /**
     * Listener to communicate with parent component.
     */
    lateinit var holderListener: MainFragmentHolderListener

    /**
     * InPost ui object.
     */
    var machineUI: MachineUi? = null

    init {
        holderListener = listener
        holderLayout.setOnClickListener { view -> if (machineUI != null) holderListener.onMachineClicked(machineUI as MachineUi) }
    }

    override fun bindItem(it: MachineItem) {
        if (it.itemData != null && it.itemData is MachineUi) {
            machineUI = it.itemData
            machineName.text = (machineUI as MachineUi).name
            machineStreet.text = (machineUI as MachineUi).street
            machineDistance.text = (machineUI as MachineUi).distance.toString()
        }
    }

    /**
     * Interface to communicate with parent component.
     */
    interface MainFragmentHolderListener {

        /**
         * Click on inPost machine.
         * @param machineUi UI object.
         */
        fun onMachineClicked(machineUi: MachineUi)
    }
}