package co.kyash.targetinstructions.targets

import android.app.Activity
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
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
        override val radius: Float,
        override val highlightPaddingLeft: Float,
        override val highlightPaddingTop: Float,
        override val highlightPaddingRight: Float,
        override val highlightPaddingBottom: Float,
        override val messageView: View,
        override val targetView: View? = null,
        override val delay: Long = 0L,
        private val messageAnimationDuration: Long = 300L,
        private val listener: OnStateChangedListener?
) : Target(
        left,
        top,
        width,
        height,
        radius,
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
        val margin = messageView.context.resources.getDimension(R.dimen.simple_target_message_margin)

        val topCaret = container.findViewById<ImageView>(R.id.top_caret)
        val bottomCaret = container.findViewById<ImageView>(R.id.bottom_caret)

        when (positionType) {
            Position.ABOVE -> {
                if (container.height > 0) {
                    bottomCaret.x = getCenterX() - bottomCaret.width / 2
                    bottomCaret.visibility = View.VISIBLE
                    container.y = top - container.height.toFloat() - margin
                    animateMessage(container.y)
                } else {
                    messageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            messageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            bottomCaret.x = getCenterX() - bottomCaret.width / 2
                            bottomCaret.visibility = View.VISIBLE
                            container.y = top - container.height.toFloat() - margin
                            animateMessage(container.y)
                        }
                    })
                }
            }
            Position.BELOW -> {
                topCaret.x = getCenterX() - topCaret.width / 2
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
        val animation = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.ABSOLUTE, pivotY).apply {
            this.interpolator = OvershootInterpolator()
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

        @DrawableRes
        private var topCaretDrawableResId = R.drawable.img_caret_top
        @DrawableRes
        private var bottomCaretDrawableResId = R.drawable.img_caret_bottom
        @DrawableRes
        private var messageBgDrawableResId = R.drawable.message_bg

        @ColorRes
        private var textColorResId = android.R.color.black

        @DimenRes
        private var titleDimenResId = 0
        @DimenRes
        private var descriptionDimenResId = 0

        private var messageAnimationDuration: Long = 300L

        @DimenRes
        private var messageMarginLeftResId = R.dimen.simple_target_message_margin
        @DimenRes
        private var messageMarginTopResId = R.dimen.simple_target_message_margin
        @DimenRes
        private var messageMarginRightResId = R.dimen.simple_target_message_margin
        @DimenRes
        private var messageMarginBottomResId = R.dimen.simple_target_message_margin

        private var listener: OnStateChangedListener? = null

        override fun self(): Builder = this

        fun setTitle(title: CharSequence): Builder = apply { this.title = title }

        fun setDescription(description: CharSequence): Builder = apply { this.description = description }

        fun setListener(listener: OnStateChangedListener): Builder = apply { this.listener = listener }

        fun setTopCaretDrawableResId(@DrawableRes topCaretDrawableResId: Int) = apply { this.topCaretDrawableResId = topCaretDrawableResId }

        fun setBottomCaretDrawableResId(@DrawableRes bottomCaretDrawableResId: Int) = apply { this.bottomCaretDrawableResId = bottomCaretDrawableResId }

        fun setMessageAnimationDuration(duration: Long) = apply { this.messageAnimationDuration = duration }

        fun setTextColorResId(@ColorRes textColorResId: Int) = apply { this.textColorResId = textColorResId }

        fun setTitleDimenResId(@DimenRes titleDimenResId: Int) = apply { this.titleDimenResId = titleDimenResId }

        fun setDescriptionDimenResId(@DimenRes descriptionDimenResId: Int) = apply { this.descriptionDimenResId = descriptionDimenResId }

        fun setMessageMargin(@DimenRes messageMarginResId: Int) {
            this.messageMarginLeftResId = messageMarginResId
            this.messageMarginTopResId = messageMarginResId
            this.messageMarginRightResId = messageMarginResId
            this.messageMarginBottomResId = messageMarginResId
        }

        fun setMessageMarginHorizontal(@DimenRes messageMarginResId: Int) {
            this.messageMarginLeftResId = messageMarginResId
            this.messageMarginRightResId = messageMarginResId
        }

        fun setMessageMarginVertical(@DimenRes messageMarginResId: Int) {
            this.messageMarginTopResId = messageMarginResId
            this.messageMarginBottomResId = messageMarginResId
        }

        override fun build(): SimpleTarget {
            val activity = activityWeakReference.get()
            if (activity == null) {
                throw IllegalStateException("activity is null")
            } else {
                val targetView = viewWeakReference?.get()

                val messageView = activity.layoutInflater.inflate(R.layout.layout_simple, null).apply {
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

                    // Modify margin
                    setPadding(
                            resources.getDimension(messageMarginLeftResId).toInt(),
                            resources.getDimension(messageMarginTopResId).toInt(),
                            resources.getDimension(messageMarginRightResId).toInt(),
                            resources.getDimension(messageMarginBottomResId).toInt()
                    )

                    // Set texts
                    (findViewById<TextView>(R.id.title)).apply {
                        text = title
                        setTextColor(ContextCompat.getColor(context, textColorResId))
                        if (titleDimenResId > 0) setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(titleDimenResId))
                    }
                    (findViewById<TextView>(R.id.description)).apply {
                        text = description
                        setTextColor(ContextCompat.getColor(context, textColorResId))
                        if (descriptionDimenResId > 0) setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(descriptionDimenResId))
                    }

                    // Modify message
                    findViewById<View>(R.id.message).setBackgroundResource(messageBgDrawableResId)

                    findViewById<ImageView>(R.id.top_caret).apply {
                        setImageResource(topCaretDrawableResId)
                    }
                    findViewById<ImageView>(R.id.bottom_caret).apply {
                        setImageResource(bottomCaretDrawableResId)
                    }
                }

                return SimpleTarget(
                        left = left,
                        top = top,
                        width = width,
                        height = height,
                        radius = radius,
                        highlightPaddingLeft = paddingLeft,
                        highlightPaddingTop = paddingTop,
                        highlightPaddingRight = paddingRight,
                        highlightPaddingBottom = paddingBottom,
                        messageView = messageView,
                        targetView = targetView,
                        delay = startDelayMillis,
                        messageAnimationDuration = messageAnimationDuration,
                        listener = listener
                )
            }
        }
    }

}