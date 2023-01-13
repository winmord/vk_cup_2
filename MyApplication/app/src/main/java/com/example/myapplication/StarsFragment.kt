package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class StarsFragment : Fragment() {

    private var rateClickListener: RateClickListener? = null
    private val stars = arrayOfNulls<ImageButton>(5)
    private var rateButton: MaterialButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stars[0] = view.findViewById(R.id.star1_image_button)
        stars[1] = view.findViewById(R.id.star2_image_button)
        stars[2] = view.findViewById(R.id.star3_image_button)
        stars[3] = view.findViewById(R.id.star4_image_button)
        stars[4] = view.findViewById(R.id.star5_image_button)

        rateButton = view.findViewById(R.id.rate_button)

        for (i in stars.indices) {
            stars[i]?.setOnClickListener {
                updateStars(i + 1)
            }
        }

        rateButton?.setOnClickListener { rateClickListener?.onRated() }
    }

    private fun updateStars(rating: Int) {
        for (i in 1..5) {
            stars[i - 1]?.setBackgroundResource(
                if (rating > i - 1)
                    R.drawable.ic_baseline_active_star_rate_24
                else
                    R.drawable.ic_baseline_inactive_star_rate_24
            )
        }

        rateButton?.visibility = if (rating > 0) View.VISIBLE else View.INVISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RateClickListener) {
            rateClickListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        rateClickListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = StarsFragment()
    }
}