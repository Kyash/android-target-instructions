package co.kyash.targetinstructions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.app.Activity
import android.support.annotation.ColorInt
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import co.kyash.targetinstructions.targets.Target
import java.lang.ref.WeakReference

class TargetInstructions private constructor(
        activity: Activity
) {

    companion object {
        private val INSTRUCTIONS_DURATION = 300L

        @ColorInt
        private val DEFAULT_OVERLAY_COLOR = R.color.default_cover
        private val DEFAULT_FADE_DURATION = 500L
        private val DEFAULT_FADE_INTERPOLATOR: TimeInterpolator = DecelerateInterpolator()

        fun with(activity: Activity) = TargetInstructions(activity)
    }

    private val instructionsViewWeakReference: WeakReference<InstructionsView>
    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)

    private var targets = ArrayList<Target>()
    private var fadeDuration = DEFAULT_FADE_DURATION
    private var fadeInterpolator = DEFAULT_FADE_INTERPOLATOR
    private var overlayColor = DEFAULT_OVERLAY_COLOR

    init {
        val decorView = activity.window.decorView
        val instructionsView = InstructionsView(activity).apply {
            this@apply.overlayColor = overlayColor
            this@apply.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            this@apply.listener = object : InstructionsView.OnStateChangedListener {
                override fun onTargetClosed() {
                    if (targets.isNotEmpty()) {
                        targets.removeAt(0)
                        if (targets.isNotEmpty()) {
                            showNextTarget()
                        } else {
                            finishInstruction()
                        }
                    }
                }

                override fun onTargetClicked() {
                    hideCurrentTarget()
                }
            }
        }
        instructionsViewWeakReference = WeakReference(instructionsView)
        (decorView as ViewGroup).addView(instructionsView)
    }

    fun setTargets(targets: List<Target>): TargetInstructions = apply {
        this.targets.clear()
        this.targets.addAll(targets)
    }

    fun setOverlayColor(@ColorInt overlayColor: Int) = apply { this.overlayColor = overlayColor }

    fun setFadeDuration(fadeDuration: Long) = apply { this.fadeDuration = fadeDuration }

    fun setFadeInterpolator(fadeInterpolator: TimeInterpolator) = apply { this.fadeInterpolator = fadeInterpolator }

    fun start() {
        startInstruction()
    }

    fun finish() {
        val activity = activityWeakReference.get() ?: return
        val decorView = activity.window.decorView
        (decorView as ViewGroup).removeView(instructionsViewWeakReference.get())
    }

    private fun showNextTarget() {
        if (targets.isNotEmpty()) {
            instructionsViewWeakReference.get()?.showTarget(targets[0])
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
                    finish()
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

}