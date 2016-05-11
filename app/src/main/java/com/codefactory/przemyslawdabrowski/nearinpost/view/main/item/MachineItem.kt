package com.codefactory.przemyslawdabrowski.nearinpost.view.main.item

/**
 * @param itemData Data to display on search result list.
 * @param itemType Type of item displayed on list view.
 */
data class MachineItem(val itemData: ItemData? = null
                       , val itemType: MachineItemType = MachineItemType.ITEM)

enum class MachineItemType {
    ITEM, EMPTY, FRESH_START, POSTAL_CODE
}