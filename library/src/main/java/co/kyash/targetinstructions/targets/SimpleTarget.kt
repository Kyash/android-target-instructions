package co.kyash.targetinstructions.targets

import android.app.Activity
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import co.kyash.targetinstructions.AbstractTargetBuilder
import co.kyash.targetinstructions.R

class SimpleTarget(
        override var left: Float,
        override var top: Float,
        override var width: Float,
        override var height: Float,
        override val highlightRadius: Float,
        override val highlightPaddingLeft: Float,
        override val highlightPaddingTop: Float,
        override val highlightPaddingRight: Float,
        override val highlightPaddingBottom: Float,
        override val messageView: View,
        override val targetView: View? = null,
        override val delay: Long = 0L,
        private val messageAnimationDuration: Long = 300L,
        private val messageInterpolator: Interpolator,
        private val listener: OnStateChangedListener?
) : Target(
        left,
        top,
        width,
        height,
        highlightRadius,
        highlightPaddingLeft,
        highlightPaddingTop,
        highlightPaddingRight,
        highlightPaddingBottom,
        messageView,
        targetView,
        delay
) {

    override fun showImmediately() {
        val positionType = decideMessagePositionType()
        val container = messageView.findViewById<LinearLayout>(R.id.container)
        val topCaret = container.findViewById<ImageView>(R.id.top_caret)
        val bottomCaret = container.findViewById<ImageView>(R.id.bottom_caret)

        when (positionType) {
            Position.ABOVE -> {
                if (container.height > 0) {
                    bottomCaret.x = getCenterX() - bottomCaret.width / 2
                    bottomCaret.visibility = View.VISIBLE
                    container.y = top - container.height.toFloat() - highlightPaddingTop
                    animateMessage(container.y)
                } else {
                    messageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            messageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            bottomCaret.x = getCenterX() - bottomCaret.width / 2
                            bottomCaret.visibility = View.VISIBLE
                            container.y = top - container.height.toFloat() - highlightPaddingTop
                            animateMessage(container.y)
                        }
                    })
                }
            }
            Position.BELOW -> {
                topCaret.x = getCenterX() - topCaret.width / 2
                topCaret.visibility = View.VISIBLE
                container.y = top + height + highlightPaddingBottom
                animateMessage(container.y)
            }
        }
    }

    override fun hideImmediately() {
        this.listener?.onClosed()
    }

    private fun animateMessage(pivotY: Float) {
        val animation = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.ABSOLUTE, pivotY).apply {
            this.interpolator = messageInterpolator
            repeatCount = 0
            duration = this@SimpleTarget.messageAnimationDuration
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
        messageView.startAnimation(animation)
    }

    class Builder(context: Activity) : AbstractTargetBuilder<Builder, SimpleTarget>(context) {

        private lateinit var title: CharSequence
        private lateinit var description: CharSequence

        @LayoutRes
        private var messageLayoutResId = R.layout.layout_simple_message

        private var messageAnimationDuration: Long = 300L

        private var messageInterpolator: Interpolator = OvershootInterpolator()

        private var listener: OnStateChangedListener? = null

        override fun self(): Builder = this

        fun setTitle(title: CharSequence): Builder = apply { this.title = title }

        fun setDescription(description: CharSequence): Builder = apply { this.description = description }

        fun setListener(listener: OnStateChangedListener): Builder = apply { this.listener = listener }

        fun setMessageLayoutResId(@LayoutRes messageLayoutResId: Int) = apply {
            this.messageLayoutResId = messageLayoutResId
        }

        fun setMessageAnimationDuration(duration: Long) = apply { this.messageAnimationDuration = duration }

        fun setMessageInterpolator(interpolator: Interpolator) = apply {
            this.messageInterpolator = interpolator
        }

        override fun build(): SimpleTarget {
            val activity = activityWeakReference.get()
            if (activity == null) {
                throw IllegalStateException("activity is null")
            } else {
                val targetView = viewWeakReference?.get()

                val messageView = activity.layoutInflater.inflate(messageLayoutResId, null).apply {
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                    (findViewById<TextView>(R.id.title)).text = title
                    (findViewById<TextView>(R.id.description)).text = description
                }

                return SimpleTarget(
                        left = left,
                        top = top,
                        width = width,
                        height = height,
                        highlightRadius = highlightRadius,
                        highlightPaddingLeft = hightlightPaddingLeft,
                        highlightPaddingTop = highlightPaddingTop,
                        highlightPaddingRight = highlightPaddingRight,
                        highlightPaddingBottom = highlightPaddingBottom,
                        messageView = messageView,
                        targetView = targetView,
                        delay = startDelayMillis,
                        messageAnimationDuration = messageAnimationDuration,
                        messageInterpolator = messageInterpolator,
                        listener = listener
                )
            }
        }
    }

}