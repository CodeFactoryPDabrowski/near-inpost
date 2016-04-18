package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerFragmentComponent
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

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        DaggerFragmentComponent.builder().appComponent(App.appComponent).build().inject(this)
        inflate(context, R.layout.custom_location_search_view, this)
        search = findViewById(R.id.customLocationSearch) as EditText
        searchResult = findViewById(R.id.customLocationSearchResult) as RecyclerView
        searchDivider = findViewById(R.id.customLocationDivider)

        adapter = LocationSearchAdapter()
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
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription?.unsubscribe()
    }

    /**
     * Get text from search location view.
     */
    fun getText(): String = search.text.toString()

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
                        adapter.addSearchResult(it)
                        setSearchLayoutHeight(it.size)
                        searchDivider.visibility = VISIBLE
                    }, {
                        err ->
                        hideHints()
                    })
        } else {
            hideHints()
        }
    }

    /**
     * Set height of search query results.
     * @param itemCount Number of items.
     */
    private fun setSearchLayoutHeight(itemCount: Int = 0) {
        var recyclerLayoutParams = searchResult.layoutParams
        recyclerLayoutParams.height = (itemCount * resources.getDimension(R.dimen.main_search_view_height)).toInt()
        searchResult.layoutParams = recyclerLayoutParams
    }

    /**
     * Hide hints result layout.
     */
    private fun hideHints() {
        setSearchLayoutHeight()
        searchDivider.visibility = GONE
    }
}
