package com.codefactory.przemyslawdabrowski.nearinpost.view.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerFragmentComponent
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.FragmentComponent

/**
 * Base implementation for applications fragments.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * Init component inside fragment that extends BaseFragment.
     */
    fun initComponent(): FragmentComponent
            = DaggerFragmentComponent.builder().appComponent(App.appComponent).build();
}




