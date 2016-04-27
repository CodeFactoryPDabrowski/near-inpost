package com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
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

    val toolbar: Toolbar by bindView(R.id.machineDetailsToolbar)
    val closeButton: ImageView by bindView(R.id.machineDetailsClose)
    val inPostName: TextView by bindView(R.id.machineDetailsName)
    lateinit var bottomSlideLayout: BottomSlideLayout

    /**
     * Object to displays details.
     */
    lateinit var machineUi: MachineUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.machine_details_activity)
        var machine = intent.getParcelableExtra<MachineUi>(MACHINE_DETAILS_BUNDLE_KEY)
        if (machine == null) {
            throw IllegalArgumentException("Machine details cannot be null")
        } else {
            machineUi = machine
        }
        initViews()
    }

    /**
     * Initialize views.
     */
    private fun initViews() {
        fun setUpToolbar() {
            setSupportActionBar(toolbar)
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false)
                actionBar.setDisplayShowTitleEnabled(false)
            }
        }
        setUpToolbar()
        bottomSlideLayout = findViewById(R.id.machineDetailsSlideLayout) as BottomSlideLayout
        bottomSlideLayout.addListener(object : BottomSlideLayout.Listener {
            override fun onDragDismissed() {
                finish()
            }

            override fun onDrag(top: Int) {
                //Empty.
            }
        })
        closeButton.setOnClickListener { view -> finish() }
        inPostName.text = machineUi.name
    }


}