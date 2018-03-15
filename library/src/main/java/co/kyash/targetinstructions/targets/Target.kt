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
        open val highlightRadius: Float,
        open val highlightPaddingLeft: Float,
        open val highlightPaddingTop: Float,
        open val highlightPaddingRight: Float,
        open val highlightPaddingBottom: Float,
        open val messageView: View,
        open val targetView: View? = null,
        open val delay: Long = 0L,
        open var positionType: Position? = null
) {

    private val targetPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    internal fun show() {
        Handler().postDelayed({
            updateCoordinate()

            if (positionType == null) {
                positionType = decideMessagePositionType()
            }

            showImmediately()
        }, delay)
    }

    internal abstract fun showImmediately()

    internal abstract fun hideImmediately()

    internal open fun drawHighlight(canvas: Canvas) {
        val left = this.left - this.highlightPaddingLeft
        val top = this.top - this.highlightPaddingTop
        val right = this.left + this.width + this.highlightPaddingRight
        val bottom = this.top + this.height + this.highlightPaddingBottom

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom, highlightRadius, highlightRadius, targetPaint)
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

    private fun decideMessagePositionType(): Position {
        val screenSize = Point()
        (messageView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(screenSize)
        return if (top > screenSize.y - (top + height)) Position.ABOVE else Position.BELOW
    }

    internal fun getCenterX() = left + width / 2

    internal fun getCenterY() = top + height / 2

    interface OnStateChangedListener {
        fun onClosed()
    }

    enum class Position {
        ABOVE,
        BELOW
    }

}