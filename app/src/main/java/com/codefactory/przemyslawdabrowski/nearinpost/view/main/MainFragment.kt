package com.codefactory.przemyslawdabrowski.nearinpost.view.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.MainFragmentPresenter
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.MainFragmentView
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter.MainFragmentAdapter
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem
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

    private lateinit var searchView: EditText
    private lateinit var searchButton: FloatingActionButton
    private lateinit var searchResultList: RecyclerView

    /**
     * Adapter to provide search query results views for recyclerView.
     */
    private lateinit var adapter: MainFragmentAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.main_fragment, container, false);
        initViews(view)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onNearestInPostResult(machines: List<Machine>) {
        var items = machines.map { MachineItem(MachineUi(it)) }
        adapter.addInPostItemList(items)
    }

    override fun onNearestInPostError(error: Throwable?) {
        //TODO: Implement this.
        throw UnsupportedOperationException()
    }

    /**
     * Initialize views.
     */
    private fun initViews(view: View?) {
        initComponent().inject(this)
        presenter.bind(this)
        if (view == null) {
            return
        }
        searchButton = view.findViewById(R.id.mainInPostSearch) as FloatingActionButton
        searchView = view.findViewById(R.id.mainLocationToSearch) as EditText
        searchResultList = view.findViewById(R.id.mainInPostSearchResultList) as RecyclerView


        searchResultList.layoutManager = LinearLayoutManager(activity)
        searchResultList.setHasFixedSize(true)
        adapter = MainFragmentAdapter()
        searchResultList.adapter = adapter

        searchButton.setOnClickListener({ view -> presenter.searchForNearestInPost(searchView.text.toString()) })
    }
}
