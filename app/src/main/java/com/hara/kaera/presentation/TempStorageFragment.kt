package com.hara.kaera.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.databinding.FragmentTempStorageBinding
import com.hara.kaera.presentation.base.BindingFragment

class TempStorageFragment :
    BindingFragment<FragmentTempStorageBinding>(R.layout.fragment_temp_storage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}