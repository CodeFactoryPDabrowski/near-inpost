package com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details

import android.content.Intent
import android.os.Bundle
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity
import com.codefactory.przemyslawdabrowski.nearinpost.view.custom.bottom_slide_layout.BottomSlideLayout

class MachineDetailsActivity : BaseActivity() {

    companion object {
        /**
         * Start this activity static factory method.
         * @param activity Activity context.
         */
        fun newInstance(activity: BaseActivity) {
            activity.startActivity(Intent(activity, MachineDetailsActivity::class.java))
        }
    }

    lateinit var bottomSlideLayout: BottomSlideLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.machine_details_activity)
        initViews()
    }

    /**
     * Initialize views.
     */
    private fun initViews() {
        bottomSlideLayout = findViewById(R.id.machineDetailsSlideLayout) as BottomSlideLayout
        bottomSlideLayout.addListener(object : BottomSlideLayout.Listener {
            override fun onDragDismissed() {
                finish()
            }

            override fun onDrag(top: Int) {
                //Empty.
            }
        })
    }


}