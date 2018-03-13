package co.kyash.targetinstructions.targets

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Canvas
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import co.kyash.targetinstructions.AbstractTargetBuilder
import co.kyash.targetinstructions.R

class SimpleAnimationTarget(
        override val left: Float,
        override val top: Float,
        override val width: Float,
        override val height: Float,
        override val radius: Float,
        override val padding: Float,
        override val messageView: View
) : Target(left, top, width, height, radius, padding, messageView) {

    companion object {
        private val DEFAULT_ANIMATION_PER = 1000f
        private val DEFAULT_ANIMATION_DURATION = 600L
    }

    private val highlightShowAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, DEFAULT_ANIMATION_PER).apply {
        this.interpolator = BounceInterpolator()
        this.duration = DEFAULT_ANIMATION_DURATION
    }

    private val highlightHideAnimator: ValueAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
        this.interpolator = DecelerateInterpolator()
        this.duration = DEFAULT_ANIMATION_DURATION
    }

    override fun canClick(): Boolean {
        return !highlightShowAnimator.isRunning
    }

    override fun show(listener: ValueAnimator.AnimatorUpdateListener?) {
        highlightShowAnimator.let {
            it.addUpdateListener { listener?.onAnimationUpdate(it) }
            it.start()
        }

        val positionType = decideMessagePositionType()
        val container = messageView.findViewById<LinearLayout>(R.id.container)
        val margin = messageView.context.resources.getDimension(R.dimen.simple_target_message_margin)

        when (positionType) {
            Position.ABOVE -> {
                if (container.height > 0) {
                    container.y = top - container.height.toFloat() - margin
                    animateMessage(container.y)
                } else {
                    messageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            messageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            container.y = top - container.height.toFloat() - margin
                            animateMessage(container.y)
                        }
                    })
                }
            }
            Position.BELOW -> {
                container.y = top + height + margin
                animateMessage(container.y)
            }
        }
    }

    override fun hide(listener: OnStateChangedListener) {
        highlightHideAnimator.let {
            it.addUpdateListener { targetPaint.alpha = (it.animatedValue as Float).toInt() }
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                    //
                }

                override fun onAnimationEnd(animation: Animator?) {
                    listener.onClosed()
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

    override fun drawHighlight(canvas: Canvas) {
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
            canvas.drawRoundRect(left + animationDiffWidth, top - animationDiffHeight, right - animationDiffWidth, bottom + animationDiffHeight, radius, radius, targetPaint)
        } else {
            canvas.drawRect(left + animationDiffWidth, top - animationDiffHeight, right - animationDiffWidth, bottom + animationDiffHeight, targetPaint)
        }
    }

    private fun animateMessage(pivotY: Float) {
        val fadeInAnimation = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.ABSOLUTE, pivotY).apply {
            this.interpolator = BounceInterpolator()
            repeatCount = 0
            duration = 1000
            fillAfter = true
        }
        messageView.startAnimation(fadeInAnimation)
    }

    class Builder(context: Activity) : AbstractTargetBuilder<Builder, SimpleAnimationTarget>(context) {
        private var title: CharSequence? = null
        private var description: CharSequence? = null

        override fun self(): Builder {
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: CharSequence): Builder {
            this.description = description
            return this
        }

        override fun build(): SimpleAnimationTarget {
            val activity = activityWeakReference.get()
            if (activity == null) {
                throw IllegalStateException("activity is null")
            } else {
                val messageView = activity.layoutInflater.inflate(R.layout.layout_simple, null)
                (messageView.findViewById(R.id.title) as TextView).text = title
                (messageView.findViewById(R.id.description) as TextView).text = description
                messageView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

                return SimpleAnimationTarget(
                        left = left,
                        top = top,
                        width = width,
                        height = height,
                        radius = radius,
                        padding = padding,
                        messageView = messageView
                )
            }
        }
    }

}