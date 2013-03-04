package com.example.callmetest;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ConfActivity extends Activity {
	DbManager m ;
	Spinner spinner ;
	EditText etinterval ;
	EditText ettitle ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conf);
		m = new DbManager(this);
		// map widget
		spinner = (Spinner)findViewById(R.id.spinner1);
		etinterval = (EditText)findViewById(R.id.etInterval);
		ettitle = (EditText)findViewById(R.id.etTitle);
		OnItemSelectedListener oisl=  new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				RunItem item = (RunItem)parent.getItemAtPosition(position);
				etinterval.setText(String.valueOf(item.interval));
				ettitle.setText(String.valueOf(item.title));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
        };
        //为下拉列表绑定事件监听器
        spinner.setOnItemSelectedListener(oisl);
		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_conf, menu);
		return true;
	}
	public void onExit(View v){
		this.finish();
	}
	ArrayAdapter aa ;
	private void refresh() {
	//	final List<String> list=new ArrayList<String>();		
		final List<RunItem> list=new ArrayList<RunItem>();
		List<RunItem> persons = m.query();
		for (int i = 0 ;i<persons.size();i++)
			list.add(persons.get(i));
		aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
		spinner.setAdapter(aa);
	}
	public void onAdd(View view){
		RunItem newp = new RunItem();
		newp.title = ettitle.getText().toString();
		newp.interval = Integer.parseInt(etinterval.getText().toString());
		m.addRunItem(newp);
		refresh();
	}
	public void onUpdate(View view){
	//	String selectedName = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
		RunItem p = (RunItem) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
		RunItem newp = new RunItem();
		newp.title = ettitle.getText().toString();
		newp.interval = Integer.parseInt(etinterval.getText().toString());
//		toast(Integer.valueOf(p._id).toString());
//		m.updatePerson(selectedName,newName);
		m.updateItem(p,newp);
		refresh();
	}
	void toast(String str){
		Toast.makeText(getApplicationContext(), str,
			     Toast.LENGTH_SHORT).show();
	}
	public void onDelete(View view){
		if (spinner.getAdapter().getCount()>1){
			RunItem item = (RunItem) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
			deletePerson(item);
			refresh();
		}
		else
			toast("最后的一个，不能删除了。");
	}
	private void deletePerson(RunItem item) {
		m.deleteById(item._id);
		
	}

}
