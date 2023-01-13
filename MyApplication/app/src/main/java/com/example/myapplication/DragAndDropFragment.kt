package com.example.myapplication

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Paint.Align
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton


class DragAndDropFragment : Fragment() {
    private var rateClickListener: RateClickListener? = null
    private val drags = arrayOfNulls<TextView>(2)
    private val edits = arrayOfNulls<EditText>(2)
    private var answerButton: MaterialButton? = null
    private var currentDrag: View? = null
    private val words = listOf("is", "drop")

    private val maskDragListener = View.OnDragListener { view, dragEvent ->
        val draggableItem = dragEvent.localState as View
        currentDrag = draggableItem

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {

                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {

                view.invalidate()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun attachViewDragListener(textView: TextView) {
        textView.setOnLongClickListener { view: View ->
            val item = ClipData.Item(textView.text)

            val dataToDrag = ClipData(
                textView.text,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val maskShadow = MaskDragShadowBuilder(view)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, maskShadow, view, 0)
            } else {
                view.startDragAndDrop(dataToDrag, maskShadow, view, 0)
            }
            
            true
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drag_and_drop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drags[0] = view.findViewById(R.id.drag_1)
        drags[1] = view.findViewById(R.id.drag_2)

        edits[0] = view.findViewById(R.id.fill_blanks_edit_text_1)
        edits[1] = view.findViewById(R.id.fill_blanks_edit_text_2)

        answerButton = view.findViewById(R.id.answer_material_button)

        for (i in drags.indices) {
            drags[i]?.text = words[i]
            attachViewDragListener(drags[i]!!)
            drags[i]?.setOnDragListener(maskDragListener)
        }

        for (edit in edits) {
            edit?.addTextChangedListener {
                if (edit.text?.isNotBlank() == true) {
                    edit.isEnabled = false

                    if (currentDrag != null) {
                        currentDrag?.visibility = View.GONE
                        currentDrag = null
                    }
                }

                if (checkFilling()) {
                    answerButton?.visibility = View.VISIBLE
                }
            }
        }

        answerButton?.setOnClickListener {
            for (i in edits.indices) {
                if (edits[i]?.text.toString() != words[i]) {
                    edits[i]?.setTextColor(Color.RED)
                } else {
                    edits[i]?.setTextColor(Color.GREEN)
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                for (blank in edits) {
                    blank?.text?.clear()
                }

                rateClickListener?.onRated()
            }, 2000)
        }
    }

    private fun checkFilling(): Boolean {
        for (edit in edits) {
            if (edit?.text?.isBlank() == true) {
                return false
            }
        }

        return true
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

    private class MaskDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = TextDrawable(view.context.resources, (view as TextView).text)

        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width
            val height: Int = view.height

            shadow.setBounds(0, 0, width, height)
            size.set(width, height)
            touch.set(width / 2, height / 2)
        }

        override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }
    }

    class TextDrawable(res: Resources, private val mText: CharSequence) : Drawable() {
        private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val mIntrinsicWidth: Int
        private val mIntrinsicHeight: Int
        override fun draw(canvas: Canvas) {
            val bounds: Rect = bounds
            canvas.drawText(
                mText, 0, mText.length,
                bounds.centerX().toFloat(), bounds.centerY().toFloat(), mPaint
            )
        }

        override fun getOpacity(): Int {
            return mPaint.alpha
        }

        override fun getIntrinsicWidth(): Int {
            return mIntrinsicWidth
        }

        override fun getIntrinsicHeight(): Int {
            return mIntrinsicHeight
        }

        override fun setAlpha(alpha: Int) {
            mPaint.alpha = alpha
        }

        override fun setColorFilter(filter: ColorFilter?) {
            mPaint.colorFilter = filter
        }

        companion object {
            private const val DEFAULT_COLOR = Color.GRAY
            private const val DEFAULT_TEXTSIZE = 18
        }

        init {
            mPaint.color = DEFAULT_COLOR
            mPaint.textAlign = Align.CENTER
            val textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                DEFAULT_TEXTSIZE.toFloat(), res.displayMetrics
            )
            mPaint.textSize = textSize
            mIntrinsicWidth = (mPaint.measureText(mText, 0, mText.length) + .5).toInt()
            mIntrinsicHeight = mPaint.getFontMetricsInt(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DragAndDropFragment()
    }
}