package zoltarianie.ang1000;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import zoltarianie.ang1000.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "ang1000.db";

   public Context _context;
   
   public DBHelper(Context context) {
      super(context, DATABASE_NAME , null, 19);
      _context = context;
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
	   db.execSQL("CREATE TABLE IF NOT EXISTS slowka (id INTEGER PRIMARY KEY AUTOINCREMENT, en text, pl text, re text, rank int, count int);");
	   resetWorldsInDB(db);
   }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("info", "DBHelper - reset");
		db.execSQL("DROP TABLE IF EXISTS slowka;");
		onCreate(db);
	}

	public void serResult(Boolean setRank, HashMap<String, String> aWord) {
		int iNewRank = Integer.parseInt(aWord.get("rank"));
		if(setRank) {
			if (iNewRank < 0) iNewRank = 0;
			else iNewRank++;
		} else {
			if (iNewRank <= 0) iNewRank--;
			else iNewRank = 0;
		}

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("UPDATE slowka SET rank = "+iNewRank+", count=30 WHERE id = "+aWord.get("id")+";");
		db.execSQL("UPDATE slowka SET count = count-1 WHERE count > 0;");
	}

	public HashMap<String, String> getWord() {
		SQLiteDatabase db = this.getReadableDatabase();
		HashMap<String, String> listUnder = null;
		Cursor c;

		if(listUnder == null) {
			c = db.rawQuery("SELECT * FROM slowka WHERE rank < 0 AND count=0 ORDER BY RANDOM() LIMIT 1;", null);
			if (c.moveToFirst()) {
				listUnder = splitCToArray(c);
			}
			c.close();
		}

		if(listUnder == null) {
			c = db.rawQuery("SELECT * FROM slowka WHERE rank >= 0 AND count=0 ORDER BY RANDOM() LIMIT 1;", null);
			if (c.moveToFirst()) {
				listUnder = splitCToArray(c);
			}
			c.close();
		}

		return(listUnder);
	}

	private HashMap<String, String> splitCToArray(Cursor c) {
		HashMap<String, String> listUnder = new HashMap<String, String>();
		listUnder.put("id", c.getString(c.getColumnIndex("id")));
		listUnder.put("en", c.getString(c.getColumnIndex("en")));
		listUnder.put("pl", c.getString(c.getColumnIndex("pl")));
		listUnder.put("re", c.getString(c.getColumnIndex("re")));
		listUnder.put("rank", c.getString(c.getColumnIndex("rank")));
		listUnder.put("count", c.getString(c.getColumnIndex("count")));
		return(listUnder);
	}

	public void resetWorldsInDB(SQLiteDatabase db) {
		int iLineNum = 0;
		String sInstSql = "INSERT INTO `slowka` (`en`, `pl`, `re`, `rank`, `count`) VALUES ";

		try {
			InputStream inputStream = _context.getResources().openRawResource(R.raw.nowe_slowka);
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				iLineNum++;
				sInstSql += " "+line;

				if(iLineNum>=2){
					db.execSQL(sInstSql.substring(0, sInstSql.length()-1)+";");
					iLineNum = 0;
					sInstSql = "INSERT INTO `slowka` (`en`, `pl`, `re`, `rank`, `count`) VALUES ";
				}
			}
			Log.i("info", total.toString());
		} catch (IOException e) {
			Log.e("info", "Error opening Log.", e);
		}
	}
}