package md.statistica.ipc.ipcm_v01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Database 
{
	//----------------------------------------------------------------------------------------------
	private static Context context;
	public static DatabaseHealper helper;
	public static SQLiteDatabase db;

	//----------------------------------------------------------------------------------------------
	public static void open(Context setContext) 
	{
		context = setContext;
		if (context == null) return;
		
		helper = new DatabaseHealper(context);
		db = helper.getWritableDatabase();
	}

	//----------------------------------------------------------------------------------------------
	public static void close() 
	{
		helper.close();
		db.close();
	}
	//----------------------------------------------------------------------------------------------
}
