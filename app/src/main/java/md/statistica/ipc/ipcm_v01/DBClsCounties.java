package md.statistica.ipc.ipcm_v01;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class DBClsCounties 
{
	//----------------------------------------------------------------------------------------------
	//Class which describes one row of the owner class
	public static class ItemClass 
	{
		public String	code;
		public String	name;
		
		//OnLoad calculated values
		public int		position;

		//..........................................................................................
		public ItemClass()
		{
			code = "";
			name = "";

			position = 0;
		}

		//..........................................................................................
		//Import routine
		public void importValue(String dbName, String dbValue)
		{
			if (dbName.equals("CODE_COUNTRY"))	code = dbValue;
			if (dbName.equals("NAME"))			name = dbValue;
		}

		//..........................................................................................
		//Import routine
		public boolean importToDB()
		{
			try
			{
				//Insert record
				ContentValues cv = new ContentValues();
				cv.put("CODE_COUNTRY", code);
				cv.put("NAME", name);
				Database.db.insert("CLS_COUNTRY_VW", null, cv);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		//..........................................................................................
	}

	//----------------------------------------------------------------------------------------------
	public List<ItemClass> list;
	
	public static final int	SORT_BY_CODE = 1;
	public static final int	SORT_BY_NAME = 2;

	//----------------------------------------------------------------------------------------------
	public DBClsCounties()
	{
		list = new ArrayList<ItemClass>();
	}

	//----------------------------------------------------------------------------------------------
	//Delete all data
	public static boolean dropData()
	{
		try
		{
		    Database.db.delete("CLS_COUNTRY_VW", null, null);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	//----------------------------------------------------------------------------------------------
	public void loadData(int sortBy)
	{
		loadData(sortBy, "", "");
	}

	//----------------------------------------------------------------------------------------------
	public void loadData(int sortBy, String codeFilter, String nameFilter) 
	{
		//Load data
		list.clear();
		ItemClass item;
		Cursor dbc;
		int i = 0;

		//Where clause
		String whereStr = "CODE_COUNTRY LIKE '" + codeFilter + "%' AND LOWER(NAME) LIKE LOWER('%" + nameFilter.replace("'", "''") + "%')";
		
		//Order by clause
		String orderBy;
		if (sortBy == SORT_BY_NAME) orderBy = "NAME";
		else orderBy = "CODE_COUNTRY";

		//Execute query
		dbc = Database.db.query("CLS_COUNTRY_VW", null, whereStr, null, null, null, orderBy);
		if (dbc.moveToFirst()) do
		{
			item = new ItemClass();
			item.code = dbc.getString(dbc.getColumnIndex("CODE_COUNTRY"));
			item.name = dbc.getString(dbc.getColumnIndex("NAME"));

			item.position = i;
			list.add(i++,item);
			
		} while (dbc.moveToNext());
		dbc.close();
	}

	//----------------------------------------------------------------------------------------------
	public ItemClass getItem(String codeFind) 
	{
		for (ItemClass item: list)
		{
			if (item.code.equals(codeFind)) return item;
		}
		return null;
	}

	//----------------------------------------------------------------------------------------------
	public ItemClass findItem(String codeFind) 
	{
		for (ItemClass item: list)
		{
			if (item.code.startsWith(codeFind)) return item;
		}
		return null;
	}
	//----------------------------------------------------------------------------------------------
}
