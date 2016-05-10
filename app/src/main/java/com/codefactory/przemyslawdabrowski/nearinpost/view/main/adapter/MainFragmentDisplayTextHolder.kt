package com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter

import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseHolder
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem

class MainFragmentDisplayTextHolder(@StringRes var textResId: Int, var parent: ViewGroup?) :
        BaseHolder<MachineItem>(LayoutInflater.from(parent?.context)
                .inflate(R.layout.main_fragment_empty_holder, parent, false)) {

    val displayedText: TextView by bindView(R.id.mainDisplayedText)

    init {
        displayedText.setText(textResId)
    }

    override fun bindItem(it: MachineItem) {
        throw UnsupportedOperationException("Empty view shouldn't have item!!!")
    }

}
