package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class ConnectElementsFragment : Fragment() {
    private var rateClickListener: RateClickListener? = null

    private val leftElements = arrayOfNulls<TextView>(3)
    private val rightElements = arrayOfNulls<TextView>(3)
    private var imageView: ImageView? = null
    private var rateButton: MaterialButton? = null

    private var startPoint: PointF? = null
    private var stopPoint: PointF? = null
    private var selectedPoint: PointF? = null
    private var selectedView: View? = null

    private val points: MutableSet<PointF> = mutableSetOf()

    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connect_elements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leftElements[0] = view.findViewById(R.id.left_element_1)
        leftElements[1] = view.findViewById(R.id.left_element_2)
        leftElements[2] = view.findViewById(R.id.left_element_3)

        rightElements[0] = view.findViewById(R.id.right_element_1)
        rightElements[1] = view.findViewById(R.id.right_element_2)
        rightElements[2] = view.findViewById(R.id.right_element_3)

        imageView = view.findViewById(R.id.image_view)
        rateButton = view.findViewById(R.id.answer_button)

        rateButton?.setOnClickListener {
            rateClickListener?.onRated()
        }

        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)

        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 4f

        for (element in leftElements) {
            element?.setOnClickListener {
                if (!points.contains(selectedPoint)) {
                    selectedView?.setBackgroundResource(R.drawable.rounded_shape)
                    selectedView = null
                    selectedPoint = null
                }

                val rect = Rect()
                it.getGlobalVisibleRect(rect)

                it.setBackgroundResource(R.drawable.rounded_selected_shape)
                startPoint = PointF(rect.right.toFloat(), rect.top.toFloat())
                selectedPoint = startPoint
                selectedView = it
            }
        }

        for (element in rightElements) {
            element?.setOnClickListener {
                val rect = Rect()
                it.getGlobalVisibleRect(rect)

                stopPoint = PointF(rect.left.toFloat(), rect.top.toFloat())

                if (startPoint != null && stopPoint != null
                    && !points.contains(startPoint!!) && !points.contains(stopPoint!!)
                ) {
                    it.setBackgroundResource(R.drawable.rounded_selected_shape)
                    canvas.drawLine(
                        startPoint!!.x,
                        startPoint!!.y,
                        stopPoint!!.x,
                        stopPoint!!.y,
                        paint
                    )

                    imageView?.setImageBitmap(bitmap)

                    points.add(startPoint!!)
                    points.add(stopPoint!!)

                    startPoint = null
                    stopPoint = null
                    ++count

                    if (count == 3) {
                        rateButton?.visibility = View.VISIBLE
                    }
                } else {
                    imageView?.setImageBitmap(null)
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    canvas = Canvas(bitmap)
                    count = 0
                    rateButton?.visibility = View.INVISIBLE
                    points.clear()

                    leftElements.map { it?.setBackgroundResource(R.drawable.rounded_shape) }
                    rightElements.map { it?.setBackgroundResource(R.drawable.rounded_shape) }
                }
            }
        }
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
        fun newInstance() = ConnectElementsFragment()
    }
}