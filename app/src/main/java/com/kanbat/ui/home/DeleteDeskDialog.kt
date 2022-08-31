package com.kanbat.ui.home

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.samples.gridtopager.R

object DeleteDeskDialog {

    fun show(
        context: Context,
        onDeleteDeskListener: (() -> Unit)
    ) {
        var dialog: AlertDialog? = null
        dialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle(context.getString(R.string.str_delete_desk))
            .setPositiveButton(R.string.str_delete) { _, _ -> onDeleteDeskListener.invoke() }
            .setNegativeButton(R.string.str_cancel) { _, _ -> dialog?.dismiss() }
            .show()
    }
}