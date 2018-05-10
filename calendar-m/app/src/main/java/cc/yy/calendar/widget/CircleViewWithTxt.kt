package cc.yy.calendar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

/**
 * Created by zpy on 16-9-28.
 */

class CircleViewWithTxt : View {
    private var paint: Paint by Delegates.notNull()
    private var paintText: Paint by Delegates.notNull()
    var txt = "0"
    var bgColor: Int = 0
    var textColor: Int = 0
    var textSize: Float = 0F
    private var rectF: RectF by Delegates.notNull()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        paint = Paint()
        paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        rectF = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.isAntiAlias = true
        paint.color = bgColor
        val width = measuredWidth
        val height = measuredHeight
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)

        paintText.color = textColor
        paintText.textSize = textSize
        paintText.isAntiAlias = true


        rectF.left = 0f
        rectF.top = 0f
        rectF.right = width.toFloat()
        rectF.bottom = height.toFloat()

        val fontMetrics = paintText.fontMetricsInt

        val baselineY = (rectF.bottom + rectF.top - fontMetrics.bottom.toFloat() - fontMetrics.top.toFloat()) / 2

        paintText.textAlign = Paint.Align.CENTER
        canvas.drawText(txt, rectF.centerX(), baselineY, paintText)

    }
}

