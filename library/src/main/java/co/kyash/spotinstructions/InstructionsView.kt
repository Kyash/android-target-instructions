package co.kyash.spotinstructions

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
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
    private var currentSpot: Spot? = null
    private var widthAnimator: ValueAnimator? = null

    var overlayColor: Int = ContextCompat.getColor(context, R.color.default_cover)

    var listener: OnStateChangedListener? = null

    init {
        id = R.id.instructions_view
        bringToFront()
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        spotPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        setOnClickListener({
            if (widthAnimator != null && !widthAnimator!!.isRunning && widthAnimator!!.animatedValue as Float > 0) {
                listener?.onClicked()
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = overlayColor
        canvas?.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
        if (widthAnimator != null && currentSpot != null) {
            val factor = (widthAnimator!!.animatedValue as Float) / 1000f

            val left = currentSpot!!.getLeft() - currentSpot!!.getPadding()
            val top = currentSpot!!.getTop() - currentSpot!!.getPadding()
            val right = currentSpot!!.getLeft() + currentSpot!!.getWidth() + currentSpot!!.getPadding()
            val bottom = currentSpot!!.getTop() + currentSpot!!.getHeight() + currentSpot!!.getPadding()
            val radius = currentSpot!!.getRadius()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas?.drawRoundRect(left, top, right, bottom, radius, radius, spotPaint)
            } else {
                canvas?.drawRect(left, top, right, bottom, spotPaint)
            }
        }
    }

    fun turnUp(spot: Spot, duration: Long, interpolator: TimeInterpolator) {
        currentSpot = spot
        widthAnimator = ValueAnimator.ofFloat(0f, 1000f).apply {
            addUpdateListener { invalidate() }
            this.interpolator = interpolator
            this.duration = duration
        }
        widthAnimator?.start()
    }

    fun turnDown(radius: Float, duration: Long, interpolator: TimeInterpolator) {
        widthAnimator = ValueAnimator.ofFloat(radius, 0f).apply {
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
        widthAnimator?.start()
    }

    interface OnStateChangedListener {
        fun onClosed()
        fun onClicked()
    }

}