package co.kyash.spotinstructions

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class InstructionsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val spotPaint = Paint()
    private val point: PointF = PointF()
    private var animator: ValueAnimator? = null

    var overlayColor: Int = ContextCompat.getColor(context, R.color.default_cover)

    var listener: OnStateChangedListener? = null

    init {
        id = R.id.instructions_view
        bringToFront()
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        spotPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        setOnClickListener({
            if (animator != null && !animator!!.isRunning && animator!!.animatedValue as Float > 0) {
                listener?.onClicked()
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = overlayColor
        canvas?.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
        if (animator != null) {
            canvas?.drawCircle(point.x, point.y, animator!!.animatedValue as Float, spotPaint)
        }
    }

    fun turnUp(x: Float, y: Float, radius: Float, duration: Long, interpolator: TimeInterpolator) {
        this.point.set(x, y)
        animator = ValueAnimator.ofFloat(0f, radius).apply {
            addUpdateListener { invalidate() }
            this.interpolator = interpolator
            this.duration = duration
        }
        animator?.start()
    }

    fun turnDown(radius: Float, duration: Long, interpolator: TimeInterpolator) {
        animator = ValueAnimator.ofFloat(radius, 0f).apply {
            addUpdateListener { invalidate() }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                    //
                }

                override fun onAnimationEnd(animation: Animator?) {
                    listener?.onClosed()
                }

                override fun onAnimationCancel(animation: Animator?) {
                    //
                }

                override fun onAnimationStart(animation: Animator?) {
                    //
                }
            })
            this.interpolator = interpolator
            this.duration = duration
        }
        animator?.start()
    }

    interface OnStateChangedListener {
        fun onClosed()
        fun onClicked()
    }

}