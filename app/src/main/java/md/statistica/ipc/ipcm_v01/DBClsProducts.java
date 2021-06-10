package md.statistica.ipc.ipcm_v01;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class DBClsProducts 
{
	//----------------------------------------------------------------------------------------------
	//Class which describes one row of the owner class
	public static class ItemClass 
	{
		public String	code;
		public Date		version;
		public String	name;
		public double	quantity;
		public long		idMesurementUnit;
		public String	mesurementUnitShort;
		public String	mesurementUnit;
		public long		idProdType;
		public String	prodType;
		public String	collectOrdinary;
		public int		collectPerMonth;
		public double	variaty;
		public long		idProdStatus;
		public String	prodStatus;
		
		//OnLoad calculated values
		public int    position;

		//..........................................................................................
		public ItemClass()
		{
			code = "";
			version = new Date(0);
			name = "";
			quantity = 0;
			idMesurementUnit = 0;
			mesurementUnitShort = "";
			mesurementUnit = "";
			idProdType = 0;
			prodType = "";
			collectOrdinary = "0";
			collectPerMonth = 0;
			variaty = 0;
			idProdStatus = 0;
			prodStatus = "";

			position = 0;
		}

		//..........................................................................................
		//Import routine
		public void importValue(String dbName, String dbValue)
		{
			if (dbName.equals("CODE_PROD"))					code = dbValue;
			if (dbName.equals("CODE_PROD_VERS"))			version = MainLibrary.StringToDate(dbValue);
			if (dbName.equals("NAME"))						name = dbValue;
			if (dbName.equals("QUANTITY"))					quantity = Double.parseDouble(dbValue);
			if (dbName.equals("ID_MESUREMENT_UNIT"))		idMesurementUnit = Long.parseLong(dbValue);
			if (dbName.equals("MESUREMENT_UNIT_SHORT"))		mesurementUnitShort = dbValue;
			if (dbName.equals("MESUREMENT_UNIT"))			mesurementUnit = dbValue;
			if (dbName.equals("ID_PROD_TYPE"))				idProdType = Long.parseLong(dbValue);
			if (dbName.equals("PROD_TYPE"))					prodType = dbValue;
			if (dbName.equals("COLLECT_ORDINARILY"))		collectOrdinary = dbValue;
			if (dbName.equals("COLLECT_PER_MONTH"))			collectPerMonth = Integer.parseInt(dbValue);
			if (dbName.equals("VARIATY"))					variaty = Double.parseDouble(dbValue);
			if (dbName.equals("ID_PROD_STATUS"))			idProdStatus = Long.parseLong(dbValue);
			if (dbName.equals("PROD_STATUS"))				prodStatus = dbValue;
		}

		//..........................................................................................
		//Import routine
		public boolean importToDB()
		{
			try
			{
				//Insert record
				ContentValues cv = new ContentValues();
				cv.put("CODE_PROD", code);
				cv.put("CODE_PROD_VERS", MainLibrary.DateToStringSql(version));
				cv.put("NAME", name);
				cv.put("QUANTITY", quantity);
				cv.put("ID_MESUREMENT_UNIT", idMesurementUnit);
				cv.put("MESUREMENT_UNIT_SHORT", mesurementUnitShort);
				cv.put("MESUREMENT_UNIT", mesurementUnit);
				cv.put("ID_PROD_TYPE", idProdType);
				cv.put("PROD_TYPE", prodType);
				cv.put("COLLECT_ORDINARILY", collectOrdinary);
				cv.put("COLLECT_PER_MONTH", collectPerMonth);
				cv.put("VARIATY", variaty);
				cv.put("ID_PROD_STATUS", idProdStatus);
				cv.put("PROD_STATUS", prodStatus);
				Database.db.insert("CLS_PRODUCT_VW", null, cv);
				
				//Update data on of the product price
				cv.clear();
				cv.put("NAME", name);
				cv.put("QUANTITY", quantity);
			    Database.db.update("DATA_PRICE_VW", cv, "CODE_PROD = ? AND CODE_ASSORT = ?", new String[] { code, "0" });
				
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		
		//..........................................................................................
		//Get the full name of the product
		public String getNameFull()
		{
			return name + " ("+ String.valueOf(quantity) + " " + mesurementUnitShort + ")";
		}
		//..........................................................................................
	}

	//----------------------------------------------------------------------------------------------
	public List<ItemClass> list;
	
	public static final int	SORT_BY_CODE = 1;
	public static final int	SORT_BY_NAME = 2;

	//----------------------------------------------------------------------------------------------
	public DBClsProducts() 
	{
		list = new ArrayList<ItemClass>();
	}

	//----------------------------------------------------------------------------------------------
	//Delete all data
	public static boolean dropData()
	{
		try
		{
		    Database.db.delete("CLS_PRODUCT_VW", null, null);
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
		String whereStr = "COLLECT_ORDINARILY = ? AND CODE_PROD LIKE '" + codeFilter + "%' AND LOWER(NAME) LIKE LOWER('%" + nameFilter.replace("'", "''") + "%')";
		String[] whereArgs = new String[] { "1" };
		
		//Order by clause
		String orderBy;
		if (sortBy == SORT_BY_NAME) orderBy = "NAME";
		else orderBy = "CODE_PROD";

		//Execute query
		dbc = Database.db.query("CLS_PRODUCT_VW", null, whereStr, whereArgs, null, null, orderBy);
		if (dbc.moveToFirst()) do
		{
			item = new ItemClass();
			item.code = dbc.getString(dbc.getColumnIndex("CODE_PROD"));
			item.version = MainLibrary.StringToDateSql(dbc.getString(dbc.getColumnIndex("CODE_PROD_VERS")));
			item.name = dbc.getString(dbc.getColumnIndex("NAME"));
			item.quantity = dbc.getDouble(dbc.getColumnIndex("QUANTITY"));
			item.idMesurementUnit = dbc.getLong(dbc.getColumnIndex("ID_MESUREMENT_UNIT"));
			item.mesurementUnitShort = dbc.getString(dbc.getColumnIndex("MESUREMENT_UNIT_SHORT"));
			item.mesurementUnit = dbc.getString(dbc.getColumnIndex("MESUREMENT_UNIT"));
			item.idProdType = dbc.getLong(dbc.getColumnIndex("ID_PROD_TYPE"));
			item.prodType = dbc.getString(dbc.getColumnIndex("PROD_TYPE"));
			item.collectOrdinary = dbc.getString(dbc.getColumnIndex("COLLECT_ORDINARILY"));
			item.collectPerMonth = dbc.getInt(dbc.getColumnIndex("COLLECT_PER_MONTH"));
			item.variaty = dbc.getDouble(dbc.getColumnIndex("VARIATY"));
			item.idProdStatus = dbc.getLong(dbc.getColumnIndex("ID_PROD_STATUS"));
			item.prodStatus = dbc.getString(dbc.getColumnIndex("PROD_STATUS"));

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
