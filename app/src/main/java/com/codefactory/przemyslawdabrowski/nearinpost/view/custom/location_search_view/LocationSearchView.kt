package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import com.codefactory.przemyslawdabrowski.nearinpost.BuildConfig
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.api.GoogleGeocodeService
import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerFragmentComponent
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.AddressComponentType
import com.codefactory.przemyslawdabrowski.nearinpost.model.api.ReverseGeocodedAddressStatusType
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.SimpleLocationSearchResultUi
import com.jakewharton.rxbinding.widget.RxTextView
import retrofit2.Retrofit
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//TODO: Need to improve this view!!! -> Height of result adapter view, should be fixed!!!
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
     * Max results count.
     */
    val MAX_RESULTS = 3

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var geocodeService: GoogleGeocodeService

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
    private var resultListener: LocationSearchView.LocationResultListener? = null

    /**
     * Listener for current location click.
     */
    private var currentLocationListener: LocationSearchView.CurrentLocationListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        DaggerFragmentComponent.builder().appComponent(App.appComponent).build().inject(this)
        geocodeService = retrofit.create(GoogleGeocodeService::class.java)
        inflate(context, R.layout.custom_location_search_view, this)
        search = findViewById(R.id.customLocationSearch) as EditText
        searchResult = findViewById(R.id.customLocationSearchResult) as RecyclerView
        searchDivider = findViewById(R.id.customLocationDivider)
        currentLocation = findViewById(R.id.customLocationCurrent)

        adapter = LocationSearchAdapter(object : LocationSearchHolder.LocationSearchHolderListener {
            override fun onResultClick(postalCodeUi: PostalCodeUi) {
                hideHints()
                resultListener?.onResultClick(postalCodeUi)
            }

        })
        searchResult.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchResult.adapter = adapter
        setSearchLayoutHeight()

        // In case of any error while geocoding empty list will be passed -> onErrorReturn()
        subscription = RxTextView.textChangeEvents(search)
                .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter { it != null && it.text().length >= 3 }
                .flatMap {
                    geocodeService.reverseGeocodeAddress(it.text().toString()
                            , BuildConfig.GOOGLE_GEOCODING_API_KEY)
                            .filter { it.status == ReverseGeocodedAddressStatusType.OK.responseType && it.results.isNotEmpty() }
                            .map { it ->
                                var searchAddresses = emptyArray<SimpleLocationSearchResultUi>()
                                for (result in it.results) {
                                    result.addressComponents
                                            .filter { it.types.size == 1 && it.types[0] == AddressComponentType.POSTAL_CODE.componentType && !result.formattedAddress.isNullOrEmpty() && !it.longName.isNullOrEmpty() }
                                            .forEach {
                                                searchAddresses += SimpleLocationSearchResultUi(
                                                        result.formattedAddress!!
                                                        , PostalCodeUi(it.longName!!))
                                            }
                                }
                                return@map searchAddresses.take(MAX_RESULTS)
                            }
                            .onErrorReturn { it -> emptyList<SimpleLocationSearchResultUi>() }
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.addSearchResult(it)
                    val resultListSize = it.size
                    setSearchLayoutHeight(resultListSize)
                    searchDivider.visibility = if (resultListSize > 0) VISIBLE else GONE
                }, {
                    hideHints()
                })
        currentLocation.setOnClickListener { view -> currentLocationListener?.onCurrentLocationClick() }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription?.unsubscribe()
        if (resultListener != null) {
            resultListener = null
        }
        if (currentLocationListener != null) {
            currentLocationListener = null
        }
    }

    /**
     * Set listener to handle search result click.
     * @param listener Listener to handle results.
     */
    fun setResultListener(listener: LocationSearchView.LocationResultListener) {
        if (resultListener != null) {
            resultListener = null
        }
        resultListener = listener
    }

    /**
     * Set listener to handle current location click.
     * @param listener Listener to handle click on current location.
     */
    fun setCurrentLocationListener(listener: LocationSearchView.CurrentLocationListener) {
        if (currentLocationListener != null) {
            currentLocationListener = null
        }
        currentLocationListener = listener
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
     * Set height of search query results.
     * @param itemCount Number of items.
     */
    private fun setSearchLayoutHeight(itemCount: Int = 0) {
        val recyclerLayoutParams = searchResult.layoutParams
        recyclerLayoutParams.height = (itemCount * resources.getDimension(R.dimen.main_search_view_height)).toInt()
        searchResult.layoutParams = recyclerLayoutParams
    }

    /**
     * Common interface for all location search view listeners.
     */
    interface LocationSearchView {

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

        /**
         * Listener to handle click on search for current location.
         */
        interface CurrentLocationListener {

            /**
             * Click on current location button.
             */
            fun onCurrentLocationClick()
        }
    }
}
