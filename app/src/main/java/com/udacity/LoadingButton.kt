package com.udacity

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import kotlin.properties.Delegates

@SuppressLint("ObjectAnimatorBinding")
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator1 = ValueAnimator()
    private val valueAnimator2 = ValueAnimator()
    private var color = 0
    private var rectColor = 0
    private var degree = 0f
    private var rectWidth = 0f
    private var buttonText = "Download"

    private val paint:Paint = Paint().apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40f
        typeface = Typeface.create("",Typeface.BOLD)
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, old, new ->
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs,R.styleable.LoadingButton,defStyleAttr,0)
        color = typedArray.getColor(R.styleable.LoadingButton_main_color,0)
        rectColor = typedArray.getColor(R.styleable.LoadingButton_rect_color,0)
        typedArray.recycle()
        //color = resources.getColor(R.color.colorPrimary,null)
        //rectColor = resources.getColor(R.color.colorPrimaryDark,null)
        setBackgroundColor(color)

    }

    fun playAnimation(){
        buttonText = "We are Loading"
        valueAnimator1.setFloatValues(degree,360f)
        valueAnimator2.setFloatValues(rectWidth,widthSize.toFloat())
        valueAnimator1.addUpdateListener {
            degree = it.animatedValue as Float
            invalidate()
        }
        valueAnimator2.addUpdateListener {
            rectWidth = it.animatedValue as Float
        }

        val animatorSet = AnimatorSet().apply {
            duration = 5000
            play(valueAnimator1).with(valueAnimator2)
        }

        animatorSet.start()

        animatorSet.addListener(onEnd = {
            rectWidth = 0f
            degree = 0f
            buttonText = "Download"

        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = rectColor
        canvas?.drawRect(0f,0f,rectWidth,heightSize.toFloat(),paint)
        paint.color = Color.WHITE
        canvas?.drawText(buttonText,(widthSize/2).toFloat()-20f,(heightSize/1.5).toFloat(),paint)
        paint.color = Color.YELLOW
        canvas?.drawArc(440f,30f,490f,80f,0f,degree,true,paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec,0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}