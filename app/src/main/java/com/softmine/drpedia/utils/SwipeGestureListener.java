package com.softmine.drpedia.utils;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private BottomSheetBehavior bottomSheet;

    public enum Direction{
        TOP,DOWN,LEFT,RIGHT
    }

    public SwipeGestureListener(BottomSheetBehavior bt) {
        bottomSheet = bt;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


        Log.d("gesture","e2 action "+e2.getAction());
        switch (getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case TOP:
                bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            case LEFT:
                return true;
            case DOWN:
                if(bottomSheet.getState()==BottomSheetBehavior.STATE_COLLAPSED || bottomSheet.getState()==BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                return true;
            case RIGHT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("gesture","onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    private Direction getDirection(float x1, float y1, float x2, float y2) {

        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));

        if (angle > 0 && angle <= 135)
            return Direction.TOP;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            return Direction.LEFT;
        if (angle < -45 && angle>= -135)
            return Direction.DOWN;
        if (angle > -45 && angle <= 45)
            return Direction.RIGHT;

        return null;     // required by java to avoid error
    }
}