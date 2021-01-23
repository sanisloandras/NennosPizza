package com.sanislo.nennospizza.presentation.thankyou

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sanislo.nennospizza.R
import kotlinx.android.synthetic.main.fragment_thanks_for_order.*

class ThankYouFragment : Fragment(R.layout.fragment_thanks_for_order) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_back_to_home.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}