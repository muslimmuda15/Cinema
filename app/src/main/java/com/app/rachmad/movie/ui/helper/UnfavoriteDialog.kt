package com.app.rachmad.movie.ui.helper

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.app.rachmad.movie.R
import com.app.rachmad.movie.sqlite.table.FavoriteTable
import com.app.rachmad.movie.viewmodel.ListModel
import kotlinx.android.synthetic.main.activity_unfavorite_dialog.*

class UnfavoriteDialog(val c: Context, val viewModel: ListModel, val favoriteTable: FavoriteTable) : Dialog(c, R.style.dialogStyle) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unfavorite_dialog)

        yes.setBackgroundColor(ContextCompat.getColor(c, R.color.white))
        no.setBackgroundColor(ContextCompat.getColor(c, R.color.white))

        no.setOnClickListener {
            dismiss()
        }

        yes.setOnClickListener {
            viewModel.deleteFavorite(favoriteTable)
            dismiss()
        }
    }
}
