package com.hux.ResourceManager.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ProcessKillerActivity extends Activity implements
		OnItemClickListener {
	private static final String TAG = "ProcessKiller";
	private ListView lv = null;
	private List<Map<String, Object>> list;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.procs);
		setTitle("进程管理");
		lv = (ListView) findViewById(R.id.lv);
		refresh();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		list = creatList();
		lv.setAdapter(new SimpleAdapter(this, list, R.layout.proc_row,
				new String[] { "name","img" }, new int[] { R.id.name,R.id.img}));
		lv.setOnItemClickListener(this);
		lv.setSelected(false);
	}

	private List<Map<String, Object>> creatList() {
		// TODO Auto-generated method stub
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processes = activityManager
				.getRunningAppProcesses();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator<RunningAppProcessInfo> iterator = processes.iterator(); iterator
				.hasNext();) {
			RunningAppProcessInfo info = iterator.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", info.processName);
			
			list.add(map);
		}
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
}
