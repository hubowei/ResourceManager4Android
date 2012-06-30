package com.hux.ResourceManager.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FileBrowserActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "FileBrowser";
	private ListView lv = null;
	private List<Map<String,Object>> list;
	private String path = "/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.files);
		
		setTitle("文件浏览器");
		lv = (ListView)findViewById(R.id.lv);
		refresh(path);
	}
	
	private void refresh(String path){
		setTitle("文件浏览器 > "+path);
		list = buildList(path);
		lv.setAdapter(new SimpleAdapter(this, list, R.layout.file_row,
				new String[] { "name", "path" ,"img"}, new int[] { R.id.name,
				R.id.desc ,R.id.img}));
		lv.setOnItemClickListener(this);
		lv.setSelection(0);
	}
	
	private List<Map<String, Object>> buildList(String path) { 
		File[] files = new File(path).listFiles();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(files.length);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("name", "/");
		root.put("img", R.drawable.directory);
		root.put("path", "返回根目录");
		list.add(root);
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("name", "..");
		pmap.put("img", R.drawable.directory);
		pmap.put("path", "返回上一级目录");
		list.add(pmap);
		for (File file : files){
			Map<String, Object> map = new HashMap<String, Object>();
			if(file.isDirectory()){
				map.put("img", R.drawable.directory);
			}else{
				map.put("img", R.drawable.file);
			}
			map.put("name", file.getName());
			map.put("path", file.getPath());
			list.add(map);
		}
		return list;
	}	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "item clicked! [" + position + "]");
		if (position == 0) {
			path = "/";
			refresh(path);
		}else if(position == 1){
			goToParent();
		} else {
			path = (String) list.get(position).get("path");
			File file = new File(path);
			if (file.isDirectory())
				refresh(path);
			else
				Toast.makeText(FileBrowserActivity.this,
						getString(R.string.is_file),
						Toast.LENGTH_SHORT).show();
		}
	}
	
	private void goToParent(){
		File file = new File(path);
		File str_pa = file.getParentFile();
		if(str_pa == null){
			Toast.makeText(FileBrowserActivity.this,
					getString(R.string.is_root),
					Toast.LENGTH_SHORT).show();
			refresh(path);	
		}else{
			path = str_pa.getAbsolutePath();
			refresh(path);	
		}
	}	
}
