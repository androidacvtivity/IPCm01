package md.statistica.ipc.ipcm_v01;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public class DBRegUnits 
{
	//----------------------------------------------------------------------------------------------
	//Class which describes one row of the owner class
	public static class ItemClass 
	{
		public String	cuatm;
		public String	code;
		public Date		version;
		public String	cuatmName;
		public String	name;
		public String	adress;
		public String	phone;
		public String	email;
		public String	www;
		public String	schedule;
		public String	contactPerson;
		public long		idUnitType;
		public String	unitType;
		public long		idUnitStatus;
		public String	unitStatus;
		
		//OnLoad calculated values
		public int	position;
		public int	prodCount;
		public int	prodCountNonZero;
		public int	prodCountRemained;
		public int	prodCountErrored;

		//..........................................................................................
		public ItemClass()
		{
			cuatm = "";
			code = "";
			version = new Date(0);
			cuatmName = "";
			name = "";
			adress = "";
			phone = "";
			email = "";
			www = "";
			schedule = "";
			contactPerson = "";
			idUnitType = 0;
			unitType = "";
			idUnitStatus = 0;
			unitStatus = "";

			position = 0;
			prodCount = 0;
			prodCountNonZero = 0;
			prodCountRemained = 0;
			prodCountErrored = 0;
		}
		
		public String getContactInfo()
		{
			return "Adresa: " + adress 
					+ (phone.equals("") ? "" : ", tel.: " + phone) 
					+ (schedule.equals("") ? "" : ", orar: " + schedule);
		}

		//..........................................................................................
		//Import routine
		public void importValue(String dbName, String dbValue)
		{
			if (dbName.equals("CUATM"))				cuatm = dbValue;
			if (dbName.equals("CODE_UNIT"))			code = dbValue;
			if (dbName.equals("CODE_UNIT_VERS"))	version = MainLibrary.StringToDate(dbValue);
			if (dbName.equals("CUATM_NAME"))		cuatmName = dbValue;
			if (dbName.equals("NAME"))				name = dbValue;
			if (dbName.equals("ADRESS"))			adress = dbValue;
			if (dbName.equals("PHONE"))				phone = dbValue;
			if (dbName.equals("EMAIL"))				email = dbValue;
			if (dbName.equals("WWW"))				www = dbValue;
			if (dbName.equals("SCHEDULE"))			schedule = dbValue;
			if (dbName.equals("CONACT_PERSON"))		contactPerson = dbValue;
			if (dbName.equals("ID_UNIT_TYPE"))		idUnitType = Long.parseLong(dbValue);
			if (dbName.equals("UNIT_TYPE"))			unitType = dbValue;
			if (dbName.equals("ID_UNIT_STATUS"))	idUnitStatus = Long.parseLong(dbValue);
			if (dbName.equals("UNIT_STATUS"))		unitStatus = dbValue;
		}

		//..........................................................................................
		//Import routine
		public boolean importToDB()
		{
			try
			{
				//Insert record
				ContentValues cv = new ContentValues();
				cv.put("CUATM", cuatm);
				cv.put("CODE_UNIT", code);
				cv.put("CODE_UNIT_VERS", MainLibrary.DateToStringSql(version));
				cv.put("CUATM_NAME", cuatmName);
				cv.put("NAME", name);
				cv.put("ADRESS", adress);
				cv.put("PHONE", phone);
				cv.put("EMAIL", email);
				cv.put("WWW", www);
				cv.put("SCHEDULE", schedule);
				cv.put("CONACT_PERSON", contactPerson);
				cv.put("ID_UNIT_TYPE", idUnitType);
				cv.put("UNIT_TYPE", unitType);
				cv.put("ID_UNIT_STATUS", idUnitStatus);
				cv.put("UNIT_STATUS", unitStatus);
				Database.db.insert("REG_UNIT_VW", null, cv);
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
	
	public static final int	FILTER_ACTIVE = 1;
	public static final int	FILTER_REMAINED = 2;
	public static final int	FILTER_ERRORS = 3;
	public static final int	FILTER_ALL = 4;
	
	public static final int	SORT_BY_CODE = 1;
	public static final int	SORT_BY_NAME = 2;
	
	//----------------------------------------------------------------------------------------------
	public DBRegUnits() 
	{
		list = new ArrayList<ItemClass>();
	}

	//----------------------------------------------------------------------------------------------
	//Delete all data
	public static boolean dropData()
	{
		try
		{
		    Database.db.delete("REG_UNIT_VW", null, null);
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
		loadData(loadFilter, sortBy, "");
	}
	
	//----------------------------------------------------------------------------------------------
	public void loadData(int loadFilter, int sortBy, String codeUnitFilter) 
	{
		//Filters
		int decadeFilter = DBUserInfo.getDateSetDecade();
		String cuatmFilter = DBUserInfo.cuatm;
		
		//Current product price SQL Selection
		String PRICE_C = "0";
		if (decadeFilter == DBUserInfo.DECADE1) PRICE_C = "DP.PRICE_C1";
		if (decadeFilter == DBUserInfo.DECADE2) PRICE_C = "DP.PRICE_C2";
		if (decadeFilter == DBUserInfo.DECADE3) PRICE_C = "DP.PRICE_C3";

		//Previous product price SQL Selection
		String PRICE_P = "0";
		if (decadeFilter == DBUserInfo.DECADE1)
		{
			PRICE_P = 
			"(CASE "+
			" WHEN CP.COLLECT_PER_MONTH = 1 THEN DP.PRICE_P1 "+
			" WHEN CP.COLLECT_PER_MONTH = 2 THEN DP.PRICE_P2 "+
			" ELSE DP.PRICE_P3 END)";
		}
		if (decadeFilter == DBUserInfo.DECADE2)
		{
			PRICE_P = 
			"(CASE "+
			" WHEN CP.COLLECT_PER_MONTH = 1 THEN DP.PRICE_P2 "+ //modified: 2013.11.06
			" ELSE DP.PRICE_C1 END)";
		}
		if (decadeFilter == DBUserInfo.DECADE3) PRICE_P = "DP.PRICE_C2";
		
		//Product collect-per-month condition
		String whereCollectPerMonth = "";
		if (decadeFilter == DBUserInfo.DECADE1) whereCollectPerMonth = " AND CP.COLLECT_PER_MONTH >= 2 "; //modified: 2013.11.06
		if (decadeFilter == DBUserInfo.DECADE2) whereCollectPerMonth = " AND CP.COLLECT_PER_MONTH >= 1 "; //modified: 2013.11.06
		if (decadeFilter == DBUserInfo.DECADE3) whereCollectPerMonth = " AND CP.COLLECT_PER_MONTH >= 3 ";
		
		//Order by clause
		String orderBy;
		if (sortBy == SORT_BY_NAME) orderBy = "RU.CUATM, RU.NAME, RU.CODE_UNIT";
		else orderBy = "RU.CUATM, RU.CODE_UNIT";
		
		//SQL Text
		String sqlText = 
			" SELECT DISTINCT "+
			"   RU.CUATM as CUATM, "+
			"   RU.CODE_UNIT as CODE_UNIT, "+
			"   RU.CODE_UNIT_VERS as CODE_UNIT_VERS, "+
			"   RU.CUATM_NAME as CUATM_NAME, "+
			"   RU.NAME as NAME, "+
			"   RU.ADRESS as ADRESS, "+
			"   RU.PHONE as PHONE, "+
			"   RU.EMAIL as EMAIL, "+
			"   RU.WWW as WWW, "+
			"   RU.SCHEDULE as SCHEDULE, "+
			"   RU.CONACT_PERSON as CONACT_PERSON, "+
			"   RU.ID_UNIT_TYPE as ID_UNIT_TYPE, "+
			"   RU.ID_UNIT_STATUS as UNIT_TYPE, "+
			"   RU.CODE_UNIT_VERS as ID_UNIT_STATUS, "+
			"   RU.CODE_UNIT_VERS as UNIT_STATUS, "+
			"   DP.PROD_COUNT as PROD_COUNT, "+
			"   DP.PROD_COUNT_NON_ZERO as PROD_COUNT_NON_ZERO, "+
			"   DP.PROD_COUNT_REMAINED as PROD_COUNT_REMAINED, "+
			"   DP.PROD_COUNT_ERRORED as PROD_COUNT_ERRORED "+
			" FROM "+
			"   REG_UNIT_VW AS RU "+
			"   LEFT JOIN "+
			"   ( "+
			
			"     SELECT DISTINCT "+
			"       D.CUATM, "+
			"       D.CODE_UNIT, "+
			"       COUNT(DISTINCT D.CODE_PROD) as PROD_COUNT, "+
			"       COUNT(DISTINCT CASE WHEN (D.ID_DATA_STATUS NOT   IN(0)  OR D.ASSORT_COUNT_NON_ZERO = D.ASSORT_COUNT) THEN D.CODE_PROD ELSE NULL END) as PROD_COUNT_NON_ZERO, "+
			"       COUNT(DISTINCT CASE WHEN (D.ID_DATA_STATUS IN(0,1,4,6) AND D.ASSORT_COUNT_NON_ZERO < D.ASSORT_COUNT) THEN D.CODE_PROD ELSE NULL END) as PROD_COUNT_REMAINED, "+
			"       COUNT"+
			"       ( DISTINCT "+
			"         CASE "+
			"           WHEN (D.ID_DATA_STATUS IN(0) AND (D.PRICE_C <> 0 AND D.PRICE_P  = 0     )) THEN D.CODE_PROD "+
			"           WHEN (D.ID_DATA_STATUS IN(1) AND (D.PRICE_C  = 0  OR D.PRICE_P <> 0     )) THEN D.CODE_PROD "+
			"           WHEN (D.ID_DATA_STATUS IN(2) AND (D.PRICE_C <> 0                        )) THEN D.CODE_PROD "+
			"           WHEN (D.ID_DATA_STATUS IN(3) AND (D.PRICE_C <> 0  OR D.REPEATED_COUNT>=3)) THEN D.CODE_PROD "+
			"           WHEN (D.ID_DATA_STATUS IN(4) AND (D.PRICE_C  = 0  OR D.PRICE_P <> 0     )) THEN D.CODE_PROD "+
			"           WHEN (D.ID_DATA_STATUS IN(5) AND (D.PRICE_C <> 0                        )) THEN D.CODE_PROD "+
			"           WHEN (D.ID_DATA_STATUS IN(6) AND (D.PRICE_C  = 0  OR D.PRICE_P  = 0     )) THEN D.CODE_PROD "+
			"         ELSE NULL END "+
			"       ) as PROD_COUNT_ERRORED "+
			"     FROM "+
			"       ("+
			
			"         SELECT DISTINCT "+
			"           DP.CUATM, "+
			"           DP.CODE_UNIT, "+
			"           DP.CODE_PROD, "+
			"           MAX  (DP.REPEATED_COUNT) AS REPEATED_COUNT, "+
			"           MAX  (CP.COLLECT_PER_MONTH) AS COLLECT_PER_MONTH, "+
			"           MAX  (CASE WHEN CODE_ASSORT ='0' THEN DP.ID_DATA_STATUS ELSE 0 END) AS ID_DATA_STATUS, "+
			"           MAX  (CASE WHEN CODE_ASSORT ='0' THEN "+PRICE_C+" ELSE 0 END) AS PRICE_C, "+
			"           MAX  (CASE WHEN CODE_ASSORT ='0' THEN "+PRICE_P+" ELSE 0 END) AS PRICE_P, "+
			"           COUNT(CASE WHEN CODE_ASSORT<>'0' THEN DP.CODE_ASSORT ELSE NULL END) AS ASSORT_COUNT, "+
			"           COUNT(CASE WHEN CODE_ASSORT<>'0' AND  "+PRICE_C+"<>0 THEN DP.CODE_ASSORT ELSE NULL END) AS ASSORT_COUNT_NON_ZERO "+
			"         FROM "+
			"           CLS_PRODUCT_VW AS CP "+
			"           INNER JOIN DATA_PRICE_VW DP ON(CP.CODE_PROD = DP.CODE_PROD) "+
			"         WHERE "+
			"           DP.SET_DATE=? "+whereCollectPerMonth+
			"         GROUP BY "+
			"           DP.CUATM, "+
			"           DP.CODE_UNIT, "+
			"           DP.CODE_PROD "+
			
			"       ) D"+
			"     GROUP BY "+
			"       D.CUATM, "+
			"       D.CODE_UNIT "+
			"     "+
			
			"   ) DP ON(RU.CUATM=DP.CUATM AND RU.CODE_UNIT=DP.CODE_UNIT) "+
			" WHERE "+
			"   RU.CUATM=? "+
			"   AND ? IN(RU.CODE_UNIT, '') "+
			    (loadFilter == FILTER_ACTIVE   ? "   AND DP.CODE_UNIT IS NOT NULL " : "")+
			    (loadFilter == FILTER_REMAINED ? "   AND DP.CODE_UNIT IS NOT NULL AND DP.PROD_COUNT > DP.PROD_COUNT_NON_ZERO " : "")+
			    (loadFilter == FILTER_ERRORS   ? "   AND DP.CODE_UNIT IS NOT NULL AND DP.PROD_COUNT_ERRORED > 0 " : "")+
			" ORDER BY "+
			"   "+orderBy+" "
		;
		
		//Where clause
		String[] whereArgs;
		whereArgs = new String[] { MainLibrary.DateToStringSqlCustom(DBUserInfo.setDate), cuatmFilter, codeUnitFilter };
		
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
			item.cuatm = dbc.getString(dbc.getColumnIndex("CUATM"));
			item.code = dbc.getString(dbc.getColumnIndex("CODE_UNIT"));
			item.version = MainLibrary.StringToDateSql(dbc.getString(dbc.getColumnIndex("CODE_UNIT_VERS")));
			item.cuatmName = dbc.getString(dbc.getColumnIndex("CUATM_NAME"));
			item.name = dbc.getString(dbc.getColumnIndex("NAME"));
			item.adress = dbc.getString(dbc.getColumnIndex("ADRESS"));
			item.phone = dbc.getString(dbc.getColumnIndex("PHONE"));
			item.email = dbc.getString(dbc.getColumnIndex("EMAIL"));
			item.www = dbc.getString(dbc.getColumnIndex("WWW"));
			item.schedule = dbc.getString(dbc.getColumnIndex("SCHEDULE"));
			item.contactPerson = dbc.getString(dbc.getColumnIndex("CONACT_PERSON"));
			item.idUnitType = dbc.getLong(dbc.getColumnIndex("ID_UNIT_TYPE"));
			item.unitType = dbc.getString(dbc.getColumnIndex("UNIT_TYPE"));
			item.idUnitStatus = dbc.getLong(dbc.getColumnIndex("ID_UNIT_STATUS"));
			item.unitStatus = dbc.getString(dbc.getColumnIndex("UNIT_STATUS"));

			item.position = i;
			item.prodCount = dbc.getInt(dbc.getColumnIndex("PROD_COUNT"));
			item.prodCountNonZero = dbc.getInt(dbc.getColumnIndex("PROD_COUNT_NON_ZERO"));
			item.prodCountRemained = dbc.getInt(dbc.getColumnIndex("PROD_COUNT_REMAINED"));
			item.prodCountErrored = dbc.getInt(dbc.getColumnIndex("PROD_COUNT_ERRORED"));
			list.add(i++,item);
			
		} while (dbc.moveToNext());
		dbc.close();
	}
	
	//----------------------------------------------------------------------------------------------
	public ItemClass getItem(String codeFind) 
	{
		for (ItemClass item: list)
		{
			if (item.cuatm.equals(DBUserInfo.cuatm) && item.code.equals(codeFind)) return item;
		}
		return null;
	}
	
	//----------------------------------------------------------------------------------------------
	public ItemClass getItemCurrent() 
	{
		return getItem(DBUserInfo.currentCodeUnit);
	}
	
	//----------------------------------------------------------------------------------------------
	@SuppressLint("DefaultLocale")
	public ItemClass findItem(String codeFind, String nameFind) 
	{
    	//Search for item
    	for (ItemClass item: list)
    	{
    		if ((!codeFind.equals("") || !nameFind.equals("")) && 
    				(codeFind.equals("") || item.code.startsWith(codeFind)) && 
    				(nameFind.equals("") || item.name.toLowerCase().contains(nameFind.toLowerCase()))) return item;
    	}
		return null;
	}
	//----------------------------------------------------------------------------------------------
}
