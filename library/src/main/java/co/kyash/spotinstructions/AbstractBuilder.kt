package co.kyash.spotinstructions

import android.app.Activity
import android.graphics.PointF
import android.view.View
import java.lang.ref.WeakReference

abstract class AbstractBuilder<T : AbstractBuilder<T, S>, S : Spot>(
        activity: Activity
) {

    val activityWeakReference: WeakReference<Activity> = WeakReference(activity)

    var startX = 0f
    var startY = 0f
    var radius = 100f

    abstract fun self(): T

    abstract fun build(): S

    fun setPoint(x: Float, y: Float): T {
        this.startX = x
        this.startY = y
        return self()
    }

    fun setPoint(point: PointF): T {
        return setPoint(point.x, point.y)
    }

    fun setPoint(view: View): T {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val x = location[0] + view.width / 2
        val y = location[1] + view.height / 2
        return setPoint(x.toFloat(), y.toFloat())
    }

    fun setRadius(radius: Float): T {
        if (radius <= 0) {
            throw IllegalArgumentException("radius must be greater than 0")
        }
        this.radius = radius
        return self()
    }

}