package com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details

import android.content.Intent
import android.os.Bundle
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity
import com.codefactory.przemyslawdabrowski.nearinpost.view.custom.bottom_slide_layout.BottomSlideLayout

class MachineDetailsActivity : BaseActivity() {

    companion object {

        /**
         * Key for machine UI details.
         */
        val MACHINE_DETAILS_BUNDLE_KEY = "machine_details"

        /**
         * Start this activity static factory method.
         * @param activity Activity context.
         * @param machineUi Machine details.
         */
        fun newInstance(activity: BaseActivity, machineUi: MachineUi) {
            var intent: Intent = Intent(activity, MachineDetailsActivity::class.java)
            intent.putExtra(MACHINE_DETAILS_BUNDLE_KEY, machineUi)
            activity.startActivity(intent)
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