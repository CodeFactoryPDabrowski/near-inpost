package com.codefactory.przemyslawdabrowski.nearinpost.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment

class MainFragment : BaseFragment() {
    companion object {
        /**
         * Tag for transactions.
         */
        val TAG = MainFragment.toString();
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.main_fragment, container, false);
        
        return view
    }
}
