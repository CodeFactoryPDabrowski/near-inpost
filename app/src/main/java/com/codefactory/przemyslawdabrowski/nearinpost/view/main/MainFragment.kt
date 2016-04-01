package com.codefactory.przemyslawdabrowski.nearinpost.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.MainFragmentPresenter
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.MainFragmentView
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment
import javax.inject.Inject

class MainFragment : BaseFragment(), MainFragmentView {

    companion object {
        /**
         * Tag for transactions.
         */
        val TAG = MainFragment.toString();
    }

    @Inject
    lateinit var presenter: MainFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.main_fragment, container, false);
        initViews()

        return view
    }

    private fun initViews() {
        initComponent().inject(this)
        presenter.bind(this)
        //TODO: init rest of views
    }
}
