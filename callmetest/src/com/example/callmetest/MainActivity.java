package com.example.callmetest;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.callmetest.R.id;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;


public class MainActivity extends Activity implements TimeFirer{
	Button button1;
	Button button2 ;
	Spinner spinner;
	TextView etCount ;
	TextView et ;
	RealTimer timer1 ;
	private MediaPlayer mPlayer;
	int timeInterval = 100 ;
	int count = 0 ;
	public void fireTime(int minutes, int seconds) {
		et.setText(String.format("%d:%02d", minutes, seconds));
		if (0 != count)
			etCount.setText(String.format("- %d - 次", count));
		if(minutes % timeInterval == 0 && seconds  == 1 && minutes != 0){
			//if (!mPlayer.isPlaying())			
				initMediaPlayer();
				mPlayer.start();
				count ++; 
				toast("-任务完成-");
//				Toast.makeText(getApplicationContext(),"--任务完成--", Toast.LENGTH_LONG).show();
		}
	}
	void toast(String str){
		Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//找到Xml中定义的下拉列表
        spinner = (Spinner)findViewById(R.id.spinner);
        //准备一个数组适配器
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        ArrayAdapter<RunItem> adapter = new ArrayAdapter<RunItem>(this,android.R.layout.simple_spinner_item);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);
        //设置下拉样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
//        adapter.add("Quick-1分钟");
//        adapter.add("灸脚-20分钟");
//        adapter.add("仰卧起坐-20分钟");
//        adapter.add("快步走-30分钟");
//        adapter.add("Test-5分钟");
        try{
	        DbManager m = new DbManager(this);
	        m.InitSalt();
	        List<RunItem> list = m.query();
	        for(int i = 0 ;i<list.size();i++){
	        	RunItem item = list.get(i);
	        	adapter.add(item);
	        }
	        spinner.setAdapter(adapter);
	        //为下拉列表设置适配器
	        
	        //定义子元素选择监听器
	        OnItemSelectedListener oisl=  new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
//					String str = parent.getItemAtPosition(position).toString();
//					str = hackInt(str);
//					timeInterval = Integer.parseInt(str);
					RunItem item = (RunItem)parent.getItemAtPosition(position);
					timeInterval = item.interval;
					String str = String.format("%d", item.interval);
		            Toast.makeText(getApplicationContext(),"Interval is ： " + str, Toast.LENGTH_LONG).show();
				}
				private String hackInt(String str) {
					try{
					String r = "";
					for(int i = 0 ;i< str.length();i++){
						String str1 = str.substring(i,i+1);
						if ("0123456789".indexOf(str1) !=-1){
							r += str1;
//							Log.v("--",str1);
						}
					}
					//return Integer.parseInt(r);
					return r ;
					}catch(Exception e){
					  return e.getMessage();
					}
//					return "0";
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
	        };
	        //为下拉列表绑定事件监听器
	        spinner.setOnItemSelectedListener(oisl);
	        button1 = (Button)findViewById(R.id.button1);
	        button2 = (Button)findViewById(R.id.button2);
	        etCount = (TextView)findViewById(R.id.etCount);
	        button2.setEnabled(false);
	        et = (TextView)findViewById(R.id.et);
	        timer1 = new RealTimer(this);
	        //timer
	        
	        initMediaPlayer();
        }catch(Exception ex){
        	Log.v("----",ex.getMessage());
        }
    
	}

	// 初始化播放器
	private void initMediaPlayer() {

		// 定义播放器
		mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.yes);
		// 定义资源准备好的监听器
		mPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// 资源准备好了再让播放器按钮有效
				//Toast.makeText(getApplicationContext(), "onPrepared", Toast.LENGTH_SHORT).show();				
			}
		});

		// 定义播放完成监听器
		mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
//				Toast.makeText(getApplicationContext(), "onCompletion",Toast.LENGTH_SHORT).show();
				//mPlayer.stop();
			}
		});
	}
	public void startClick(View v){
//		Toast.makeText(getApplicationContext(), "--开始--", Toast.LENGTH_LONG).show();
		toast("--开始--");
		button1.setEnabled(false);
		button2.setEnabled(true);
		spinner.setEnabled(false);
		et.setText("_");
		etCount.setText("_");
		timer1.startTimer();
		
	}
	public void stopClick(View v){
//		Toast.makeText(getApplicationContext(), "--结束--", Toast.LENGTH_LONG).show();
		toast("--结束--");
		button1.setEnabled(true);
		button2.setEnabled(false);
		spinner.setEnabled(true);
		timer1.stopTimer();
		et.setText("_");
		count = 0 ;
		etCount.setText("_");
		mPlayer.stop();
//		if (mPlayer.isPlaying())				
//			mPlayer.stop();
	}
	public void confClick(View v){
		toast("--conf--");
		goMore(v);
	}
	public void goMore(View view){
   	 String val=et.getText().toString();
   	 Hashtable<String,String> args = new Hashtable<String,String> ();
   	 args.put("val", val);
//   	 go(MainActivity.this,MoreActivity.class,args); 
   	 go(this,ConfActivity.class,args);
   }
   public void go(Context from,Class to,Hashtable<String,String> args){
       Intent intent=new Intent();
       Enumeration<String> e1 = args.keys(); 
       while (e1.hasMoreElements()) { 
         String key = (String) e1.nextElement();
         String val = args.get(key);
         intent.putExtra(key, val); 
       } 
       intent.setClass(from,to); 
       from.startActivity(intent); 
   }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}

interface TimeFirer{
	void fireTime(int minutes, int seconds) ;
}
class RealTimer{
	TimeFirer t ;
	RealTimer(TimeFirer ft){
		t = ft ;
	}
	final Handler h = new Handler(){
    	
        @Override
        public void handleMessage(Message msg) {
           long millis = System.currentTimeMillis() - starttime;
           int seconds = (int) (millis / 1000);
           int minutes = seconds / 60;
           seconds     = seconds % 60;
           fireTimer(minutes, seconds);
           super.handleMessage(msg);
        }
	};
	private void fireTimer(int minutes, int seconds) {
		if (t!=null)
			t.fireTime(minutes, seconds);
	}
    public void stopTimer() {
		starttime = 0 ;
	    timer.cancel();
	    timer = null;
	    task = null;
	}
	public void startTimer() {
		starttime = System.currentTimeMillis();
		timer = new Timer();
		task = new TimerTask(){          	  
	        public void run() {  
	            Message message = new Message();      
	            message.what = 1;      
	            h.sendMessage(message);    
	        }  
	          
	    };  
	    timer.schedule(task, 0,1000);		
	}
    TimerTask task ;
    long starttime  ;
    Timer timer ;
}