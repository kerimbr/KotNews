package com.kerimbr.kotnews.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getViewBinding(): VB

}