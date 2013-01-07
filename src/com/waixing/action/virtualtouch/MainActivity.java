package com.waixing.action.virtualtouch;

import android.os.Bundle;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.InjectEventCtrl;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener{
	private static final String TAG = "MainActivity"; 
	
	private Button mButton;
	private InjectEventCtrl injectEventCtrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mButton = (Button) findViewById(R.id.button1);
        mButton.setOnClickListener(this);
        mButton.setOnTouchListener(this);
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        
//        TextView mTv = new TextView(this);
//        mTv.setText("Hello");
//        
//        LayoutParams lparams = new LayoutParams();
//        wm.addView(mTv, lparams);
        
        injectEventCtrl = new InjectEventCtrl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button1:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_MENU:
			injectEventCtrl.InjectMotionEvent();
			android.util.Log.d(TAG, "KEYCODE_A");
			break;
		}
		
		return true;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		Log.d(TAG, arg1.toString());
		return false;
	}

    
}
