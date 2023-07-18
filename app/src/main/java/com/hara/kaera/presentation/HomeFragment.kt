package com.hara.kaera.presentation

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.FragmentHomeBinding
import com.hara.kaera.presentation.base.BindingFragment

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpContainer.adapter = HomeAdapter().apply {
            // sample list
            submitList(
                listOf(
                    listOf(
                        Gem(R.drawable.gem_blue__on, "gemstone1"),
                        Gem(R.drawable.gem_pink_l, "gemstone2"),
                        Gem(R.drawable.gem_red_l, "gemstone3")
                    ),
                    listOf(
                        Gem(R.drawable.gem_green_m, "jewel1"),
                        Gem(R.drawable.gem_orange_m, "jewel2"),
                        Gem(R.drawable.gem_pink_s_on, "jewel3")
                    ),
                )
            )
        }
    }
}