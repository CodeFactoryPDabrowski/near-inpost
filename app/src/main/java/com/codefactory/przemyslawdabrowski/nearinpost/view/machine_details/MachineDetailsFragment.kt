package com.codefactory.przemyslawdabrowski.nearinpost.view.machine_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.model.ui.MachineUi
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.machine_details.MachineDetailsFragmentPresenter
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.machine_details.MachineDetailsFragmentView
import com.codefactory.przemyslawdabrowski.nearinpost.view.base.BaseFragment
import javax.inject.Inject

class MachineDetailsFragment : BaseFragment(), MachineDetailsFragmentView {
    companion object {
        /**
         * Tag for transactions.
         */
        val TAG = MachineDetailsFragment::class.java.canonicalName

        /**
         * Key for machine details.
         */
        val MACHINE_DETAILS_KEY = "machine_details_key"

        /**
         * Key for postal code.
         */
        val POSTAL_CODE_KEY = "postal_code_key"

        fun newInstance(machineUi: MachineUi, postalCode: String): MachineDetailsFragment {
            var fragment = MachineDetailsFragment()
            var args: Bundle = Bundle()
            args.putParcelable(MACHINE_DETAILS_KEY, machineUi)
            args.putString(POSTAL_CODE_KEY, postalCode)
            fragment.arguments = args

            return fragment
        }
    }

    @Inject
    lateinit var presenter: MachineDetailsFragmentPresenter
    lateinit var distance: TextView
    lateinit var city: TextView
    lateinit var street: TextView
    lateinit var postalCodeTextView: TextView
    lateinit var locationDescription: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.machine_details_fragment, container, false)
        initViews(view)
        return view
    }

    override fun showDetails(postalCode: String, machineDetails: MachineUi) {
        var empty = activity.resources.getString(R.string.machine_details_empty_field)
        var distanceFrom = activity.resources.getString(R.string.machine_details_distance_from)
        distance.text = "$distanceFrom '$postalCode' -> ${machineDetails.distance ?: empty} Km"
        city.text = machineDetails.town ?: empty
        street.text = "${machineDetails.street ?: empty}  ${machineDetails.buildingNumber ?: ""}"
        postalCodeTextView.text = machineDetails.postcode ?: empty
        locationDescription.text = machineDetails.locationDescription ?: empty
    }

    /**
     * Initialize views.
     */
    private fun initViews(view: View?) {
        //Find necessary views.
        fun findViews() {
            if (view == null) {
                throw IllegalArgumentException("View cannot be null!!!")
            }
            distance = view.findViewById(R.id.machineDetailsDistance) as TextView
            city = view.findViewById(R.id.machineDetailsCity) as TextView
            street = view.findViewById(R.id.machineDetailsStreet) as TextView
            postalCodeTextView = view.findViewById(R.id.machineDetailsPostalCode) as TextView
            locationDescription = view.findViewById(R.id.machineDetailsLocationDesc) as TextView
        }
        initComponent().inject(this)
        presenter.bind(this)
        findViews()
        presenter.setDetails(arguments)
    }
}