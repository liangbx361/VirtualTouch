package android.view;

import android.app.IActivityManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.IWindowManager;
import android.view.KeyEvent;

public class InjectMotionEvent extends InjectKeyEvent{
	private long mDownTime = -1;
    private long mEventTime = -1;    
    private int mAction = -1;
    private float mX = -1;
    private float mY = -1;
    private float mPressure = -1;
    private float mSize = -1;
    private int mMetaState = -1;
    private float mXPrecision = -1;
    private float mYPrecision = -1;
    private int mDeviceId = -1;
    private int mEdgeFlags = -1;
    
    //If true, this is an intermediate step (more verbose logging, only)
    private boolean mIntermediateNote; 
    
    public InjectMotionEvent(int type, long downAt, int action, 
            float x, float y, int metaState) {
        super(type);
        mDownTime = downAt;
        mAction = action;
        mX = x;
        mY = y;
        mMetaState = metaState;
    }
    
    public InjectMotionEvent(int type, long downTime, long eventTime, int action,
            float x, float y, float pressure, float size, int metaState,
            float xPrecision, float yPrecision, int deviceId, int edgeFlags) {
        super(type);
        mDownTime = downTime;
        mEventTime = eventTime;
        mAction = action;
        mX = x;
        mY = y;
        mPressure = pressure;
        mSize = size;
        mMetaState = metaState;
        mXPrecision = xPrecision;
        mYPrecision = yPrecision;
        mDeviceId = deviceId;
        mEdgeFlags = edgeFlags;
    }  
    
    public void setIntermediateNote(boolean b) {
        mIntermediateNote = b;
    }
    
    public boolean getIntermediateNote() {
        return mIntermediateNote;
    }
    
    public float getX() {
        return mX;
    }
    
    public float getY() {
        return mY;
    }
    
    public int getAction() {
        return mAction;
    }
    
    public long getDownTime() {
        return mDownTime;
    }
    
    public long getEventTime() {
        return mEventTime;
    }
    
    public void setDownTime(long downTime) {
        mDownTime = downTime;
    }
    
    public void setEventTime(long eventTime) {
        mEventTime = eventTime;
    }
    
    /**
     * 
     * @return instance of a motion event
     */
    private MotionEvent getEvent() {
        if (mDeviceId < 0) {
            return MotionEvent.obtain(mDownTime, SystemClock.uptimeMillis(), 
                mAction, mX, mY, mMetaState);
        }
        
        // for scripts
        return MotionEvent.obtain(mDownTime, mEventTime, 
                mAction, mX, mY, mPressure, mSize, mMetaState,
                mXPrecision, mYPrecision, mDeviceId, mEdgeFlags);
    }
    
    @Override
    public boolean isThrottlable() {
        return (getAction() == KeyEvent.ACTION_UP);
    }

//	@Override
//	public int injectEvent(IWindowManager iwm, IActivityManager iam, int verbose) {
//        MotionEvent me = getEvent();
//        if ((verbose > 0 && !mIntermediateNote) || verbose > 1) {
//            StringBuilder msg = new StringBuilder(":Sending ");
//            msg.append(" (");
//            switch (me.getActionMasked()) {
//                case MotionEvent.ACTION_DOWN:
//                    msg.append("ACTION_DOWN");
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    msg.append("ACTION_MOVE");
//                    break;
//                case MotionEvent.ACTION_UP:
//                    msg.append("ACTION_UP");
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                    msg.append("ACTION_CANCEL");
//                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    msg.append("ACTION_POINTER_DOWN ").append(me.getPointerId(me.getActionIndex()));
//                    break;
//                case MotionEvent.ACTION_POINTER_UP:
//                    msg.append("ACTION_POINTER_UP ").append(me.getPointerId(me.getActionIndex()));
//                    break;
//                default:
//                    msg.append(me.getAction());
//                    break;
//            }
//            msg.append("):");
//
//            int pointerCount = me.getPointerCount();
//            for (int i = 0; i < pointerCount; i++) {
//                msg.append(" ").append(me.getPointerId(i));
//                msg.append(":(").append(me.getX(i)).append(",").append(me.getY(i)).append(")");
//            }
//            System.out.println(msg.toString());
//        }
//        try {
//        	iwm.inj
//            if (!InputManager.getInstance().injectInputEvent(me,
//                    InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT)) {
//                return InjectKeyEvent.INJECT_FAIL;
//            }
//        } finally {
//            me.recycle();
//        }
//        return InjectKeyEvent.INJECT_SUCCESS;
//	}
    
    @Override
    public int injectEvent(IWindowManager iwm, IActivityManager iam, int verbose) {
        
        String note;
        if ((verbose > 0 && !mIntermediateNote) || verbose > 1) {
            if (mAction == MotionEvent.ACTION_DOWN) {
                note = "DOWN";
            } else if (mAction == MotionEvent.ACTION_UP) {
                note = "UP";
            } else {
                note = "MOVE";
            }
            System.out.println(":Sending Pointer ACTION_" + note + 
                    " x=" + mX + " y=" + mY);
        }
        try {
            int type = this.getEventType();
            MotionEvent me = getEvent();
            
            if ((type == InjectKeyEvent.EVENT_TYPE_POINTER && 
                    !iwm.injectPointerEvent(me, false))
                    || (type == InjectKeyEvent.EVENT_TYPE_TRACKBALL && 
                            !iwm.injectTrackballEvent(me, false))) {
                return InjectKeyEvent.INJECT_FAIL;
            }
        } catch (RemoteException ex) {
            return InjectKeyEvent.INJECT_ERROR_REMOTE_EXCEPTION;
        }
        return InjectKeyEvent.INJECT_SUCCESS;
    }
}
