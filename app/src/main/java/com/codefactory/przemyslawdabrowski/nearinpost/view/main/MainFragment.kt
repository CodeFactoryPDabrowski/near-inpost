package com.codefactory.przemyslawdabrowski.nearinpost.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.extension.givenPermission
import com.codefactory.przemyslawdabrowski.nearinpost.extension.hideKeyboard
import com.codefactory.przemyslawdabrowski.nearinpost.extension.makeInvisible
import com.codefactory.przemyslawdabrowski.nearinpost.extension.makeVisible
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.Machine
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.MainFragmentPresenter
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.MainFragmentView
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment
import com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view.LocationSearchView
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter.MainFragmentAdapter
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.adapter.MainFragmentHolder
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItem
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.item.MachineItemType
import javax.inject.Inject

class MainFragment : BaseFragment(), MainFragmentView {
    companion object {
        /**
         * Tag for transactions.
         */
        val TAG = MainFragment.toString()

        /**
         * Request code for location permission.
         */
        val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    @Inject
    lateinit var presenter: MainFragmentPresenter

    private lateinit var searchView: LocationSearchView
    private lateinit var searchButton: FloatingActionButton
    private lateinit var searchResultList: RecyclerView
    private lateinit var consumerView: View
    private lateinit var searchProgress: ProgressBar

    /**
     * Postal code fir searched in post machines.
     */
    private var postalCode: PostalCodeUi? = null

    /**
     * Adapter to provide search query results views for recyclerView.
     */
    private lateinit var adapter: MainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent().inject(this)
        presenter.bind(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.main_fragment, container, false)
        initViews(view)
        presenter.onCreateView()

        return view
    }

    override fun onResume() {
        super.onResume()
        searchProgress.makeInvisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (permissions.size == 1 && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.findCurrentLocation()
                } else {
                    Toast.makeText(activity
                            , activity.getString(R.string.main_fragment_permission_denied_text)
                            , Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                throw IllegalArgumentException("Unknown request code $requestCode")
            }
        }
    }

    override fun onFreshStart() {
        adapter.addInPostItemList(listOf(MachineItem(itemType = MachineItemType.FRESH_START)))
    }

    override fun onNearestInPostResult(postalCodeUi: PostalCodeUi, machines: List<Machine>) {
        searchProgress.makeInvisible()
        var items = emptyList<MachineItem>()
        if (machines.isNotEmpty()) {
            postalCode = postalCodeUi
            items += MachineItem(postalCodeUi, MachineItemType.POSTAL_CODE)
            items += machines.map { MachineItem(MachineUi(it)) }
        } else {
            items += listOf(MachineItem(itemType = MachineItemType.EMPTY))
        }
        adapter.addInPostItemList(items)
    }

    override fun onNearestInPostError(error: Throwable?) {
        searchProgress.makeInvisible()
        Toast.makeText(activity, R.string.main_error_message, Toast.LENGTH_SHORT).show()
    }

    override fun onFindCurrentLocationError(err: Throwable?) {
        searchProgress.makeInvisible()
        Toast.makeText(activity, R.string.main_error_message, Toast.LENGTH_SHORT).show()
    }

    override fun onEmptyPostalCode() {
        searchProgress.makeInvisible()
    }

    override fun onFindCurrentLocationCompleted() {
        Toast.makeText(activity, R.string.main_searched_by_last_known_location, Toast.LENGTH_SHORT).show()
        searchProgress.makeInvisible()
    }

    override fun getActivityContext(): BaseActivity {
        val activityContext = activity
        if (activityContext is BaseActivity) {
            return activityContext
        } else {
            throw IllegalArgumentException("All activities in application should extend BaseActivity!!!")
        }
    }

    /**
     * Initialize views.
     */
    private fun initViews(view: View?) {
        //Find necessary views.
        fun findViews() {
            if (view == null) {
                throw IllegalArgumentException("View cannot be null!!!")
            }
            searchButton = view.findViewById(R.id.mainInPostSearch) as FloatingActionButton
            searchView = view.findViewById(R.id.mainLocationToSearch) as LocationSearchView
            searchResultList = view.findViewById(R.id.mainInPostSearchResultList) as RecyclerView
            consumerView = view.findViewById(R.id.mainEventConsumerView)
            searchProgress = view.findViewById(R.id.mainSearchProgress) as ProgressBar
        }
        findViews()
        searchResultList.layoutManager = LinearLayoutManager(activity)
        searchResultList.setHasFixedSize(true)
        adapter = MainFragmentAdapter(object : MainFragmentHolder.MainFragmentHolderListener {
            override fun onMachineClicked(machineUi: MachineUi) {
                if (postalCode == null) {
                    throw IllegalArgumentException("Postal code shouldn't be null")
                }
                presenter.showDetails(machineUi, postalCode as PostalCodeUi)
            }

        })
        searchResultList.adapter = adapter
        searchButton.setOnClickListener { view ->
            searchView.hideHints()
            hideKeyboard(searchView.search.windowToken)
            searchProgress.makeVisible()
            presenter.searchForNearestInPost(searchView.getPostalCode())
        }
        consumerView.setOnTouchListener { view, motionEvent ->
            searchView.hideHints()
            false
        }
        searchView.setResultListener(object : LocationSearchView.LocationSearchView.LocationResultListener {
            override fun onResultClick(postalCodeUi: PostalCodeUi) {
                searchProgress.makeVisible()
                presenter.searchForNearestInPost(postalCodeUi)
            }
        })

        searchView.setCurrentLocationListener(object : LocationSearchView.LocationSearchView.CurrentLocationListener {
            override fun onCurrentLocationClick() {
                givenPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                        , LOCATION_PERMISSION_REQUEST_CODE
                        , {
                    searchProgress.makeVisible()
                    presenter.findCurrentLocation()
                })
            }
        })
    }
}
