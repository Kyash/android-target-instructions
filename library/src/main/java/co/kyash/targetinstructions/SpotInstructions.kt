package co.kyash.spotinstructions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.app.Activity
import android.support.annotation.ColorInt
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import co.kyash.targetinstructions.InstructionsView
import java.lang.ref.WeakReference

class SpotInstructions private constructor(activity: Activity) {

    companion object {
        private val INSTRUCTIONS_DURATION = 300L

        @ColorInt
        private val DEFAULT_OVERLAY_COLOR = R.color.default_cover
        private val DEFAULT_DURATION = 500L
        private val DEFAULT_ANIMATION: TimeInterpolator = DecelerateInterpolator()

        fun with(activity: Activity) = SpotInstructions(activity)
    }

    private lateinit var instructionsViewWeakReference: WeakReference<InstructionsView>
    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)

    private var spots = ArrayList<Spot>()
    private var duration = DEFAULT_DURATION
    private var animation = DEFAULT_ANIMATION
    private var overlayColor = DEFAULT_OVERLAY_COLOR

    fun setSpots(spots: List<Spot>): SpotInstructions {
        this.spots.clear()
        this.spots.addAll(spots)
        return this
    }

    fun setOverlayColor(@ColorInt overlayColor: Int) = apply { this.overlayColor = overlayColor }

    fun setDuration(duration: Long) = apply { this.duration = duration }

    fun setAnimation(animation: TimeInterpolator) = apply { this.animation = animation }

    fun start() {
        val activity = activityWeakReference.get() ?: return

        val decorView = activity.window.decorView
        val instructionsView = InstructionsView(activity).apply {
            this@apply.overlayColor = overlayColor
            this@apply.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            this@apply.listener = object : InstructionsView.OnStateChangedListener {
                override fun onClosed() {
                    if (spots.isNotEmpty()) {
                        val spot = spots.removeAt(0)
                        if (spots.size > 0) {
                            showSpot()
                        } else {
                            finishInstruction()
                        }
                    }
                }

                override fun onClicked() {
                    hideSpot()
                }
            }
        }
        instructionsViewWeakReference = WeakReference(instructionsView)
        (decorView as ViewGroup).addView(instructionsView)
        startInstruction()
    }

    private fun showSpot() {
        if (spots.isNotEmpty()) {
            val spot = spots[0]
            instructionsViewWeakReference.get().let {
                it?.removeAllViews()
                it?.addView(spot.getView())
                it?.turnUp(spot, duration, animation)
            }
        }
    }

    private fun hideSpot() {
        if (spots.isNotEmpty()) {
            val spot = spots[0]
            instructionsViewWeakReference.get()?.turnDown(spot.getRadius(), duration, animation)
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
                    showSpot()
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
                    // TODO
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