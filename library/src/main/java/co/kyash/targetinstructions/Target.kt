package co.kyash.targetinstructions

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator

abstract class Target(
        open val left: Float,
        open val top: Float,
        open val width: Float,
        open val height: Float,
        open val radius: Float,
        open val padding: Float,
        open val messageView: View
) {

    companion object {
        private val DEFAULT_ANIMATION_PER = 1000f
        private val DEFAULT_ANIMATION_DURATION = 600L
    }

    enum class Position {
        ABOVE,
        BELOW
    }

    val highlightShowAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, DEFAULT_ANIMATION_PER).apply {
        this.interpolator = BounceInterpolator()
        this.duration = DEFAULT_ANIMATION_DURATION
    }

    val highlightHideAnimator: ValueAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
        this.interpolator = DecelerateInterpolator()
        this.duration = DEFAULT_ANIMATION_DURATION
    }

    abstract fun show()

    fun isAnimating(): Boolean {
        return highlightShowAnimator.isRunning
    }

    fun decideMessagePositionType(): Position {
        val screenSize = Point()
        (messageView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(screenSize)

        return if (top - height / 2 >= screenSize.y / 2) Position.ABOVE else Position.BELOW
    }

    fun drawHighlight(canvas: Canvas, paint: Paint) {
        val factor = (highlightShowAnimator.animatedValue as Float) / DEFAULT_ANIMATION_PER

        val left = this.left - this.padding
        val top = this.top - this.padding
        val right = this.left + this.width + this.padding
        val bottom = this.top + this.height + this.padding

        val width = right - left
        val height = top - bottom

        val animationDiffWidth = width / 2 * (factor - 1) * -1
        val animationDiffHeight = height / 2 * (factor - 1) * -1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left + animationDiffWidth, top - animationDiffHeight, right - animationDiffWidth, bottom + animationDiffHeight, radius, radius, paint)
        } else {
            canvas.drawRect(left + animationDiffWidth, top - animationDiffHeight, right - animationDiffWidth, bottom + animationDiffHeight, paint)
        }
    }

}