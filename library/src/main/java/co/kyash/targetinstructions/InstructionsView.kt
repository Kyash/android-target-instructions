package co.kyash.targetinstructions

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class InstructionsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_ANIMATION_PER = 1000f
    }

    private val paint = Paint()
    private val targetPaint = Paint()
    private var currentTarget: Target? = null

    var overlayColor: Int = ContextCompat.getColor(context, R.color.default_cover)

    var listener: OnStateChangedListener? = null

    init {
        id = R.id.instructions_view
        bringToFront()
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        targetPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        setOnClickListener({
            if (currentTarget != null && !currentTarget!!.isAnimating()) {
                listener?.onClicked()
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = overlayColor
        canvas?.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

        if (currentTarget != null && canvas != null) {
            currentTarget!!.drawHighlight(canvas, targetPaint)
        }
    }

    fun showTarget(target: Target) {
        removeAllViews()
        addView(target.messageView)

        currentTarget = target
        currentTarget?.highlightShowAnimator?.let {
            it.addUpdateListener { invalidate() }
            it.start()
        }
        currentTarget?.show()
    }

    fun hideTarget(target: Target) {
        target.highlightHideAnimator.let {
            it.addUpdateListener { targetPaint.alpha = (it.animatedValue as Float).toInt() }
            it.addListener(object : Animator.AnimatorListener {
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

            it.start()
        }
    }

    interface OnStateChangedListener {
        fun onClosed()
        
        fun onClicked()
    }

}