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

    internal var highlightRadius = 0f

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

    fun setHighlightRadius(radius: Float): T {
        this.highlightRadius = radius
        return self()
    }

    fun setHighlightRadius(@DimenRes radiusResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightRadius(activity.resources.getDimension(radiusResId))
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

    fun setHighlightPaddingLeft(padding: Float): T {
        this.hightlightPaddingLeft = padding
        return self()
    }

    fun setHighlightPaddingLeft(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightPaddingLeft(activity.resources.getDimension(paddingResId))
        }
    }

    fun setHighlightPaddingTop(padding: Float): T {
        this.highlightPaddingTop = padding
        return self()
    }

    fun setHighlightPaddingTop(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightPaddingTop(activity.resources.getDimension(paddingResId))
        }
    }

    fun setHighlightPaddingRight(padding: Float): T {
        this.highlightPaddingRight = padding
        return self()
    }

    fun setHighlightPaddingRight(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightPaddingRight(activity.resources.getDimension(paddingResId))
        }
    }

    fun setHighlightPaddingBottom(padding: Float): T {
        this.highlightPaddingBottom = padding
        return self()
    }

    fun setHighlightPaddingBottom(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setHighlightPaddingBottom(activity.resources.getDimension(paddingResId))
        }
    }

}