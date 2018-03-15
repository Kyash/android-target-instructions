package co.kyash.targetinstructions

import android.app.Activity
import android.support.annotation.DimenRes
import android.view.View
import co.kyash.targetinstructions.targets.Target
import java.lang.ref.WeakReference

abstract class AbstractTargetBuilder<T : AbstractTargetBuilder<T, S>, S : Target>(
        activity: Activity
) {

    internal val activityWeakReference: WeakReference<Activity> = WeakReference(activity)
    internal var viewWeakReference: WeakReference<View>? = null

    internal var left = 0f
    internal var top = 0f
    internal var width = 0f
    internal var height = 0f

    internal var radius = 0f

    internal var hightlightPaddingLeft = 0f
    internal var highlightPaddingTop = 0f
    internal var highlightPaddingRight = 0f
    internal var highlightPaddingBottom = 0f

    internal var startDelayMillis = 0L

    internal abstract fun self(): T

    abstract fun build(): S

    fun setCoordinate(left: Float, top: Float, width: Float, height: Float): T {
        this.left = left
        this.top = top
        this.width = width
        this.height = height
        return self()
    }

    fun setCoordinate(view: View): T {
        viewWeakReference = WeakReference(view)
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return setCoordinate(location[0].toFloat(), location[1].toFloat(), view.width.toFloat(), view.height.toFloat())
    }

    fun setRadius(radius: Float): T {
        this.radius = radius
        return self()
    }

    fun setRadius(@DimenRes radiusResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setRadius(activity.resources.getDimension(radiusResId))
        }
    }

    fun setStartDelayMillis(startDelayMillis: Long): T {
        this.startDelayMillis = startDelayMillis
        return self()
    }

    fun setHighlightPadding(padding: Float): T {
        this.hightlightPaddingLeft = padding
        this.highlightPaddingTop = padding
        this.highlightPaddingRight = padding
        this.highlightPaddingBottom = padding
        return self()
    }

    fun setHighlightPadding(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightPadding(activity.resources.getDimension(paddingResId))
        }
    }

    fun setHighlightVerticalPadding(padding: Float): T {
        this.highlightPaddingTop = padding
        this.highlightPaddingBottom = padding
        return self()
    }

    fun setHighlightVerticalPadding(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightVerticalPadding(activity.resources.getDimension(paddingResId))
        }
    }

    fun setHighlightHorizontalPadding(padding: Float): T {
        this.hightlightPaddingLeft = padding
        this.highlightPaddingRight = padding
        return self()
    }

    fun setHighlightHorizontalPadding(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightHorizontalPadding(activity.resources.getDimension(paddingResId))
        }
    }

}