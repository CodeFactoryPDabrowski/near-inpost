package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.content.Context
import android.location.Address
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerFragmentComponent
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//TODO: Need to improve this view!!! -> Hide hints on click outside, show only addresses with postal code...
class LocationSearchView(context: Context, attrs: AttributeSet?, defStyle: Int) : FrameLayout(context, attrs, defStyle) {

    /**
     * Container for search query.
     */
    lateinit var search: EditText

    /**
     * Current location image view button.
     */
    lateinit var currentLocation: View

    /**
     * List of search query results.
     */
    lateinit var searchResult: RecyclerView

    /**
     * Divider between search query and list of results.
     */
    lateinit var searchDivider: View

    /**
     * Time to start searching for results.
     */
    val DEBOUNCE_TIME = 400L

    /**
     * Max number of search results.
     */
    val GEOCODER_MAX_RESULTS = 3

    @Inject
    lateinit var reactiveLocationProvide: ReactiveLocationProvider

    /**
     * Adapter to provide search query results views to list.
     */
    private lateinit var adapter: LocationSearchAdapter

    /**
     * Subscription of search query for locations.
     */
    private var subscription: Subscription? = null

    /**
     * Listener of search results.
     */
    private var resultListener: LocationResultListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        DaggerFragmentComponent.builder().appComponent(App.appComponent).build().inject(this)
        inflate(context, R.layout.custom_location_search_view, this)
        search = findViewById(R.id.customLocationSearch) as EditText
        searchResult = findViewById(R.id.customLocationSearchResult) as RecyclerView
        searchDivider = findViewById(R.id.customLocationDivider)
        currentLocation = findViewById(R.id.customLocationCurrent)

        adapter = LocationSearchAdapter(object : LocationSearchHolder.LocationSearchHolderListener {
            override fun onResultClick(postalCodeUi: PostalCodeUi) {
                hideHints()
                resultListener?.let { (resultListener as LocationResultListener).onResultClick(postalCodeUi) }
            }

        })
        searchResult.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchResult.adapter = adapter
        setSearchLayoutHeight()
        search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    textChanged(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Empty
            }

            override fun afterTextChanged(s: Editable?) {
                //Empty
            }
        })

        //TODO: Search for current location.
        currentLocation.setOnClickListener { view -> Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show() }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription?.unsubscribe()
    }

    /**
     * Set listener to handle search result click.
     * @param listener Listener to handle results.
     */
    fun setResultListener(listener: LocationResultListener) {
        if (resultListener != null) {
            resultListener = null
        }
        resultListener = listener
    }

    /**
     * Get postal code from search location view. It can be postal code or text.
     */
    fun getPostalCode(): PostalCodeUi = PostalCodeUi(search.text.toString())


    /**
     * Hide hints result layout.
     */
    fun hideHints() {
        setSearchLayoutHeight()
        searchDivider.visibility = GONE
    }

    /**
     * Action on search edit text changed.
     * @param newText Changed text.
     */
    private fun textChanged(newText: String) {
        if (newText.length >= 2) {
            subscription = reactiveLocationProvide.getGeocodeObservable(newText, GEOCODER_MAX_RESULTS)
                    .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val resultWitPostalOnly = getAddressesWithPostalOnly(it)
                        adapter.addSearchResult(resultWitPostalOnly)
                        setSearchLayoutHeight(resultWitPostalOnly.size)
                        searchDivider.visibility = if (resultWitPostalOnly.size > 0) VISIBLE else GONE
                    }, {
                        err ->
                        hideHints()
                    })
        } else {
            hideHints()
        }
    }

    /**
     * Get results addresses with postal code only.
     * @param allAddressesResults List of all addresses results.
     * @return List of addresses with postal code.
     */
    private fun getAddressesWithPostalOnly(allAddressesResults: List<Address>): List<Address> = allAddressesResults.filter { addr -> addr.postalCode != null }

    /**
     * Set height of search query results.
     * @param itemCount Number of items.
     */
    private fun setSearchLayoutHeight(itemCount: Int = 0) {
        val recyclerLayoutParams = searchResult.layoutParams
        recyclerLayoutParams.height = (itemCount * resources.getDimension(R.dimen.main_search_view_height)).toInt()
        searchResult.layoutParams = recyclerLayoutParams
    }

    /**
     * Listener to handle search results.
     */
    interface LocationResultListener {

        /**
         * Click on result action.
         *@param postalCodeUi Postal code object.
         */
        fun onResultClick(postalCodeUi: PostalCodeUi)
    }
}
