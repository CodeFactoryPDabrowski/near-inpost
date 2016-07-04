package com.codefactory.przemyslawdabrowski.nearinpost.view.main

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Workaround to propagate permission result to fragments.
        for (fragment in supportFragmentManager.fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mainMenuMaxResults -> {
                //TODO: Show dialog with max results count.
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Initialize views,
     */
    private fun initViews() {
        toolbar.showOverflowMenu()
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}
