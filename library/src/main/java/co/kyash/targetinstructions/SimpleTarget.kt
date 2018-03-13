package co.kyash.targetinstructions

import android.app.Activity
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

class SimpleTarget(
        override val left: Float,
        override val top: Float,
        override val width: Float,
        override val height: Float,
        override val radius: Float,
        override val padding: Float,
        override val messageView: View
) : Target(left, top, width, height, radius, padding, messageView) {

    override fun show() {
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

    private fun animateMessage(pivotY: Float) {
        val fadeInAnimation = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.ABSOLUTE, pivotY).apply {
            this.interpolator = BounceInterpolator()
            repeatCount = 0
            duration = 1000
            fillAfter = true
        }
        messageView.startAnimation(fadeInAnimation)
    }

    class Builder(context: Activity) : AbstractTargetBuilder<Builder, SimpleTarget>(context) {
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

        override fun build(): SimpleTarget {
            val activity = activityWeakReference.get()
            if (activity == null) {
                throw IllegalStateException("activity is null")
            } else {
                val messageView = activity.layoutInflater.inflate(R.layout.layout_simple, null)
                (messageView.findViewById(R.id.title) as TextView).text = title
                (messageView.findViewById(R.id.description) as TextView).text = description
                messageView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

                return SimpleTarget(
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