package com.hux.ResourceManager.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hux.ResourceManager.Activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ResourceManagerActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "ResourceManager";
    private ListView lv;
    private List<Map<String, Object>> list;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        lv = (ListView)findViewById(R.id.lv);
        refresh();
    }
    
    private void refresh(){
    	list = creatList();
    	lv.setAdapter(new SimpleAdapter(this, list, R.layout.main_row,
				new String[] { "name", "desc", "img" }, new int[] { R.id.name,
				R.id.desc, R.id.img }));
    	lv.setOnItemClickListener(this);
    	lv.setSelected(false);
    }
    
    private List<Map<String, Object>> creatList(){
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(3);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "系统信息");
		map.put("desc", "查看设备的OS,CPU,内存等信息.");
		map.put("img", R.drawable.ic_launcher);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("name", "进程管理");
		map.put("desc", "管理正在运行的程序.");
		map.put("img", R.drawable.ic_launcher);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("name", "文件浏览器");
		map.put("desc", "浏览设备的文件系统.");
		map.put("img", R.drawable.ic_launcher);
		list.add(map);

		return list;
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	Intent intent = new Intent();
		Log.i(TAG, "item clicked! [" + position + "]");
		switch (position) {
		case 0:
			intent.setClass(ResourceManagerActivity.this, SystemInfoActivity.class);
			startActivity(intent);
			break;
		case 1:
			intent.setClass(ResourceManagerActivity.this, ProcessKillerActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent.setClass(ResourceManagerActivity.this, FileBrowserActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
    }
}