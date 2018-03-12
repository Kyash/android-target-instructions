package co.kyash.spotinstructions

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView

class SimpleSpot(
        private val point: PointF,
        private val radius: Float,
        private val view: View
) : Spot {

    override fun getPoint(): PointF = point

    override fun getRadius(): Float = radius

    override fun getView(): View = view

    class Builder(context: Activity) : AbstractBuilder<Builder, SimpleSpot>(context) {

        private var title: CharSequence? = null
        private var description: CharSequence? = null

        override fun self(): Builder {
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: CharSequence): Builder {
            this.description = description
            return this
        }

        override fun build(): SimpleSpot {
            val activity = activityWeakReference.get()
            if (activity != null) {
                val view = activity.getLayoutInflater().inflate(R.layout.layout_simple, null)
                (view.findViewById(R.id.title) as TextView).text = title
                (view.findViewById(R.id.description) as TextView).text = description
                val point = PointF(startX, startY)
                calculatePosition(point, radius, view)
                return SimpleSpot(point, radius, view)
            } else {
                throw IllegalStateException("Activity is null")
            }
        }

        private fun calculatePosition(point: PointF, radius: Float, view: View) {
            val areas = FloatArray(2)
            val screenSize = Point()
            (view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(screenSize)

            areas[ABOVE_SPOTLIGHT] = point.y / screenSize.y
            areas[BELOW_SPOTLIGHT] = (screenSize.y - point.y) / screenSize.y

            val largest: Int = if (areas[ABOVE_SPOTLIGHT] > areas[BELOW_SPOTLIGHT]) {
                ABOVE_SPOTLIGHT
            } else {
                BELOW_SPOTLIGHT
            }

            val layout = view.findViewById<LinearLayout>(R.id.container)
            layout.setPadding(100, 0, 100, 0)
            when (largest) {
                ABOVE_SPOTLIGHT -> view.viewTreeObserver.addOnGlobalLayoutListener {
                    layout.y = point.y - radius - 100f - layout.height.toFloat()
                }
                BELOW_SPOTLIGHT -> layout.y = (point.y + radius + 100f).toInt().toFloat()
            }
        }

        companion object {

            private val ABOVE_SPOTLIGHT = 0
            private val BELOW_SPOTLIGHT = 1
        }
    }
}