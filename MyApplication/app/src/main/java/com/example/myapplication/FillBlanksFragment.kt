package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class FillBlanksFragment : Fragment() {
    private var rateClickListener: RateClickListener? = null
    private val blanks = arrayOfNulls<EditText>(2)
    private var button: MaterialButton? = null

    private val words = listOf("is", "interaction")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fill_blanks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blanks[0] = view.findViewById(R.id.fill_blanks_edit_text_1)
        blanks[1] = view.findViewById(R.id.fill_blanks_edit_text_2)
        button = view.findViewById(R.id.fill_blanks_material_button)

        button?.setOnClickListener {
            view.clearFocus()

            for (i in blanks.indices) {
                if (blanks[i]?.text.toString() == words[i]) {
                    blanks[i]?.setTextColor(
                        getColor(
                            view,
                            R.color.option_selected_background_color
                        )
                    )
                } else {
                    blanks[i]?.setTextColor(getColor(view, android.R.color.holo_red_dark))

                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                for (blank in blanks) {
                    blank?.text?.clear()
                }

                rateClickListener?.onRated()
            }, 2000)

        }

        for (i in blanks.indices) {
            blanks[i]?.addTextChangedListener {
                blanks[i]?.setTextColor(getColor(view, R.color.black))
                if (checkFilling()) {
                    button?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun checkFilling(): Boolean {
        for (i in blanks.indices) {
            if (blanks[i]?.text?.isBlank() == true) {
                return false
            }
        }

        return true
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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
        fun newInstance() = FillBlanksFragment()
    }
}