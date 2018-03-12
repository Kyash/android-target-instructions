package co.kyash.spotinstructions

import android.view.View

interface Spot {

    fun getLeft(): Float

    fun getTop(): Float

    fun getWidth(): Float

    fun getHeight(): Float

    fun getRadius(): Float

    fun getPadding(): Float

    fun getView(): View

}