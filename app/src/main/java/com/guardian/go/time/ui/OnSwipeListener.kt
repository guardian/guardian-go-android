package com.guardian.go.time.ui

import android.view.GestureDetector
import android.view.MotionEvent


abstract class OnSwipeListener : GestureDetector.SimpleOnGestureListener() {

    private val SWIPE_MIN_DISTANCE = 120
    private val SWIPE_MAX_OFF_PATH = 250
    private val SWIPE_THRESHOLD_VELOCITY = 200

    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float,
        velocityY: Float
    ): Boolean {
        try {
            if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH) {
                return false
            }
            // right to left swipe
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onLeftSwipe(e1.x - e2.x)
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onRightSwipe(e2.x - e1.x)
            }// left to right swipe
        } catch (e: Exception) {

        }

        return false
    }

    abstract fun onLeftSwipe(distance: Float)

    abstract fun onRightSwipe(distance: Float)
}