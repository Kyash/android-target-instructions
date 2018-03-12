package co.kyash.spotinstructions

import android.graphics.PointF
import android.view.View

interface Spot {

    fun getPoint(): PointF

    fun getRadius(): Float

    fun getView(): View

}