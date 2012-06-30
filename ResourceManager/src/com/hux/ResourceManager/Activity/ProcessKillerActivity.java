package com.hux.ResourceManager.Activity;

import android.R.string;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class ProcessKillerActivity extends Activity
{
	private static final int KILLPROC=Menu.FIRST;
	private static final int PROCINFO=Menu.FIRST+1;
	private static final int UNINSTALL=Menu.FIRST+2;

	public ActivityManager am=null;
	private ListView listView=null;
	PackageManager pkManager=null;
	ApplicationInfo appInfo = null;
	List<ActivityManager.RunningAppProcessInfo> processInfoList = null;
	private List<ApplicationInfo> allAppInfoList = null;
	List<Map<String,Object>> list;
	public void refresh(){
		pkManager=this.getApplicationContext().getApplicationContext().getPackageManager();
		allAppInfoList = pkManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		am=(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE); 
		processInfoList= am.getRunningAppProcesses();	
		list=getdata();
		SimpleAdapter adapter=new SimpleAdapter(
				this,list,R.layout.procs,
				new String[]{"iv","procnm","procinfo"},
				new int[]{R.id.iv,R.id.procnm,R.id.procinfo});
		adapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				if ((view instanceof ImageView) && (data instanceof Drawable)) {
					ImageView iv = (ImageView) view;
					iv.setImageDrawable((Drawable)data);
					return true;
				}
				return false;
			}
		});
		listView.setAdapter(adapter);
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("进程管理");
		listView =new ListView(this);
		setContentView(listView);
		refresh();
		//registerForContextMenu(this.listView);
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				AdapterView.AdapterContextMenuInfo info;

				menu.add(0, KILLPROC, 0, "结束进程");
				menu.add(0, PROCINFO, 0 ,"详细信息");
				menu.add(0, UNINSTALL, 0, "卸载");

			}
		});
	}

	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo itemInfo = (AdapterContextMenuInfo)item.getMenuInfo();  
		int position = itemInfo.position; 

		switch (item.getItemId())
		{
		case KILLPROC:am.killBackgroundProcesses((String)list.get(position).get("procnm"));
		refresh();
		break;
		case PROCINFO:Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", (String)list.get(position).get("procnm"), null);
		intent.setData(uri);
		startActivity(intent);
		break;
		case UNINSTALL:
			Uri uri2 = Uri.fromParts("package",(String)list.get(position).get("procnm"), null);
			Intent it = new Intent(Intent.ACTION_DELETE, uri2);
			try {
				ProcessKillerActivity.this.startActivity(it);
			} catch (Exception e) {
				Toast.makeText(ProcessKillerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			refresh();
			break;
		}
		return super.onContextItemSelected(item);
	}

	public ApplicationInfo getAppInfo(String pName){
		for (ApplicationInfo it: allAppInfoList){
			if(it.processName.equals(pName)){
				return it;
			}
		}
		return null;
	}
	private List<Map<String,Object>>  getdata()
	{
		Map<String,Object> map; 
		List<Map<String,Object>> processList = new ArrayList<Map<String,Object>>();		
		for (RunningAppProcessInfo it: processInfoList){
			appInfo = getAppInfo(it.processName);
			map =new HashMap<String, Object>();	
			map.put("procnm", it.processName);
			map.put("iv",appInfo.loadIcon(pkManager));
			processList.add(map);
			int[] myMempid = new int[] { it.pid };  
			Debug.MemoryInfo[] memoryInfo = am
					.getProcessMemoryInfo(myMempid); 
			int memSize = memoryInfo[0].dalvikPrivateDirty;
			map.put("procinfo","PID:"+it.pid+" "+"MEM:"+memSize+"kb");
		}
		return processList;
	}
}