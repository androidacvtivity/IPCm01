package md.statistica.ipc.ipcm_v01;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public class DBDataPrices 
{
	//----------------------------------------------------------------------------------------------
	//Class which describes one row of the owner class
	public static class ItemClass 
	{
		public DBDataPrices parent;
		public Date		setDate;
		public String	cuatm;
		public String	codeUnit;
		public String	codeProd;
		public String	codeAsrt;
		public String	name;
		public double	quantity;
		public long		idMesurementUnit;		//virtual
		public String	mesurementUnitShort;	//virtual
		public long		idProdType;				//virtual
		public String	prodType;				//virtual
		public String	asrtCountry;
		public String	asrtCompany;
		public String	asrtBrand;
		public int		collectPerMonth;		//virtual
		public double	variaty;				//virtual
		public long		idDataStatus;
		public double	price_P1;
		public double	price_P2;
		public double	price_P3;
		public double	price_C1;
		public double	price_C2;
		public double	price_C3;
		public int		repeatedCount;
		public String	textualComment;
		public Date		modDate;
		
		//OnLoad calculated values
		public int		decade;
		public int		position;
		public int		consistsFrom;
		public int		consistsFromNonZero;

		//..........................................................................................
		public ItemClass()
		{
			parent = null;
			setDate = new Date(0);
			cuatm = "";
			codeUnit = "";
			codeProd = "";
			codeAsrt = "";
			name = "";
			quantity = 0;
			idMesurementUnit = 0;
			mesurementUnitShort = "";
			idProdType = 0;
			prodType = "";
			asrtCountry = "";
			asrtCompany = "";
			asrtBrand = "";
			variaty = 0;
			collectPerMonth = 0;
			idDataStatus = 0;
			price_P1 = 0;
			price_P2 = 0;
			price_P3 = 0;
			price_C1 = 0;
			price_C2 = 0;
			price_C3 = 0;
			repeatedCount = 0;
			textualComment = "";
			modDate = new Date(0);

			decade = 0;
			position = 0;
			consistsFrom = 0;
			consistsFromNonZero = 0;
		}

		//..........................................................................................
		//Import routine
		public void importValue(String dbName, String dbValue)
		{
			if (dbName.equals("SET_DATE"))				setDate = MainLibrary.StringToDate(dbValue);
			if (dbName.equals("CUATM"))					cuatm = dbValue;
			if (dbName.equals("CODE_UNIT"))				codeUnit = dbValue;
			if (dbName.equals("CODE_PROD"))				codeProd = dbValue;
			if (dbName.equals("CODE_ASSORT"))			codeAsrt = dbValue;
			if (dbName.equals("NAME"))					name = dbValue;
			if (dbName.equals("QUANTITY"))				quantity = Double.parseDouble(dbValue);
			if (dbName.equals("ASSORT_COUNTRY"))		asrtCountry = dbValue;
			if (dbName.equals("ASSORT_COMPANY"))		asrtCompany = dbValue;
			if (dbName.equals("ASSORT_BRAND"))			asrtBrand = dbValue;
			if (dbName.equals("ID_DATA_STATUS"))		idDataStatus = Long.parseLong(dbValue);
			if (dbName.equals("PRICE_P1"))				price_P1 = Double.parseDouble(dbValue);
			if (dbName.equals("PRICE_P2"))				price_P2 = Double.parseDouble(dbValue);
			if (dbName.equals("PRICE_P3"))				price_P3 = Double.parseDouble(dbValue);
			if (dbName.equals("PRICE_C1"))				price_C1 = Double.parseDouble(dbValue);
			if (dbName.equals("PRICE_C2"))				price_C2 = Double.parseDouble(dbValue);
			if (dbName.equals("PRICE_C3"))				price_C3 = Double.parseDouble(dbValue);
			if (dbName.equals("REPEATED_COUNT"))		repeatedCount = Integer.parseInt(dbValue);
			if (dbName.equals("TEXTUAL_COMMENT"))		textualComment = dbValue;
			if (dbName.equals("MOD_DATE"))				modDate = MainLibrary.StringToDateTime(dbValue);
		}

		//..........................................................................................
		//Import routine
		public boolean importToDB()
		{
			return insertToDB();
		}

		//..........................................................................................
		//Insert routine
		public boolean insertToDB()
		{
			try
			{
				//Insert record
				ContentValues cv = new ContentValues();
				cv.put("SET_DATE", MainLibrary.DateToStringSqlCustom(setDate));
				cv.put("CUATM", cuatm);
				cv.put("CODE_UNIT", codeUnit);
				cv.put("CODE_PROD", codeProd);
				cv.put("CODE_ASSORT", codeAsrt);
				cv.put("NAME", name);
				cv.put("QUANTITY", quantity);
				cv.put("ASSORT_COUNTRY", asrtCountry);
				cv.put("ASSORT_COMPANY", asrtCompany);
				cv.put("ASSORT_BRAND", asrtBrand);
				cv.put("ID_DATA_STATUS", idDataStatus);
				cv.put("PRICE_P1", price_P1);
				cv.put("PRICE_P2", price_P2);
				cv.put("PRICE_P3", price_P3);
				cv.put("PRICE_C1", price_C1);
				cv.put("PRICE_C2", price_C2);
				cv.put("PRICE_C3", price_C3);
				cv.put("REPEATED_COUNT", repeatedCount);
				cv.put("TEXTUAL_COMMENT", textualComment);
				cv.put("MOD_DATE", MainLibrary.DateTimeToStringSql(modDate));
				Database.db.insert("DATA_PRICE_VW", null, cv);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}

		//..........................................................................................
		//Update routine
		public boolean updateToDB()
		{
			try
			{
				//Update record
				ContentValues cv = new ContentValues();
				cv.put("NAME", name);
				cv.put("QUANTITY", quantity);
				cv.put("ASSORT_COUNTRY", asrtCountry);
				cv.put("ASSORT_COMPANY", asrtCompany);
				cv.put("ASSORT_BRAND", asrtBrand);
				cv.put("MOD_DATE", MainLibrary.DateTimeToStringSql(modDate));
				
				//Where values
				String whereStr = "SET_DATE = ? AND CUATM = ? AND CODE_UNIT = ? AND CODE_PROD = ? AND CODE_ASSORT = ?";
			    String[] whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDate), cuatm, codeUnit, codeProd, codeAsrt };
				
				//Execute query
			    Database.db.update("DATA_PRICE_VW", cv, whereStr, whereArgs);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}

		//..........................................................................................
		//Update routine
		public boolean deleteFromDB()
		{
			try
			{
				//Where values
				String whereStr = "SET_DATE = ? AND CUATM = ? AND CODE_UNIT = ? AND CODE_PROD = ? AND CODE_ASSORT = ?";
			    String[] whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDate), cuatm, codeUnit, codeProd, codeAsrt };
				
				//Execute query
			    Database.db.delete("DATA_PRICE_VW", whereStr, whereArgs);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		
		//..........................................................................................
		//Get current price
		public Double getPriceC()
		{
			if (decade == DBUserInfo.DECADE1) return price_C1;
			if (decade == DBUserInfo.DECADE2) return price_C2;
			if (decade == DBUserInfo.DECADE3) return price_C3;
			return 0.0;
		}
		
		//..........................................................................................
		//Get previous price
		public Double getPriceP()
		{
			if (decade == DBUserInfo.DECADE1)
			{
				if (collectPerMonth == 1) return price_P1;
				if (collectPerMonth == 2) return price_P2;
				if (collectPerMonth == 3) return price_P3;
			}
			if (decade == DBUserInfo.DECADE2)
			{
				if (collectPerMonth == 1) return price_P2; //modified: 2013.11.06
				else return price_C1;
			}
			if (decade == DBUserInfo.DECADE3) return price_C2;
			return 0.0;
		}
		
		//..........................................................................................
		//Get price index for the specific decade (!)
		public boolean isHighVariaty()
		{
			if (consistsFrom != consistsFromNonZero) return false;
			if (!codeAsrt.equals("0") || variaty <= 0.0 || getPriceC() == 0.0 || getPriceP() == 0.0) return false;
			Double index = 100.0 - 100.0 * getPriceC() / getPriceP();
			return (Math.abs(index) > variaty);
		}
		
		//..........................................................................................
		//Identifies if the product item is remained for introduction for the specific decade (!)
		public boolean isRemained()
		{
			//Only for the product level
			boolean res = false;
			if (!codeAsrt.equals("0")) return res;
			
			//Calculate result
			if (!res && idDataStatus == 0 && consistsFromNonZero < consistsFrom) res = true; //none
			if (!res && idDataStatus == 1 && consistsFromNonZero < consistsFrom) res = true; //Start season
			if (!res && idDataStatus == 4 && consistsFromNonZero < consistsFrom) res = true; //New assortment
			if (!res && idDataStatus == 6 && consistsFromNonZero < consistsFrom) res = true; //Quality change
			
			//Return result
			return res;
		}
		
		//..........................................................................................
		//Identifies if the product item has errors for the specific decade (!!!!!!!!!!!!!!!!!!!!!!)
		public boolean isErrored()
		{
			//Only for the product level
			boolean res = false;
			if (!codeAsrt.equals("0")) return res;
			
			//Calculate result
			if (!res && idDataStatus == 0 && (getPriceC() != 0 && getPriceP() == 0))	res = true; //none
			if (!res && idDataStatus == 1 && (getPriceC() == 0 || getPriceP() != 0))	res = true; //Start season
			if (!res && idDataStatus == 2 && (getPriceC() != 0 ))						res = true; //End season
			if (!res && idDataStatus == 3 && (getPriceC() != 0 || repeatedCount>=3))	res = true; //Temporary absent
			if (!res && idDataStatus == 4 && (getPriceC() == 0 || getPriceP() != 0))	res = true; //New assortment
			if (!res && idDataStatus == 5 && (getPriceC() != 0 ))						res = true; //Excluded assortment
			if (!res && idDataStatus == 6 && (getPriceC() == 0 || getPriceP() == 0))	res = true; //Quality change
			
			//Return result
			return res;
		}
		
		//..........................................................................................
		//Set price for the specific product in the current period
		public double submitPrice(double val)
		{
			try
			{
				//Current month - Where clause
				String whereStr = "SET_DATE = ? AND CUATM = ? AND CODE_UNIT = ? AND CODE_PROD = ? AND CODE_ASSORT = ?";
			    String[] whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDate), cuatm, codeUnit, codeProd, codeAsrt };
			    
				//Current month - Set data
				ContentValues cv = new ContentValues();
				if (decade == DBUserInfo.DECADE1) { cv.put("PRICE_C1", val); price_C1 = val; }
				if (decade == DBUserInfo.DECADE2) { cv.put("PRICE_C2", val); price_C2 = val; }
				if (decade == DBUserInfo.DECADE3) { cv.put("PRICE_C3", val); price_C3 = val; }
				
				cv.put("MOD_DATE", MainLibrary.DateTimeToStringSql(MainLibrary.CurrentDate()));
				modDate = MainLibrary.CurrentDate();
				
				//Current month - Update data
				Database.db.update("DATA_PRICE_VW", cv, whereStr, whereArgs);
				
				//Next month - Where clause
				whereStr = "SET_DATE = ? AND CUATM = ? AND CODE_UNIT = ? AND CODE_PROD = ? AND CODE_ASSORT = ?";
			    whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(MainLibrary.DateAddMonth(setDate, 1)), cuatm, codeUnit, codeProd, codeAsrt };
			    
				//Next month - Set data
				cv.clear();
				if (decade == DBUserInfo.DECADE1) { cv.put("PRICE_P1", val); }
				if (decade == DBUserInfo.DECADE2) { cv.put("PRICE_P2", val); }
				if (decade == DBUserInfo.DECADE3) { cv.put("PRICE_P3", val); }
				
				//Next month - Update data
				Database.db.update("DATA_PRICE_VW", cv, whereStr, whereArgs);
				
				//Return updated value
				return val;
			}
			catch(Exception e)
			{
				return 0;
			}
		}
		
		//..........................................................................................
		//Set comment for the specific product in the current period
		public int submitComment(int idDataStatusSet, String textualCommentSet)
		{
			try
			{
				//Current month - Where clause
				String whereStr = "SET_DATE = ? AND CUATM = ? AND CODE_UNIT = ? AND CODE_PROD = ? AND CODE_ASSORT = '0'";
			    String[] whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDate), cuatm, codeUnit, codeProd };
			    
				//Current month - Set data
				ContentValues cv = new ContentValues();
				cv.put("ID_DATA_STATUS", idDataStatusSet);										idDataStatus = idDataStatusSet;
				cv.put("TEXTUAL_COMMENT", textualCommentSet);									textualComment = textualCommentSet;
				cv.put("MOD_DATE", MainLibrary.DateTimeToStringSql(MainLibrary.CurrentDate()));	modDate = MainLibrary.CurrentDate();
				
				//Current month - Update data
				Database.db.update("DATA_PRICE_VW", cv, whereStr, whereArgs);
				
				//Return updated value
				return idDataStatusSet;
			}
			catch(Exception e)
			{
				return 0;
			}
		}
		//..........................................................................................
	}
	
	//----------------------------------------------------------------------------------------------
	public List<ItemClass> list;
	public int maxCollectPerMonth; 
	
	public static final int	FILTER_ALL = 1;
	public static final int	FILTER_REMAINED = 2;
	public static final int	FILTER_ERRORS = 3;
	
	public static final int	SORT_BY_CODE = 1;
	public static final int	SORT_BY_DATE = 2;
	public static final int	SORT_BY_NAME = 3;
	
	//----------------------------------------------------------------------------------------------
	public DBDataPrices() 
	{
		list = new ArrayList<ItemClass>();
		maxCollectPerMonth = 0;
	}

	//----------------------------------------------------------------------------------------------
	//Delete all data (static function)
	public static boolean dropData(Date setDateDelete)
	{
		try
		{
			//Current month - Where clause
			String whereStr = "SET_DATE = ? AND CUATM = ?";
		    String[] whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDateDelete), DBUserInfo.cuatm };
		    
		    Database.db.delete("DATA_PRICE_VW", whereStr, whereArgs);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//Copies data from one month to another
	public boolean copyPreviousData(Date setDateFrom, Date setDateTo) 
	{
		try
		{
			//SQL Text
			String sqlText = 
				"INSERT INTO DATA_PRICE_VW "+
				"( "+
				"  SET_DATE, "+
				"  CUATM, "+
				"  CODE_UNIT, "+
				"  CODE_PROD, "+
				"  CODE_ASSORT, "+
				"  NAME, "+
				"  QUANTITY, "+
				"  ASSORT_COUNTRY, "+
				"  ASSORT_COMPANY, "+
				"  ASSORT_BRAND, "+
				"  ID_DATA_STATUS, "+
				"  PRICE_P1, "+
				"  PRICE_P2, "+
				"  PRICE_P3, "+
				"  PRICE_C1, "+
				"  PRICE_C2, "+
				"  PRICE_C3, "+
				"  REPEATED_COUNT, "+
				"  TEXTUAL_COMMENT, "+
				"  MOD_DATE "+
				") "+
				"SELECT "+
				"  ? AS SET_DATE, "+
				"  D.CUATM, "+
				"  D.CODE_UNIT, "+
				"  D.CODE_PROD, "+
				"  D.CODE_ASSORT, "+
				"  D.NAME, "+
				"  D.QUANTITY, "+
				"  D.ASSORT_COUNTRY, "+
				"  D.ASSORT_COMPANY, "+
				"  D.ASSORT_BRAND, "+
				"  (CASE WHEN DP.ID_DATA_STATUS IN(2) AND D.CODE_ASSORT='0' THEN D.ID_DATA_STATUS ELSE 0 END) ID_DATA_STATUS, "+
				"  (CASE WHEN DP.ID_DATA_STATUS IN(2,3) THEN D.PRICE_P1 ELSE D.PRICE_C1 END) AS PRICE_P1, "+
				"  (CASE WHEN DP.ID_DATA_STATUS IN(2,3) THEN D.PRICE_P2 ELSE D.PRICE_C2 END) AS PRICE_P2, "+
				"  (CASE WHEN DP.ID_DATA_STATUS IN(2,3) THEN D.PRICE_P3 ELSE D.PRICE_C3 END) AS PRICE_P3, "+
				"  0 AS PRICE_C1, "+
				"  0 AS PRICE_C2, "+
				"  0 AS PRICE_C3, "+
				"  (CASE WHEN DP.ID_DATA_STATUS IN(3) AND D.CODE_ASSORT='0' THEN D.REPEATED_COUNT+1 ELSE 0 END) AS REPEATED_COUNT, "+
				"  '' as TEXTUAL_COMMENT, "+
				"  D.MOD_DATE "+
				"FROM "+
				"  DATA_PRICE_VW D  INNER JOIN "+
				"  DATA_PRICE_VW DP "+
				"  ON "+
				"  ( "+
				"    D.SET_DATE=DP.SET_DATE "+
				"    AND D.CUATM=DP.CUATM "+
				"    AND D.CODE_UNIT=DP.CODE_UNIT "+
				"    AND D.CODE_PROD=DP.CODE_PROD "+
				"    AND DP.CODE_ASSORT='0' "+
				"  ) "+
				"WHERE "+
				"  D.SET_DATE=? AND "+
				"  D.CUATM=? AND "+
				"  DP.ID_DATA_STATUS NOT IN(5) "
			;
	
			//Where clause
			String[] whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDateTo), MainLibrary.DateToStringSqlCustom(setDateFrom), DBUserInfo.cuatm };
			
			//Execute query
			Database.db.execSQL(sqlText, whereArgs);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	//----------------------------------------------------------------------------------------------
	public void loadData(int loadFilter, int sortBy) 
	{
		loadData(loadFilter, sortBy, DBUserInfo.setDate, DBUserInfo.currentCodeUnit);
	}
	
	//----------------------------------------------------------------------------------------------
	public void loadData(int loadFilter, int sortBy, Date setDateFilter) 
	{
		loadData(loadFilter, sortBy, setDateFilter, "");
	}
	
	//----------------------------------------------------------------------------------------------
	public void loadData(int loadFilter, int sortBy, Date setDateFilter, String codeUnitFilter) 
	{
		loadData(loadFilter, sortBy, setDateFilter, codeUnitFilter, "", "");
	}
	
	//----------------------------------------------------------------------------------------------
	public void loadData(int loadFilter, int sortBy, Date setDateFilter, String codeUnitFilter, String codeProdFilter, String codeAsrtFilter) 
	{
		//Set variables
		list = new ArrayList<ItemClass>();

		//Where clause
		String whereStr;
		String[] whereArgs;
		if (codeUnitFilter.equals(""))
		{
			whereStr = "DP.SET_DATE = ? AND DP.CUATM = ?";
			whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDateFilter), DBUserInfo.cuatm };
		}
		else if (codeProdFilter.equals(""))
		{
			whereStr = "DP.SET_DATE = ? AND DP.CUATM = ? AND DP.CODE_UNIT = ?";
			whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDateFilter), DBUserInfo.cuatm, codeUnitFilter };
		}
		else
		{
			whereStr = "DP.SET_DATE = ? AND DP.CUATM = ? AND DP.CODE_UNIT = ? AND DP.CODE_PROD = ? AND DP.CODE_ASSORT = ?";
			whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDateFilter), DBUserInfo.cuatm, codeUnitFilter, codeProdFilter, codeAsrtFilter };
		}
		
		//Product collect-per-month condition
		String whereCollectPerMonth = "";
		int decadeFilter = setDateFilter.getDate();
		if (decadeFilter == DBUserInfo.DECADE1) whereCollectPerMonth = " AND CP.COLLECT_PER_MONTH >= 2 "; //modified: 2013.11.06
		if (decadeFilter == DBUserInfo.DECADE2) whereCollectPerMonth = " AND CP.COLLECT_PER_MONTH >= 1 "; //modified: 2013.11.06
		if (decadeFilter == DBUserInfo.DECADE3) whereCollectPerMonth = " AND CP.COLLECT_PER_MONTH >= 3 ";
		
		//Order by clause
		String orderBy;
		if (sortBy == SORT_BY_DATE)			orderBy = "DP.CUATM, DP.CODE_UNIT, DPr.MOD_DATE, DP.CODE_PROD, DP.CODE_ASSORT";
		else if (sortBy == SORT_BY_NAME)	orderBy = "DP.CUATM, DP.CODE_UNIT, CP.NAME,      DP.CODE_PROD, DP.CODE_ASSORT";
		else								orderBy = "DP.CUATM, DP.CODE_UNIT,               DP.CODE_PROD, DP.CODE_ASSORT";
		
		//SQL Text
		String sqlText = 
			" SELECT DISTINCT "+
			"   DP.SET_DATE as SET_DATE, "+
			"   DP.CUATM as CUATM, "+
			"   DP.CODE_UNIT as CODE_UNIT, "+
			"   DP.CODE_PROD as CODE_PROD, "+
			"   DP.CODE_ASSORT as CODE_ASSORT, "+
			"   CP.NAME AS NAME_PROD, "+
			"   DP.NAME as NAME, "+
			"   DP.QUANTITY as QUANTITY, "+
			"   CP.ID_MESUREMENT_UNIT as ID_MESUREMENT_UNIT, "+
			"   CP.MESUREMENT_UNIT_SHORT as MESUREMENT_UNIT_SHORT, "+
			"   CP.ID_PROD_TYPE as ID_PROD_TYPE, "+
			"   CP.PROD_TYPE as PROD_TYPE, "+
			"   DP.ASSORT_COUNTRY as ASSORT_COUNTRY, "+
			"   DP.ASSORT_COMPANY as ASSORT_COMPANY, "+
			"   DP.ASSORT_BRAND as ASSORT_BRAND, "+
			"   CP.COLLECT_PER_MONTH as COLLECT_PER_MONTH, "+
			"   CP.VARIATY as VARIATY, "+
			"   DP.ID_DATA_STATUS as ID_DATA_STATUS, "+
			"   DP.PRICE_P1 as PRICE_P1, "+
			"   DP.PRICE_P2 as PRICE_P2, "+
			"   DP.PRICE_P3 as PRICE_P3, "+
			"   DP.PRICE_C1 as PRICE_C1, "+
			"   DP.PRICE_C2 as PRICE_C2, "+
			"   DP.PRICE_C3 as PRICE_C3, "+
			"   DP.REPEATED_COUNT as REPEATED_COUNT, "+
			"   DP.TEXTUAL_COMMENT as TEXTUAL_COMMENT, "+
			"   DP.MOD_DATE as MOD_DATE, "+
			"   DPr.MOD_DATE as MOD_DATE_P "+
			" FROM "+
			"   CLS_PRODUCT_VW AS CP "+
			"   INNER JOIN DATA_PRICE_VW DP   ON(CP.CODE_PROD = DP.CODE_PROD) "+
			"   LEFT  JOIN DATA_PRICE_VW DPr  ON(DP.SET_DATE = DPr.SET_DATE AND DP.CUATM = DPr.CUATM AND DP.CODE_UNIT = DPr.CODE_UNIT AND DP.CODE_PROD = DPr.CODE_PROD AND DPr.CODE_ASSORT = '0') "+
			" WHERE "+
			"   "+whereStr+" "+whereCollectPerMonth+
			" ORDER BY "+
			"   "+orderBy+" "
		;
		
		//Prepare data
		list.clear();
		ItemClass item;
		Cursor dbc;
		int i = 0;
		
		//Execute query
		dbc = Database.db.rawQuery(sqlText, whereArgs);
		if (dbc.moveToFirst()) do
		{
			item = new ItemClass();
			item.parent = this;
			item.setDate = MainLibrary.StringToDateSql(dbc.getString(dbc.getColumnIndex("SET_DATE")));
			item.cuatm = dbc.getString(dbc.getColumnIndex("CUATM"));
			item.codeUnit = dbc.getString(dbc.getColumnIndex("CODE_UNIT"));
			item.codeProd = dbc.getString(dbc.getColumnIndex("CODE_PROD"));
			item.codeAsrt = dbc.getString(dbc.getColumnIndex("CODE_ASSORT"));
			item.name = dbc.getString(dbc.getColumnIndex("NAME"));
			item.quantity = dbc.getDouble(dbc.getColumnIndex("QUANTITY"));
			item.idMesurementUnit = dbc.getLong(dbc.getColumnIndex("ID_MESUREMENT_UNIT"));
			item.mesurementUnitShort = dbc.getString(dbc.getColumnIndex("MESUREMENT_UNIT_SHORT"));
			item.idProdType = dbc.getLong(dbc.getColumnIndex("ID_PROD_TYPE"));
			item.prodType = dbc.getString(dbc.getColumnIndex("PROD_TYPE"));
			item.asrtCountry = dbc.getString(dbc.getColumnIndex("ASSORT_COUNTRY"));
			item.asrtCompany = dbc.getString(dbc.getColumnIndex("ASSORT_COMPANY"));
			item.asrtBrand = dbc.getString(dbc.getColumnIndex("ASSORT_BRAND"));
			item.collectPerMonth = dbc.getInt(dbc.getColumnIndex("COLLECT_PER_MONTH"));
			item.variaty = dbc.getDouble(dbc.getColumnIndex("VARIATY"));
			item.idDataStatus = dbc.getLong(dbc.getColumnIndex("ID_DATA_STATUS"));
			item.price_P1 = dbc.getDouble(dbc.getColumnIndex("PRICE_P1"));
			item.price_P2 = dbc.getDouble(dbc.getColumnIndex("PRICE_P2"));
			item.price_P3 = dbc.getDouble(dbc.getColumnIndex("PRICE_P3"));
			item.price_C1 = dbc.getDouble(dbc.getColumnIndex("PRICE_C1"));
			item.price_C2 = dbc.getDouble(dbc.getColumnIndex("PRICE_C2"));
			item.price_C3 = dbc.getDouble(dbc.getColumnIndex("PRICE_C3"));
			item.repeatedCount = dbc.getInt(dbc.getColumnIndex("REPEATED_COUNT"));
			item.textualComment = dbc.getString(dbc.getColumnIndex("TEXTUAL_COMMENT"));
			item.modDate = MainLibrary.StringToDateTimeSql(dbc.getString(dbc.getColumnIndex("MOD_DATE")));
			
			item.decade = setDateFilter.getDate();
			
			list.add(i++,item);
			
		} while (dbc.moveToNext());
		dbc.close();
		
		autocalcValues();
		applyFilter(loadFilter);
	}
	
	//----------------------------------------------------------------------------------------------
	//Auto-calculate values
	private void autocalcValues() 
	{
		int i = 0;
		
		//Set auto-calculate values
		maxCollectPerMonth = 0;
		ItemClass productItem = null;
		for (ItemClass item: list)
		{
			//Product item
			if (item.codeAsrt.equals("0"))
			{
				//Reset product links
				productItem = item;
				productItem.consistsFrom = 0;
				productItem.consistsFromNonZero = 0;
				if (item.collectPerMonth > maxCollectPerMonth) maxCollectPerMonth = item.collectPerMonth;
			}
			//Assortment item
			else if (productItem != null) 
			{
				productItem.consistsFrom++;
				if (item.getPriceC() != 0) productItem.consistsFromNonZero++;
			}
			item.position = i++;
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//Apply list filter
	private void applyFilter(int loadFilter)
	{
		//Exit in case if all records are needed
		if (loadFilter == FILTER_ALL) return;
		
		int i = 0;
		ItemClass item = null;
		boolean isErrored = false;
		boolean isRemained = false;
		
		while (i < list.size())
		{
			item = list.get(i);
			
			//Check the product
			if (item.codeAsrt.equals("0"))
			{
				isErrored = item.isErrored();
				isRemained = item.isRemained();
			}
			
			//Apply filter
			if ((loadFilter == FILTER_ERRORS && !isErrored) || (loadFilter == FILTER_REMAINED && !isRemained)) list.remove(item);
			else item.position = i++;
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//Checks if exits data for the specific date
	public boolean existData(Date setDateCheck) 
	{
		//Prepare data
		boolean existDataReult = false;
		Cursor dbc;
		
		//SQL Text
		String sqlText = "SELECT * FROM DATA_PRICE_VW WHERE SET_DATE=? AND CUATM=? LIMIT 1";

		//Where clause
		String[] whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(setDateCheck), DBUserInfo.cuatm };
		
		//Execute query
		dbc = Database.db.rawQuery(sqlText, whereArgs);
		if (dbc.moveToFirst()) existDataReult = true;
		dbc.close();
		
		//Return result
		return existDataReult;
	}
	
	//----------------------------------------------------------------------------------------------
	//Checks if exits errored data
	public boolean existErrors() 
	{
		for (ItemClass item: list)
		{
			if (item.isErrored()) return true;
		}
		return false;
	}
	
	//----------------------------------------------------------------------------------------------
	//Checks if exits not completed data
	public boolean existRemained() 
	{
		for (ItemClass item: list)
		{
			if (item.isRemained()) return true;
		}
		return false;
	}
	
	//----------------------------------------------------------------------------------------------
	public List<String> getDatasets() 
	{
		//Prepare data
		List<String> dataList = new ArrayList<String>();
		Cursor dbc;
		int i = 0;
		
		//Execute query
		dbc = Database.db.rawQuery("SELECT DISTINCT SET_DATE  FROM DATA_PRICE_VW  ORDER BY SET_DATE", null);
		if (dbc.moveToFirst()) do
		{
			Date setDateTmp = MainLibrary.StringToDateSql(dbc.getString(dbc.getColumnIndex("SET_DATE")));
			dataList.add(i++, MainLibrary.DateToString(setDateTmp));
			
		} while (dbc.moveToNext());
		dbc.close();
		
		return dataList;
	}
	
	//----------------------------------------------------------------------------------------------
	//Gets item from the loaded list by the key values
	public ItemClass getItem(String codeUnitFind, String codeProdFind, String codeAsrtFind) 
	{
		for (ItemClass item: list)
		{
			if (item.codeUnit.equals(codeUnitFind) && 
					item.codeProd.equals(codeProdFind) && 
					item.codeAsrt.equals(codeAsrtFind)) return item;
		}
		return null;
	}
	
	//----------------------------------------------------------------------------------------------
	//Gets the current item from the loaded list
	public ItemClass getItemCurrent() 
	{
		return getItem(DBUserInfo.currentCodeUnit, DBUserInfo.currentCodeProd, DBUserInfo.currentCodeAstr);
	}
	
	//----------------------------------------------------------------------------------------------
	//Gets the current product item from the loaded list
	public ItemClass getItemCurrentProd() 
	{
		return getItem(DBUserInfo.currentCodeUnit, DBUserInfo.currentCodeProd, "0");
	}
	
	//----------------------------------------------------------------------------------------------
	//Gets next assortment item (if no - returns null)
	public ItemClass getItemNextAssort() 
	{
		ItemClass itemCurrent = null;
		
		for (ItemClass item: list)
		{
			if (item.codeUnit.equals(DBUserInfo.currentCodeUnit) && 
					item.codeProd.equals(DBUserInfo.currentCodeProd) && 
					item.codeAsrt.equals(DBUserInfo.currentCodeAstr)) itemCurrent = item;
			
			if (itemCurrent != null && 
					!item.codeAsrt.equals("0") &&
					item.position > itemCurrent.position) return item;
		}
		return null;
	}
	
	//----------------------------------------------------------------------------------------------
	//Gets previous assortment item (if no - returns null)
	public ItemClass getItemPrevAssort() 
	{
		ItemClass item = null;
		ItemClass itemCurrent = null;
		
		for (int i = list.size()-1; i >= 0; i--)
		{
			item = list.get(i);
			
			if (item != null &&
					item.codeUnit.equals(DBUserInfo.currentCodeUnit) && 
					item.codeProd.equals(DBUserInfo.currentCodeProd) && 
					item.codeAsrt.equals(DBUserInfo.currentCodeAstr)) itemCurrent = item;
			
			if (itemCurrent != null && 
					!item.codeAsrt.equals("0") &&
					item.position < itemCurrent.position) return item;
		}
		return null;
	}
	
	//----------------------------------------------------------------------------------------------
	//Gets first item from the loaded list by the search values
	@SuppressLint("DefaultLocale")
	public ItemClass findItem(String codeFind, String nameFind) 
	{
    	//Search for item
    	for (ItemClass item: list)
    	{
    		if ((!codeFind.equals("") || !nameFind.equals("")) && 
    				(codeFind.equals("") || item.codeProd.startsWith(codeFind)) && 
    				(nameFind.equals("") || item.name.toLowerCase().contains(nameFind.toLowerCase()))) return item;
    	}
		return null;
	}
	
	//----------------------------------------------------------------------------------------------
	//Insert the item to the database
	public boolean insertItem(ItemClass item)
	{
		if (item == null) return false;
		
		boolean isInserted = item.insertToDB();
		if (isInserted)
		{
			list.add(item);
			autocalcValues();
		}
		return isInserted;
	}
	
	//----------------------------------------------------------------------------------------------
	//Update the item to the database
	public boolean updateItem(ItemClass item)
	{
		if (item == null) return false;
		
		boolean isUpdated = item.updateToDB();
		ItemClass updItem = getItem(item.codeUnit, item.codeProd, item.codeAsrt);
		if (isUpdated && updItem != null) list.add(updItem);
		if (isUpdated) autocalcValues();
		return isUpdated;
	}
	
	//----------------------------------------------------------------------------------------------
	//Sets the price for the specific item
	public boolean deleteItem(ItemClass item)
	{
		//Don't delete product item directly
		if (item == null || item.codeAsrt.equals("0")) return false;
		
		String codeUnitDel = item.codeUnit;
		String codeProdDel = item.codeProd;
		
		//Remove from the database
		boolean isDelited = item.deleteFromDB();
		
		if (isDelited)
		{
			//Remove item from the list
			list.remove(item.position);
			item = null;
			
			//Count product assortment remained items
			int itemProdCount = 0;
			ItemClass itemProd = null;
			for (ItemClass itemTmp: list)
			{
				if (itemTmp.codeUnit.equals(codeUnitDel) && 
						itemTmp.codeProd.equals(codeProdDel) && 
						itemTmp.codeAsrt.equals("0")) itemProd = itemTmp;
				
				if (itemTmp.codeUnit.equals(codeUnitDel) && 
						itemTmp.codeProd.equals(codeProdDel) && 
						!itemTmp.codeAsrt.equals("0")) itemProdCount++;
			}
			
			//If product doesn't have items - delete it too
			if (itemProdCount == 0 && itemProd != null)
			{
				boolean itemProdIsDeleted = itemProd.deleteFromDB();
				if (itemProdIsDeleted) { list.remove(itemProd.position); itemProd = null; }
			}
			
			autocalcValues();
		}
		return isDelited;
	}
	
	//----------------------------------------------------------------------------------------------
	//Sets the price for the specific item
	public void setItemPrice(ItemClass item, double val)
	{
		//Don't set price for non-assortment items
		if (item.codeAsrt.equals("0")) return;
		
		//Find the product or stop procedure
		ItemClass itemProd = getItem(item.codeUnit, item.codeProd, "0");
		if (itemProd == null) return;
		
		//Set assortment price
		item.submitPrice(val);
		
		//Calculate geometric mean for the product
		double priceCount = 0;
		double priceMult = 1;
		for (ItemClass itemTmp: list)
		{
			if (itemTmp.codeUnit.equals(item.codeUnit) && itemTmp.codeProd.equals(item.codeProd) && !itemTmp.codeAsrt.equals("0"))
			{
				if (itemTmp.getPriceC() != 0.0 && itemTmp.quantity != 0.0) 
				{
					priceMult = priceMult * (itemTmp.getPriceC() * itemProd.quantity / itemTmp.quantity);
					priceCount++;
				}
			}
		}
		double priceMean = 0.0;
		if (priceCount > 0.0) priceMean = MainLibrary.round(Math.pow(priceMult, 1.0/priceCount), 2);
		
		//Set product price
		itemProd.submitPrice(priceMean);
		autocalcValues();
	}
	
	//----------------------------------------------------------------------------------------------
	//Sets the price for the specific item
	public void setItemComment(ItemClass item, int idDataStatusSet, String textualCommentSet)
	{
		//Don't set comment for assortment items
		if (!item.codeAsrt.equals("0")) return;
		
		//Set product comment
		item.submitComment(idDataStatusSet, textualCommentSet);
		autocalcValues();
	}
	//----------------------------------------------------------------------------------------------
}
