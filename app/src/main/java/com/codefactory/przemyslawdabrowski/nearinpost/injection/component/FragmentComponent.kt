package com.codefactory.przemyslawdabrowski.nearinpost.injection.component

import com.codefactory.przemyslawdabrowski.nearinpost.injection.scope.FragmentScope
import com.codefactory.przemyslawdabrowski.nearinpost.view.main.MainFragment
import dagger.Component

@FragmentScope
@Component(dependencies = arrayOf(AppComponent::class))
interface FragmentComponent {

    /**
     * Inject dependencies into mainFragment.
     */
    fun inject(mainFragment: MainFragment)
}