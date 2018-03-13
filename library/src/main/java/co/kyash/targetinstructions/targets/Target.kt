package co.kyash.targetinstructions.targets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View
import android.view.WindowManager

abstract class Target(
        open val left: Float,
        open val top: Float,
        open val width: Float,
        open val height: Float,
        open val radius: Float,
        open val padding: Float,
        open val messageView: View
) {

    val targetPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    enum class Position {
        ABOVE,
        BELOW
    }

    abstract fun show(listener: ValueAnimator.AnimatorUpdateListener? = null)

    abstract fun hide(listener: OnStateChangedListener)

    open fun drawHighlight(canvas: Canvas) {
        val left = this.left - this.padding
        val top = this.top - this.padding
        val right = this.left + this.width + this.padding
        val bottom = this.top + this.height + this.padding

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom, radius, radius, targetPaint)
        } else {
            canvas.drawRect(left, top, right, bottom, targetPaint)
        }
    }

    open fun canClick(): Boolean {
        return true
    }

    fun decideMessagePositionType(): Position {
        val screenSize = Point()
        (messageView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(screenSize)

        return if (top - height / 2 >= screenSize.y / 2) Position.ABOVE else Position.BELOW
    }

    interface OnStateChangedListener {

        fun onClosed()
    }

}