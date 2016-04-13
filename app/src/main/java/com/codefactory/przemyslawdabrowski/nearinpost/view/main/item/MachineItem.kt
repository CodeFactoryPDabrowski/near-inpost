package com.codefactory.przemyslawdabrowski.nearinpost.view.main.item

import com.codefactory.przemyslawdabrowski.nearinpost.model.api.ui.MachineUi

/**
 * @param item Item to display on search nearest inPost list view.
 * @param itemType Type of item displayed on list view.
 */
data class MachineItem(val item: MachineUi?, val itemType: MachineItemType = MachineItemType.ITEM)

enum class MachineItemType {
    ITEM, EMPTY
}