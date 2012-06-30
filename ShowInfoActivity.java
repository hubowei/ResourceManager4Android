package com.hux.ResourceManager.Activity;

import com.hux.ResourceManager.Util.*;
import com.hux.ResourceManager.Activity.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class ShowInfoActivity extends Activity implements Runnable{
	private static final String TAG = "ShowInfo";
	private TextView title = null;
	private TextView info = null;
	private ProgressDialog pd;
	public String info_datas;
	public boolean is_valid = false;
	public int _id = 0;
	public String _name = "";
	public int _position = 0;
	public int _ref = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infos);
		
		receive();
		info = (TextView) findViewById(R.id.info);
		title = (TextView) findViewById(R.id.title);
		setTitle("ResourceManager: " + _name);
		title.setText(_name);
		loading();
	}
	
	void receive(){
		Log.i(TAG, "receive");
		Intent startingIntent = getIntent();
		if (startingIntent != null) {
			Bundle info = startingIntent
					.getBundleExtra("android.intent.extra.info");
			if (info == null) {
				is_valid = false;
			} else {
				_id = info.getInt("id");
				_name = info.getString("name");
				_position = info.getInt("position");
				is_valid = true;
			}
		} else {
			is_valid = false;
		}
		Log.i(TAG, "_name:" + _name + ",_id="+_id);
	}
	
	void loading(){
		pd = ProgressDialog.show(this, "Please Wait a moment..",
				"fetch info datas...", true, false);
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		if (_ref > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.i("load_data", "e=" + e.toString());
			}
		}
		switch (_id) {
		case Preferences.VER_INFO:
			info_datas = FetchData.fetch_version_info();
			break;
		case Preferences.CPU_INFO:
			info_datas = FetchData.fetch_cpu_info();
			break;
		case Preferences.MEM_INFO:
			info_datas = FetchData.getMemoryInfo(this);
			break;
		}

		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			info.setText(info_datas);
		}
	};

}
