package com.codefactory.przemyslawdabrowski.nearinpost.view.main.results_number

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.RadioGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R
import com.codefactory.przemyslawdabrowski.nearinpost.app.App
import com.codefactory.przemyslawdabrowski.nearinpost.injection.component.DaggerFragmentComponent
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.results_number.MainMaxResultsDialogPresenter
import com.codefactory.przemyslawdabrowski.nearinpost.presenter.main.results_number.MainMaxResultsDialogView
import javax.inject.Inject

class MainMaxResultsDialog : DialogFragment(), MainMaxResultsDialogView {

    companion object {
        val TAG = MainMaxResultsDialog::class.java.simpleName
    }

    @Inject
    lateinit var presenter: MainMaxResultsDialogPresenter

    /**
     * Contains options for max displaying results number of searched inPost machines.
     */
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerFragmentComponent.builder().appComponent(App.appComponent).build().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        presenter.bind(this)
        radioGroup = activity.layoutInflater.inflate(R.layout.main_max_results_dialog, null) as RadioGroup
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setView(radioGroup)
        dialogBuilder.setTitle(R.string.main_max_results_dialog_title)
        dialogBuilder.setPositiveButton(android.R.string.ok, { dialog, which ->
            presenter.saveMaxResultsNumber(MaxResultsType.resultsCountFromRadioButtonId(radioGroup.checkedRadioButtonId))
        })
        dialogBuilder.setNegativeButton(android.R.string.cancel, { dialog, which -> dismiss() })
        presenter.onCreateDialog()

        return dialogBuilder.create()
    }

    override fun checkMaxResults(maxInPostMachinesResult: Int) {
        when (maxInPostMachinesResult) {
            MaxResultsType.OPTION_1.maxResults -> radioGroup.check(MaxResultsType.OPTION_1.radioButtonId)
            MaxResultsType.OPTION_2.maxResults -> radioGroup.check(MaxResultsType.OPTION_2.radioButtonId)
            MaxResultsType.OPTION_3.maxResults -> radioGroup.check(MaxResultsType.OPTION_3.radioButtonId)
            else -> throw IllegalArgumentException("Unknown results number $maxInPostMachinesResult")

        }
    }

    private enum class MaxResultsType(val maxResults: Int, val radioButtonId: Int) {
        OPTION_1(3, R.id.mainMaxResultOption1),
        OPTION_2(5, R.id.mainMaxResultOption2),
        OPTION_3(10, R.id.mainMaxResultOption3);

        companion object {

            fun resultsCountFromRadioButtonId(radioButtonID: Int): Int? {
                val enumMap = MaxResultsType.values().associateBy({ it.radioButtonId }, { it.maxResults })
                return enumMap[radioButtonID]
            }
        }
    }
}