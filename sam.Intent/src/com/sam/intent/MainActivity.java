package com.sam.intent;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	EditText et ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText)findViewById(R.id.editText1);
        et.setText("aha");
    }

    public void goMore(View view){
    	 String val=et.getText().toString();
    	 Hashtable<String,String> args = new Hashtable<String,String> ();
    	 args.put("val", val);
//    	 go(MainActivity.this,MoreActivity.class,args); 
    	 go(this,MoreActivity.class,args);
    }
    public void go(Context from,Class to,Hashtable<String,String> args){
        Intent intent=new Intent();
        Enumeration e1 = args.keys(); 
        while (e1.hasMoreElements()) { 
          String key = (String) e1.nextElement();
          String val = args.get(key);
          intent.putExtra(key, val); 
        } 
        intent.setClass(from,to); 
        //Æô¶¯intentµÄActivity 
        from.startActivity(intent); 
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
