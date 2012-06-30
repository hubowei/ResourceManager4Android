package com.hux.ResourceManager.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hux.ResourceManager.Activity.R;
import com.hux.ResourceManager.Util.Preferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SystemInfoActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "SystemInfo";
	private ListView lv;
	private List<Map<String, Object>> list;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system);
		
		setTitle("系统信息");
		lv = (ListView) findViewById(R.id.lv);
		refresh();
	}
	
	private void refresh(){
    	list = creatList();
    	lv.setAdapter(new SimpleAdapter(this, list, R.layout.system_row,
				new String[] { "name", "desc" }, new int[] { R.id.name,
				R.id.desc}));
    	lv.setOnItemClickListener(this);
    	lv.setSelected(false);
    }
	
	private List<Map<String, Object>> creatList(){
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	map = new HashMap<String, Object>();
    	map.put("id", Preferences.VER_INFO);
		map.put("name", "OS版本信息");
		map.put("desc", "获取设备的操作系统版本信息.");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", Preferences.CPU_INFO);
		map.put("name", "CPU信息");
		map.put("desc", "获取设备的CPU信息.");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", Preferences.MEM_INFO);
 		map.put("name", "内存信息");
		map.put("desc", "获取设备的内存信息.");
		list.add(map);

		return list;
    }
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent();
		Log.i(TAG, "item clicked! [" + position + "]");
		Bundle info = new Bundle();
		Map<String, Object> map = list.get(position);
		info.putInt("id",  (Integer) map.get("id"));
		info.putString("name", (String) map.get("name"));
		info.putString("desc", (String) map.get("desc"));
		info.putInt("position", position);
		intent.putExtra("android.intent.extra.info", info);
		intent.setClass(SystemInfoActivity.this, ShowInfoActivity.class);
		startActivityForResult(intent, 0);
	}
}
