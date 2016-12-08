package com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter

import android.content.Context
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItemType

//TODO: For postal code should be other holder?
class MainFragmentDisplayTextHolder(parent: ViewGroup?) :
        BaseHolder<MachineItem>(LayoutInflater.from(parent?.context)
                .inflate(R.layout.main_fragment_empty_holder, parent, false)) {

    val displayedText: TextView by bindView(R.id.mainDisplayedText)

    /**
     * Context for getting resources.
     */
    var context: Context?

    init {
        context = parent?.context
    }

    constructor(@StringRes textResId: Int, parent: ViewGroup?) : this(parent) {
        displayedText.setText(textResId)
    }

    override fun bindItem(it: MachineItem) {
        if (it.itemType == MachineItemType.POSTAL_CODE) {
            val prefix = context?.getString(R.string.main_fragment_postal_code_text)
            displayedText.text = "$prefix '${(it.itemData as PostalCodeUi).postalCode}' "
        }
    }

}
