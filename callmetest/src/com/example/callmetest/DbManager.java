package com.example.callmetest;


import java.util.ArrayList;  
import java.util.List;  
  
import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {  
	  
    private static final String DATABASE_NAME = "callmelater2.db";  
    private static final int DATABASE_VERSION = 1;  
      
    public DBHelper(Context context) {  
        //CursorFactory����Ϊnull,ʹ��Ĭ��ֵ  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }  
//    DbManager m ;
    //���ݿ��һ�α�����ʱonCreate�ᱻ����  
    @Override  
    public void onCreate(SQLiteDatabase db) {  
        db.execSQL("CREATE TABLE IF NOT EXISTS runitem" +  
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, interval INTEGER)");
        // add some test data blabla
        
    }  
  
    //���DATABASE_VERSIONֵ����Ϊ2,ϵͳ�����������ݿ�汾��ͬ,�������onUpgrade  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        db.execSQL("ALTER TABLE runitem ADD COLUMN other STRING");  
    }  
}  


public class DbManager {  
    private DBHelper helper;  
    private SQLiteDatabase db;  
      
    public DbManager(Context context) {  
        helper = new DBHelper(context);  
        //��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //����Ҫȷ��context�ѳ�ʼ��,���ǿ��԰�ʵ����DBManager�Ĳ������Activity��onCreate��  
        db = helper.getWritableDatabase();  
    }  
      
    /** 
     * add persons 
     * @param persons 
     */  
    public void add(List<RunItem> persons) {  
        db.beginTransaction();  //��ʼ����  
        try {  
            for (RunItem person : persons) {  
                db.execSQL("INSERT INTO runitem VALUES(null, ?, ?)", new Object[]{person.title, person.interval});  
            }  
            db.setTransactionSuccessful();  //��������ɹ����  
        } finally {  
            db.endTransaction();    //��������  
        }  
    }  
    public void addRunItem(RunItem item) {  
        db.beginTransaction();  //��ʼ����  
        try {  
            db.execSQL("INSERT INTO runitem VALUES(null, ?, ?)", new Object[]{item.title, item.interval});  
            db.setTransactionSuccessful();  //��������ɹ����  
        } finally {  
            db.endTransaction();    //��������  
        }  
    }  
      
    /** 
     * update person's age 
     * @param person 
     */  
    public void updateInterval(RunItem person) {  
        ContentValues cv = new ContentValues();  
        cv.put("interval", person.interval);  
        db.update("runitem", cv, "title = ?", new String[]{person.title});  
    }  
      
    /** 
     * delete old person 
     * @param person 
     */  
    public void deleteByName(String name) {  
        db.delete("runitem", "title = ?", new String[]{name});  
    }  
      
    /** 
     * query all persons, return list 
     * @return List<Person> 
     */  
    public List<RunItem> query() {  
        ArrayList<RunItem> persons = new ArrayList<RunItem>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
            RunItem person = new RunItem();  
            person._id = c.getInt(c.getColumnIndex("_id"));  
            person.title = c.getString(c.getColumnIndex("title"));  
            person.interval = c.getInt(c.getColumnIndex("interval"));  
            persons.add(person);  
        }  
        c.close();  
        return persons;  
    }  
    public boolean isEmpty() {  
        ArrayList<RunItem> persons = new ArrayList<RunItem>();  
        Cursor c = queryTheCursor();  
        boolean r =  !c.moveToNext();
        c.close();  
        return r;  
    }  
    /** 
     * query all persons, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM runitem", null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }

	public void updatePerson(String selectedName, String newName) {
		ContentValues cv = new ContentValues();  
        cv.put("title", newName);  
        db.update("runitem", cv, "title = ?", new String[]{selectedName});
		
	}

	public void clearAll() {
		 db.execSQL("delete from runitem ", new Object[]{});	
	}

	public void updateItem(RunItem p) {
		ContentValues cv = new ContentValues();  
        cv.put("title", p.title);  
        cv.put("interval", p.interval);
        db.update("runitem", cv, "_id = ?", new String[]{String.valueOf(p._id)});
		
	}

	public void updateItem(RunItem p, RunItem newp) {
		ContentValues cv = new ContentValues();  
        cv.put("title", newp.title);  
        cv.put("interval", newp.interval);
        db.update("runitem", cv, "_id = ?", new String[]{String.valueOf(p._id)});
		
	}

	public void addItem(RunItem newp) {
		
		
	}

	public void deleteById(int _id) {
        db.delete("runitem", "_id = ?", new String[]{String.valueOf(_id)});	
	}

	public void InitSalt() {
		if(isEmpty()){
			// add some test data
	        List<RunItem> list = new ArrayList<RunItem>();
	        RunItem r = new RunItem();
	        r.title = "Quick1";
	        r.interval = 1;
	        list.add(r);
	        r = new RunItem();
	        r.title = "Quick3";
	        r.interval = 2;
	        list.add(r);
	        add(list);
		}
	}  
}  