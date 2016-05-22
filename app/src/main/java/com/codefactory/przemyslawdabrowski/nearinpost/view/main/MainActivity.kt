package com.codefactory.przemyslawdabrowski.nearinpost.view.main

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.WindowManager
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity

class MainActivity : BaseActivity() {

    val toolbar: Toolbar by bindView(R.id.commonToolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initViews()
        if (savedInstanceState == null) {
            replaceFragment(R.id.mainFragmentContainer, MainFragment(), MainFragment.TAG).commit()
        }
    }

    /**
     * Initialize views,
     */
    private fun initViews() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}
