package co.kyash.targetinstructions

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import co.kyash.targetinstructions.targets.Target

class InstructionsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var currentTarget: Target? = null

    var overlayColor: Int = ContextCompat.getColor(context, R.color.default_cover)

    var listener: OnStateChangedListener? = null

    init {
        id = R.id.instructions_view
        bringToFront()
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        setOnClickListener({
            if (currentTarget != null && currentTarget!!.canClick()) {
                listener?.onClicked()
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = overlayColor
        canvas?.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

        if (currentTarget != null && canvas != null) {
            currentTarget!!.drawHighlight(canvas)
        }
    }

    fun showTarget(target: Target) {
        removeAllViews()
        addView(target.messageView)

        currentTarget = target
        currentTarget?.show(ValueAnimator.AnimatorUpdateListener { invalidate() })
    }

    fun hideTarget(target: Target) {
        target.hide(object : Target.OnStateChangedListener {
            override fun onClosed() {
                listener?.onClosed()
            }
        })
    }

    interface OnStateChangedListener {
        fun onClosed()

        fun onClicked()
    }

}