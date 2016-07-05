package com.codefactory.przemyslawdabrowski.nearinpost.view.main.results_number

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codefactory.przemyslawdabrowski.nearinpost.R

//TODO: Read number of results from shared prefs. add mvp pattern
class MainMaxResultsDialog : DialogFragment() {

    companion object {
        val TAG = MainMaxResultsDialog::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.main_max_results_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val radioGroup = activity.layoutInflater.inflate(R.layout.main_max_results_dialog, null)
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setView(radioGroup)
        dialogBuilder.setTitle(R.string.main_max_results_dialog_title)
        dialogBuilder.setPositiveButton(android.R.string.ok, { dialog, which -> throw UnsupportedOperationException("not implemented") })
        dialogBuilder.setNegativeButton(android.R.string.cancel, { dialog, which -> dismiss() })

        return dialogBuilder.create()
    }
}