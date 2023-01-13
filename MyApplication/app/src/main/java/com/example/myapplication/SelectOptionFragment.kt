package com.example.myapplication

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.lang.StringBuilder
import kotlin.random.Random

class SelectOptionFragment : Fragment() {
    private var rateClickListener: RateClickListener? = null
    private val options = arrayOfNulls<LinearLayout>(4)
    private val checks = arrayOfNulls<ImageView>(4)
    private val percents = arrayOfNulls<TextView>(4)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_option, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options[0] = view.findViewById(R.id.option_1_button)
        options[1] = view.findViewById(R.id.option_2_button)
        options[2] = view.findViewById(R.id.option_3_button)
        options[3] = view.findViewById(R.id.option_4_button)

        checks[0] = view.findViewById(R.id.option_1_check)
        checks[1] = view.findViewById(R.id.option_2_check)
        checks[2] = view.findViewById(R.id.option_3_check)
        checks[3] = view.findViewById(R.id.option_4_check)

        percents[0] = view.findViewById(R.id.option_1_percent)
        percents[1] = view.findViewById(R.id.option_2_percent)
        percents[2] = view.findViewById(R.id.option_3_percent)
        percents[3] = view.findViewById(R.id.option_4_percent)

        for (i in options.indices) {
            options[i]?.setOnClickListener {
                checks[i]?.visibility = View.VISIBLE
                var answerPercent = Random.nextInt(0, 100)

                percents[i]?.text = "$answerPercent%"
                percents[i]?.visibility = View.VISIBLE

                for (j in percents.indices) {
                    if (j == i) continue
                    answerPercent = 100 - answerPercent
                    answerPercent = Random.nextInt(0, answerPercent)
                    percents[j]?.text = "$answerPercent%"
                    percents[j]?.visibility = View.VISIBLE
                }

                val colorAnimation =
                    ValueAnimator.ofObject(
                        ArgbEvaluator(),
                        getColor(view, R.color.option_background_color),
                        getColor(view, R.color.option_selected_background_color)
                    )
                colorAnimation.duration = 250

                colorAnimation.addUpdateListener { animator ->
                    it.backgroundTintList =
                        ColorStateList.valueOf(animator.animatedValue as Int)
                }
                colorAnimation.start()

                Handler(Looper.getMainLooper()).postDelayed({
                    rateClickListener?.onRated()
                }, 2000)
            }
        }
    }

    private fun getColor(view: View, id: Int): Int = ContextCompat.getColor(
        view.context,
        id
    )

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
        fun newInstance() = SelectOptionFragment()
    }
}