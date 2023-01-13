package com.devyataykin.vk_cup_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.devyataykin.vk_cup_2.common.ResponseClickListener

class MainActivity : AppCompatActivity(), ResponseClickListener {
    private val mainLooperHandler = Handler(Looper.getMainLooper())
    private var index = -1
    private val fragments = listOf(
        SelectOptionFragment.newInstance(),
        ConnectElementsFragment.newInstance(),
        FillBlanksFragment.newInstance(),
        StarsFragment.newInstance(),
        DragAndDropFragment.newInstance()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showNextFragment()
    }

    private fun showNextFragment() {
        ++index
        index %= fragments.size

        mainLooperHandler.postDelayed({
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_activity_frame_layout, fragments[index])
            }.commit()
        }, 2000)
    }

    override fun onResponseClicked() {
        showThanksFragment()
        showNextFragment()
    }

    private fun showThanksFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_activity_frame_layout, ThanksFragment.newInstance())
        }.commit()
    }
}