package com.devyataykin.vk_cup_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ThanksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_thanks, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ThanksFragment()
    }
}