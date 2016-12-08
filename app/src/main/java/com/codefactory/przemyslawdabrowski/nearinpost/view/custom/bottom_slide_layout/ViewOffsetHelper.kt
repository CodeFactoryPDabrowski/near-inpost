package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.bottom_slide_layout

import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.Property
import android.view.View
import android.view.ViewParent

class ViewOffsetHelper(view: View) {

    /**
     * View to operate on.
     */
    val mView: View

    /**
     * Top position of view.
     */
    var mLayoutTop: Int = 0

    /**
     * Left position of view.
     */
    var mLayoutLeft: Int = 0

    /**
     * Top offset of view.
     */
    var mOffsetTop: Int = 0

    /**
     * Left offset of view.
     */
    var mOffsetLeft: Int = 0

    init {
        mView = view
    }

    fun onViewLayout() {
        //Grab intended top
        mLayoutTop = mView.top
        mLayoutLeft = mView.left

        updateOffsets()
    }

    /**
     * Set the top and bottom offset for this {@link ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    fun setTopAndBottomOffset(offset: Int): Boolean {
        if (mOffsetTop != offset) {
            mOffsetTop = offset
            updateOffsets()
            return true
        }
        return false
    }

    fun getTopAndBottomOffset() = mOffsetTop

    /**
     * Update offsets of mView.
     */
    private fun updateOffsets() {
        // Translate view positions.
        fun tickleInvalidationFlag(view: View) {
            val x: Float = ViewCompat.getTranslationX(view)
            ViewCompat.setTranslationY(view, x + 1)
            ViewCompat.setTranslationY(view, x)
        }

        ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.top - mLayoutTop))
        ViewCompat.offsetLeftAndRight(mView, mOffsetLeft - (mView.left - mLayoutLeft))
        // Manually invalidate the view and parent to make sure we get drawn pre-M
        if (Build.VERSION.SDK_INT < 23) {
            tickleInvalidationFlag(mView)
            val vp: ViewParent = mView.parent
            if (vp is View) {
                tickleInvalidationFlag(vp)
            }
        }
    }

    companion object {

        /**
         * Y Offset property changes.
         */
        val OFFSET_Y: Property<ViewOffsetHelper, Int> = object : IntProperty<ViewOffsetHelper>("TopAndBottomOffset") {

            override fun get(`object`: ViewOffsetHelper?): Int? {
                if (`object` != null) {
                    return `object`.getTopAndBottomOffset()
                } else {
                    return null
                }
            }

            override fun setValue(obj: ViewOffsetHelper, value: Int) {
                obj.setTopAndBottomOffset(value)
            }

        }
    }


    /**
     * Class for handle int property changes.
     */
    abstract class IntProperty<T>(name: String) : Property<T, Int>(Int::class.java, name) {

        /**
         * Set bew value property for object.
         * @param obj Object to set value.
         * @param value Value to set.
         */
        abstract fun setValue(obj: T, value: Int)

        override fun set(`object`: T, value: Int?) {
            if (value != null) {
                setValue(`object`, value)
            }
        }
    }
}