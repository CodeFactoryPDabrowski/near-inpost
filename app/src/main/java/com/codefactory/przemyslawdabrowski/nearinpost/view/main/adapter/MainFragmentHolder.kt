package com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter

import android.content.Context
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
     * Context needs for resources.
     */
    private lateinit var context: Context

    /**
     * Listener to communicate with parent component.
     */
    private lateinit var holderListener: MainFragmentHolderListener

    /**
     * InPost ui object.
     */
    private var machineUI: MachineUi? = null

    init {
        context = itemView!!.context
        holderListener = listener
        holderLayout.setOnClickListener { view -> if (machineUI != null) holderListener.onMachineClicked(machineUI as MachineUi) }
    }

    override fun bindItem(it: MachineItem) {
        if (it.itemData != null && it.itemData is MachineUi) {
            machineUI = it.itemData
            machineName.text = (machineUI as MachineUi).name
            machineStreet.text = (machineUI as MachineUi).street
            machineDistance.text = (machineUI as MachineUi).distance.toString() + " " +
                    context.getString(R.string.main_fragment_holder_distance_unit)
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