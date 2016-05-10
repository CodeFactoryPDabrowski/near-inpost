package com.codefactory.przemyslawdabrowski.nearinpost.view.custom.bottom_slide_layout

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.codefactory.przemyslawdabrowski.nearinpost.R

/**
 * Layout that can slide down all content.
 */
class BottomSlideLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
: FrameLayout(context, attrs, defStyleAttr) {

    /**
     * Rate od change of BottomSlideLayout position with respect to time.
     */
    val FLING_VELOCITY: Int

    //Configurable attributes
    /**
     * Value determinate if layout has nested scrolling child.
     */
    private var hasScrollingChild = false

    /**
     * Id of nested scrolling child.
     */
    private var scrollingChildId = -1

    /**
     * Distance of possible drag after reached, View is dismissed.
     */
    private var dragDismissDistance = Int.MAX_VALUE

    // child views & helpers

    /**
     * Child view that can be slide down.
     */
    private var dragView: View? = null

    /**
     * Possible scrolling nested child.
     */
    private var scrollingChild: View? = null

    /**
     * Helper for drag operations on views.
     */
    lateinit private var viewDragHelper: ViewDragHelper

    /**
     * Helper for calculating child view offset.
     */
    lateinit private var dragViewOffsetHelper: ViewOffsetHelper

    // state
    /**
     * List of listeners to communicate with this view.
     */
    private var listeners: List<Listener> = emptyList()

    /**
     * Vale determinate if view is dismissing.
     */
    private var isDismissing: Boolean = false

    /**
     * Left position of view relative to parent.
     */
    private var dragViewLeft: Int = 0

    /**
     * Top position of view relative to parent.
     */
    private var dragViewTop: Int = 0

    /**
     * Bottom position of view relative to parent.
     */
    private var dragViewBottom: Int = 0

    /**
     * Value determinate if nested scrolling child scroll was consumed.
     */
    private var lastNestedScrollWasDownward: Boolean = false

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        FLING_VELOCITY = ViewConfiguration.get(context).scaledMinimumFlingVelocity
        val typedArray: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomSlideLayout, 0, 0)
        if (typedArray.hasValue(R.styleable.BottomSlideLayout_scrollingChild)) {
            hasScrollingChild = true
            scrollingChildId = typedArray.getResourceId(R.styleable.BottomSlideLayout_scrollingChild, scrollingChildId)
        }
        if (typedArray.hasValue(R.styleable.BottomSlideLayout_dragDismissDistance)) {
            dragDismissDistance = typedArray.getDimensionPixelSize(R.styleable.BottomSlideLayout_dragDismissDistance, dragDismissDistance)
        }
        typedArray.recycle()
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (dragView != null) {
            throw UnsupportedOperationException("BottomSheet must only have 1 child view")
        }
        dragView = child;
        if (dragView != null) {
            dragViewOffsetHelper = ViewOffsetHelper(dragView as View)
            if (hasScrollingChild) {
                scrollingChild = (dragView as View).findViewById(scrollingChildId)
            }
        }
        super.addView(child, index, params)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewDragHelper = ViewDragHelper.create(this, dragHelperCallbacks)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (dragView != null && ViewCompat.isLaidOut(dragView as View)) {
            dragViewLeft = (dragView as View).left
            dragViewTop = (dragView as View).top
            dragViewBottom = (dragView as View).bottom
            dragViewOffsetHelper.onViewLayout()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel()
            return false
        }
        if (ev != null) {
            return isDraggableViewUnder(ev.x.toInt(), ev.y.toInt())
                    && (viewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev))
        } else {
            return super.onInterceptTouchEvent(ev)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        viewDragHelper.processTouchEvent(event)
        if (viewDragHelper.capturedView == null) {
            return super.onTouchEvent(event)
        }
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return (nestedScrollAxes != 0 && View.SCROLL_AXIS_VERTICAL != 0)
    }

    override fun onNestedScroll(target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        // if scrolling downward, use any unconsumed (i.e. not used by the scrolling child)
        // to drag the sheet downward
        lastNestedScrollWasDownward = dyUnconsumed < 0
        if (lastNestedScrollWasDownward) {
            dragView?.offsetTopAndBottom(-dyUnconsumed)
        }
    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        // if scrolling upward & the sheet has been dragged downward
        // then drag back into place before allowing scrolls
        if (dy > 0 && dragView != null) {
            val dragDisplacement = (dragView as View).top - dragViewTop
            if (dragDisplacement > 0) {
                val consume = Math.min(dragDisplacement, dy)
                (dragView as View).offsetTopAndBottom(-consume)
                consumed?.let {
                    consumed[1] = consume
                }
                lastNestedScrollWasDownward = false
            }
        }
    }

    override fun onStopNestedScroll(child: View?) {
        if (dragView == null) {
            return
        }
        val dragDisplacement = (dragView as View).top - dragViewTop
        if (dragDisplacement == 0) return

        // check if we should perform a dismiss or settle back into place
        val dismiss = lastNestedScrollWasDownward && dragDisplacement >= dragDismissDistance
        // animate either back into place or to bottom
        var settleAnim = ObjectAnimator.ofInt(dragViewOffsetHelper,
                ViewOffsetHelper.OFFSET_Y,
                (dragView as View).top,
                if (dismiss) dragViewBottom else dragViewTop)

        settleAnim.duration = 200L
        settleAnim.interpolator = AnimationUtils.loadInterpolator(context,
                android.R.interpolator.fast_out_slow_in);
        if (dismiss) {
            settleAnim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    dispatchDismissCallback()
                }
            })
        }
        settleAnim.start();
    }

    /**
     * Add view listeners.
     * @param listener New listener to communicate with view.
     */
    fun addListener(listener: Listener) {
        listeners += listener
    }

    /**
     * Dismiss child view.
     */
    fun doDismiss() {
        viewDragHelper.settleCapturedViewAt(dragViewLeft, dragViewBottom)
    }

    /**
     * Dispatch drag callback to all listeners.
     */
    protected fun dispatchDragCallback() {
        if (listeners.size > 0) {
            for (listener in listeners) {
                listener.onDrag(top)
            }
        }
    }

    /**
     * Dispatch dismiss callback to all listeners.
     */
    protected fun dispatchDismissCallback() {
        if (listeners.size > 0) {
            for (listener in listeners) {
                listener.onDragDismissed()
            }
        }
    }

    /**
     * Check if draggable view is under this view.
     */
    private fun isDraggableViewUnder(x: Int, y: Int): Boolean {
        return visibility == VISIBLE && viewDragHelper.isViewUnder(this, x, y)
    }

    /**
     * Callback for drag operations on view.
     */
    private var dragHelperCallbacks: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
            // if we have a scrolling child and it can scroll then don't drag, it'll be handled
            // by nested scrolling
            var childCanScroll: Boolean = scrollingChild != null
                    && ((scrollingChild as View).canScrollVertically(1)
                    || (scrollingChild as View).canScrollVertically(-1))

            return !childCanScroll
        }

        override fun clampViewPositionVertical(child: View?, top: Int, dy: Int): Int {
            return Math.min(Math.max(top, dragViewTop), dragViewBottom)
        }

        override fun clampViewPositionHorizontal(child: View?, left: Int, dx: Int): Int {
            return dragViewLeft
        }

        override fun getViewVerticalDragRange(child: View?): Int {
            return dragViewBottom - dragViewTop
        }

        override fun onViewPositionChanged(changedView: View?, left: Int, top: Int, dx: Int, dy: Int) {
            dispatchDragCallback()
        }

        override fun onViewReleased(releasedChild: View?, xvel: Float, yvel: Float) {
            if (yvel >= FLING_VELOCITY) {
                isDismissing = true
                doDismiss();
            } else {
                // settle back into position
                viewDragHelper.settleCapturedViewAt(dragViewLeft, dragViewTop)
            }
            ViewCompat.postInvalidateOnAnimation(this@BottomSlideLayout)
        }

        override fun onViewDragStateChanged(state: Int) {
            if (isDismissing && state == ViewDragHelper.STATE_IDLE) {
                isDismissing = false
                dispatchDismissCallback()
            }
        }
    }

    /**
     * Listener to communicate with parent component.
     */
    interface Listener {

        /**
         * View dismissed operation.
         */
        fun onDragDismissed()

        /**
         * Drag view.
         * @param top Top position of view.
         */
        fun onDrag(top: Int)
    }
}