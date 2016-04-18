package com.codefactory.przemyslawdabrowski.nearinpost.injection.component

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.view.custom.location_search_view.LocationSearchView
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.MainFragment
import dagger.Component

@FragmentScope
@Component(dependencies = arrayOf(AppComponent::class))
interface FragmentComponent {

    /**
     * Inject dependencies into mainFragment.
     */
    fun inject(mainFragment: MainFragment)

    /**
     * Inject dependencies into location search view that lives fragment lifecycle.
     */
    fun inject(locationSearchView: LocationSearchView)
}