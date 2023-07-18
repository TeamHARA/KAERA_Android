package com.hara.kaera.presentation

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.presentation.base.BindingFragment

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewPagerAdapter = HomeViewPagerAdapter()
        //homeViewPagerAdapter.submitList()
    }
}