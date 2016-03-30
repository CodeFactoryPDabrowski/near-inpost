package com.codefactory.przemyslawdabrowski.nearinpost.view.base

import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * Base implementation for application activities.
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * Create replace transaction.
     * @param containerId Id of container to replace new fragment.
     * @param newFragment new fragment to replace old one.
     * @param tag Tag for transactions.
     *
     * @return Transaction to commit.
     * */
    fun BaseActivity.replaceFragment(containerId: Int, newFragment: BaseFragment, tag: String): FragmentTransaction {
        return supportFragmentManager.beginTransaction().replace(containerId, newFragment, tag);
    }
}
