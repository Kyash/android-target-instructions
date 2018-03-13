package co.kyash.targetinstructions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.app.Activity
import android.support.annotation.ColorInt
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import java.lang.ref.WeakReference

class TargetInstructions private constructor(
        activity: Activity
) {

    companion object {
        private val INSTRUCTIONS_DURATION = 300L

        @ColorInt
        private val DEFAULT_OVERLAY_COLOR = R.color.default_cover
        private val DEFAULT_DURATION = 500L
        private val DEFAULT_INTERPOLATOR: TimeInterpolator = DecelerateInterpolator()

        fun with(activity: Activity) = TargetInstructions(activity)
    }

    private lateinit var instructionsViewWeakReference: WeakReference<InstructionsView>
    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)

    private var targets = ArrayList<Target>()
    private var duration = DEFAULT_DURATION
    private var interpolator = DEFAULT_INTERPOLATOR
    private var overlayColor = DEFAULT_OVERLAY_COLOR

    fun setTargets(targets: List<Target>): TargetInstructions {
        this.targets.clear()
        this.targets.addAll(targets)
        return this
    }

    fun setOverlayColor(@ColorInt overlayColor: Int) = apply { this.overlayColor = overlayColor }

    fun setDuration(duration: Long) = apply { this.duration = duration }

    fun setInterpolator(interpolator: TimeInterpolator) = apply { this.interpolator = interpolator }

    fun start() {
        val activity = activityWeakReference.get() ?: return

        val decorView = activity.window.decorView
        val instructionsView = InstructionsView(activity).apply {
            this@apply.overlayColor = overlayColor
            this@apply.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            this@apply.listener = object : InstructionsView.OnStateChangedListener {
                override fun onClosed() {
                    if (targets.isNotEmpty()) {
                        val target = targets.removeAt(0)
                        if (targets.size > 0) {
                            showNextTarget()
                        } else {
                            finishInstruction()
                        }
                    }
                }

                override fun onClicked() {
                    hideCurrentTarget()
                }
            }
        }
        instructionsViewWeakReference = WeakReference(instructionsView)
        (decorView as ViewGroup).addView(instructionsView)
        startInstruction()
    }

    private fun showNextTarget() {
        if (targets.isNotEmpty()) {
            val target = targets[0]
            instructionsViewWeakReference.get()?.showTarget(target)
        }
    }

    private fun hideCurrentTarget() {
        if (targets.isNotEmpty()) {
            val target = targets[0]
            instructionsViewWeakReference.get()?.hideTarget(target)
        }
    }

    private fun startInstruction() {
        ObjectAnimator.ofFloat(instructionsViewWeakReference.get(), "alpha", 0f, 1f).let {
            it.duration = INSTRUCTIONS_DURATION
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                    //
                }

                override fun onAnimationEnd(animation: Animator?) {
                    showNextTarget()
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

    private fun finishInstruction() {
        ObjectAnimator.ofFloat(instructionsViewWeakReference.get(), "alpha", 1f, 0f).let {
            it.duration = INSTRUCTIONS_DURATION
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                    //
                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {
                    //
                }

                override fun onAnimationStart(animation: Animator?) {
                    // TODO
                }

            })
            it.start()
        }
    }

}