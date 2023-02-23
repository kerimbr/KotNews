package com.kerimbr.kotnews.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding> : Fragment(){

    private var _binding: VB? = null
    protected var binding: VB
        get() = _binding!!
        set(value) {
            _binding = value
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean = false): VB

}