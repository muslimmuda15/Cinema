package com.app.rachmad.movie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    val viewModel by lazy {
        (activity as BaseActivity).viewModel
    }
    val c = this.getContext()
}