package co.kyash.targetinstructions

import android.app.Activity
import android.support.annotation.DimenRes
import android.view.View
import co.kyash.targetinstructions.targets.Target
import java.lang.ref.WeakReference

abstract class AbstractTargetBuilder<T : AbstractTargetBuilder<T, S>, S : Target>(
        activity: Activity
) {

    val activityWeakReference: WeakReference<Activity> = WeakReference(activity)

    var left = 0f
    var top = 0f
    var width = 0f
    var height = 0f

    var radius = activity.resources.getDimension(R.dimen.default_target_radius)
    var padding = activity.resources.getDimension(R.dimen.default_target_padding)

    abstract fun self(): T

    abstract fun build(): S

    fun setPoint(left: Float, top: Float, width: Float, height: Float): T {
        this.left = left
        this.top = top
        this.width = width
        this.height = height
        return self()
    }

    fun setPoint(view: View): T {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return setPoint(location[0].toFloat(), location[1].toFloat(), view.width.toFloat(), view.height.toFloat())
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

    fun setPadding(padding: Float): T {
        this.padding = padding
        return self()
    }

    fun setPadding(@DimenRes paddingResId: Int): T {
        val activity = activityWeakReference.get()
        return if (activity == null) {
            throw IllegalStateException("activity is null")
        } else {
            setPadding(activity.resources.getDimension(paddingResId))
        }
    }

}