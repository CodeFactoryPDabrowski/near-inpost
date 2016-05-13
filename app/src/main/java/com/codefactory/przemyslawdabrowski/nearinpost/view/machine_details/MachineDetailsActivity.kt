package com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.PostalCodeUi
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseActivity
import com.codefactory.przemyslawdabrowski.nearinpost.view.custom.bottom_slide_layout.BottomSlideLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MachineDetailsActivity : BaseActivity(), OnMapReadyCallback {
    companion object {

        /**
         * Delay of initialization support map fragment.
         */
        val MAP_INIT_DELAY = 500L //ms

        /**
         * Key for machine UI details.
         */
        val MACHINE_DETAILS_BUNDLE_KEY = "machine_details"

        /**
         * Key for postal code.
         */
        val POSTAL_CODE_BUNDLE_KEY = "postal_code"

        /**
         * Start this activity static factory method.
         * @param activity Activity context.
         * @param machineUi Machine details.
         * @param postalCodeUi Searched postal code.
         */
        fun startActivity(activity: BaseActivity, machineUi: MachineUi, postalCodeUi: PostalCodeUi) {
            var intent: Intent = Intent(activity, MachineDetailsActivity::class.java)
            intent.putExtra(MACHINE_DETAILS_BUNDLE_KEY, machineUi)
            intent.putExtra(POSTAL_CODE_BUNDLE_KEY, postalCodeUi)
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
        var postalCodeUi: PostalCodeUi? = intent.getParcelableExtra<PostalCodeUi>(POSTAL_CODE_BUNDLE_KEY) ?:
                throw IllegalArgumentException("Machine details cannot be null")

        if (savedInstanceState == null) {
            replaceFragment(R.id.machineDetailsContainer
                    , MachineDetailsFragment.newInstance(machineUi, postalCodeUi!!.postalCode), MachineDetailsFragment.TAG).commit()
        }
        initViews()
        initMap()
    }

    override fun onMapReady(p0: GoogleMap?) {
        // Disable interactions with map fragment.
        fun disableMapInteraction() {
            p0?.setOnMarkerClickListener { true }
            var mapUISettings = p0?.uiSettings
            if (mapUISettings != null) {
                mapUISettings.setAllGesturesEnabled(false)
            }
        }

        var inPostLocation: LatLng = LatLng((machineUi.latitude as Float).toDouble()
                , (machineUi.longitude as Float).toDouble());
        p0?.addMarker(MarkerOptions().icon(BitmapDescriptorFactory
                .fromResource(R.drawable.machine_details_loc_icon))
                .anchor(1F, 1F)
                .position(inPostLocation))
        p0?.moveCamera(CameraUpdateFactory.newLatLngZoom(inPostLocation, 15F))
        disableMapInteraction()
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

    /**
     * Initialize map fragment.
     */
    private fun initMap() {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            return
        }
        Handler().postDelayed({
            if (!isFinishing) {
                var inPostMapFragment = SupportMapFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.machineDetailsLocationContainer,
                        inPostMapFragment).commit()
                inPostMapFragment.getMapAsync(this)
            }
        }, MAP_INIT_DELAY)
    }

}