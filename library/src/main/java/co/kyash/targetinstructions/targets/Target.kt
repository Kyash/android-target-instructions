package co.kyash.targetinstructions.targets

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.WindowManager

abstract class Target(
        open var left: Float,
        open var top: Float,
        open var width: Float,
        open var height: Float,
        open val radius: Float,
        open val paddingLeft: Float,
        open val paddingTop: Float,
        open val paddingRight: Float,
        open val paddingBottom: Float,
        open val messageView: View,
        open val targetView: View? = null,
        open val delay: Long = 0L
) {

    private val targetPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    internal enum class Position {
        ABOVE,
        BELOW
    }

    internal fun show() {
        Handler().postDelayed({
            updateCoordinate()
            showImmediately()
        }, delay)
    }

    internal abstract fun showImmediately()

    internal abstract fun hideImmediately()

    internal open fun drawHighlight(canvas: Canvas) {
        val left = this.left - this.paddingLeft
        val top = this.top - this.paddingTop
        val right = this.left + this.width + this.paddingRight
        val bottom = this.top + this.height + this.paddingBottom

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom, radius, radius, targetPaint)
        } else {
            canvas.drawRect(left, top, right, bottom, targetPaint)
        }
    }

    fun updateCoordinate() {
        if (targetView != null) {
            val location = IntArray(2)
            targetView!!.getLocationInWindow(location)
            left = location[0].toFloat()
            top = location[1].toFloat()
            width = targetView!!.width.toFloat()
            height = targetView!!.height.toFloat()
        }
    }

    internal open fun canClick(): Boolean = true

    internal fun decideMessagePositionType(): Position {
        val screenSize = Point()
        (messageView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(screenSize)

        return if (top - height / 2 >= screenSize.y / 2) Position.ABOVE else Position.BELOW
    }

    internal fun getCenterX() = left + width / 2

    internal fun getCenterY() = top + height / 2

    interface OnStateChangedListener {
        fun onClosed()
    }

}