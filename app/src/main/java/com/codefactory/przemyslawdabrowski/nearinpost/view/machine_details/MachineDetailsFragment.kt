package com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment

class MachineDetailsFragment : BaseFragment() {

    companion object {
        /**
         * Tag for transactions.
         */
        val TAG = MachineDetailsFragment::class.java.canonicalName
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.machine_details_fragment, container, false)

        return view
    }
}