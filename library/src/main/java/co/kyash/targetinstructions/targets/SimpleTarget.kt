package co.kyash.targetinstructions.targets

import android.app.Activity
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import co.kyash.targetinstructions.AbstractTargetBuilder
import co.kyash.targetinstructions.R

class SimpleTarget(
        override var left: Float,
        override var top: Float,
        override var width: Float,
        override var height: Float,
        override val radius: Float,
        override val paddingLeft: Float,
        override val paddingTop: Float,
        override val paddingRight: Float,
        override val paddingBottom: Float,
        override val messageView: View,
        override val targetView: View? = null,
        override val delay: Long = 0L,
        private val listener: OnStateChangedListener?
) : Target(
        left,
        top,
        width,
        height,
        radius,
        paddingLeft,
        paddingTop,
        paddingRight,
        paddingBottom,
        messageView,
        targetView,
        delay
) {

    override fun showImmediately() {
        val positionType = decideMessagePositionType()
        val container = messageView.findViewById<LinearLayout>(R.id.container)
        val margin = messageView.context.resources.getDimension(R.dimen.simple_target_message_margin)

        val topCaret = container.findViewById<View>(R.id.top_caret)
        val bottomCaret = container.findViewById<View>(R.id.bottom_caret)

        when (positionType) {
            Position.ABOVE -> {
                if (container.height > 0) {
                    bottomCaret.x = left
                    bottomCaret.visibility = View.VISIBLE
                    container.y = top - container.height.toFloat() - margin
                    animateMessage(container.y)
                } else {
                    messageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            messageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            bottomCaret.x = left
                            bottomCaret.visibility = View.VISIBLE
                            container.y = top - container.height.toFloat() - margin
                            animateMessage(container.y)
                        }
                    })
                }
            }
            Position.BELOW -> {
                topCaret.x = left
                topCaret.visibility = View.VISIBLE
                container.y = top + height + margin
                animateMessage(container.y)
            }
        }
    }

    override fun hideImmediately() {
        this.listener?.onClosed()
    }

    private fun animateMessage(pivotY: Float) {
        val fadeInAnimation = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.ABSOLUTE, pivotY).apply {
            this.interpolator = OvershootInterpolator()
            repeatCount = 0
            duration = 400
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                    //
                }

                override fun onAnimationEnd(animation: Animation?) {
                    //
                }

                override fun onAnimationStart(animation: Animation?) {
                    messageView.visibility = View.VISIBLE
                }

            })
        }
        messageView.startAnimation(fadeInAnimation)
    }

    class Builder(context: Activity) : AbstractTargetBuilder<Builder, SimpleTarget>(context) {
        private var title: CharSequence? = null
        private var description: CharSequence? = null
        private var listener: OnStateChangedListener? = null

        override fun self(): Builder = this

        fun setTitle(title: CharSequence): Builder = apply { this.title = title }

        fun setDescription(description: CharSequence): Builder = apply { this.description = description }

        fun setListener(listener: OnStateChangedListener): Builder = apply { this.listener = listener }

        override fun build(): SimpleTarget {
            val activity = activityWeakReference.get()
            if (activity == null) {
                throw IllegalStateException("activity is null")
            } else {
                val targetView = viewWeakReference?.get()

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
                        paddingLeft = paddingLeft,
                        paddingTop = paddingTop,
                        paddingRight = paddingRight,
                        paddingBottom = paddingBottom,
                        messageView = messageView,
                        targetView = targetView,
                        delay = startDelayMillis,
                        listener = listener
                )
            }
        }
    }

}