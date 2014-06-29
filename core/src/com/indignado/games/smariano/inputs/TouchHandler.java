package com.indignado.games.smariano.inputs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class TouchHandler {
    public int TouchStartX;
    public int TouchStartY;
    public int TouchEndX;
    public int TouchEndY;
    public int CurrentX;
    public int CurrentY;

    public TouchHandler(){touchState=NO_TOUCH;}

    public int touchState;

    public static final int NO_TOUCH=0;
    public static final int TOUCH_START=1;
    public static final int TOUCH_DRAG=2;
    public static final int TOUCH_END=3;

    public float SwipeDistanceSquare;
    public int swipeYDirection;
    public int swipeXDirection;
    public int swipeYDistance;

    public final int LEFT=1;
    public final int RIGHT=2;
    public final int UP=3;
    public final int DOWN=4;
    public Vector3 TouchPoint;

    public void StartTouch(int x,int y)
    {
        TouchStartX=x;
        TouchStartY=y;
        CurrentX=x;
        CurrentY=y;
        touchState=TOUCH_START;
    }

    public void DragTouch(int x,int y)
    {
        TouchEndX=x;
        TouchEndY=y;
        CurrentX=x;
        CurrentY=y;
        UpdateDirection();
        touchState=TOUCH_DRAG;
    }

    public void EndTouch(int x,int y)
    {
        DragTouch(x, y);
        touchState=TOUCH_END;
    }

    void UpdateDirection()
    {
        if(TouchEndX>TouchStartX)
            swipeXDirection=LEFT;
        else
            swipeXDirection=RIGHT;
        if(TouchEndY>TouchStartY)
            swipeXDirection=UP;
        else
            swipeXDirection=DOWN;
        swipeYDistance=TouchEndY-TouchStartY;
    }

    public void UpdateTouch(int x,int y,boolean IsTouching)
    {
        if(touchState==TOUCH_END)
            touchState=NO_TOUCH;
        if(!IsTouching)
        {
            if(touchState==TOUCH_START || touchState==TOUCH_DRAG)
                EndTouch(x, y);
        }else{
            if(touchState==NO_TOUCH)
            {
                StartTouch(x,y);
            }else if(touchState==TOUCH_START ||
                    touchState ==TOUCH_DRAG)
            {
                DragTouch(x, y);
            }
        }

    }

    public void UnProjectCamera(int x,int y,OrthographicCamera cam,float startX,float startY,float width,float height)
    {
        TouchPoint.set(x,y,0);
        cam.unproject(TouchPoint,startX,startY,width,height);
    }

}