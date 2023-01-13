package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), RateClickListener {
    private val mainLooperHandler = Handler(Looper.getMainLooper())
    private val fragments = listOf(
        DragAndDropFragment.newInstance(),
        ConnectElementsFragment.newInstance(),
        FillBlanksFragment.newInstance(),
        SelectOptionFragment.newInstance(),
        StarsFragment.newInstance()
    )
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showNextFragment()
    }

    override fun onRated() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_activity_fragment_container, ThanksFragment.newInstance())
        }.commit()

        showNextFragment()
    }

    private fun showNextFragment() {
        ++index
        index %= fragments.size

        mainLooperHandler.postDelayed({
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_activity_fragment_container, fragments[index])
            }.commit()
        }, 2000)
    }
}