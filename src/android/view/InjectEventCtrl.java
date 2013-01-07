package android.view;

import android.app.ActivityManagerNative;
import android.app.IActivityController;
import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.view.IWindowManager;
import android.view.WindowManagerImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class InjectEventCtrl {
	private List<InjectMotionEvent> mList;
	private IActivityManager mAm;
	private IWindowManager mWm;
	
	public InjectEventCtrl() {
		getSystemInterfaces();
	}
	
	private void getSystemInterfaces() {
		mWm = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
		mAm = ActivityManagerNative.getDefault();
		mList = new ArrayList<InjectMotionEvent>();
	}
	
	/**
	 * 产生触摸事件
	 */
	public void InjectMotionEvent() {
		generateMotionEvent(new Random());
		
		for(int i=0; i<mList.size(); i++) {
			InjectKeyEvent keyEvent = mList.get(i);
			keyEvent.injectEvent(mWm, mAm, 1);
		}
	}
	
	private void generateMotionEvent(Random random){
		mList.clear();
        Display display = WindowManagerImpl.getDefault().getDefaultDisplay();

        float x = Math.abs(random.nextInt() % display.getWidth());
        float y = Math.abs(random.nextInt() % display.getHeight());
        long downAt = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        if (downAt == -1) {
            downAt = eventTime;
        }
        
        //生成一个按下事件
        InjectMotionEvent e = new InjectMotionEvent(InjectKeyEvent.EVENT_TYPE_POINTER,
                downAt, MotionEvent.ACTION_DOWN, x, y, 0);
        e.setIntermediateNote(false);
        mList.add(e);

        //生成一个弹起事件
        e = new InjectMotionEvent(InjectKeyEvent.EVENT_TYPE_POINTER,
                downAt, MotionEvent.ACTION_UP, x, y, 0);
        e.setIntermediateNote(false);
        mList.add(e);
	}
}
