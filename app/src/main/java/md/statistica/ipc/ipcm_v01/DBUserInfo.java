package md.statistica.ipc.ipcm_v01;

import java.util.Date;
import android.content.ContentValues;
import android.database.Cursor;

public class DBUserInfo 
{
	//----------------------------------------------------------------------------------------------
	//User global information
	public static long		idUser;
	public static long		idRole;
	public static String	roleName;
	
	public static String	login;
	public static String	pasHash;
	public static String	name;
	public static String	surname;
	public static String	adress;
	public static String	phone;
	public static String	email;
	
	public static Date		setDate;
	public static String	cuatm;
	public static String	cuatmName;
	
	//User local information
	public static boolean	isAuthorized;		//identified if user is authorized
	public static String	currentCodeUnit;	//code of the selected statistical unit
	public static String	currentCodeProd;	//code of the selected product
	public static String	currentCodeAstr;	//code of the selected assortment
	
	public static String	foundCodeUnit;		//found register unit
	public static String	foundCodeProd;		//found product item
	
	public static int 		filterRegUnit;		//filter settings for unit register
	public static int		filterDataPrice;	//filter settings for data prices
	public static int		sortDataPrice;		//sort settings for data prices
	public static int		sortRegUnit;		//sort settings for reg. units
	public static int		sortCountry;		//sort settings for countries in assortment
	public static int		sortProduct;		//sort settings for products in assortment
	
	public static final int	DECADE1 = 1;
	public static final int	DECADE2 = 11;
	public static final int	DECADE3 = 21;
	
	//----------------------------------------------------------------------------------------------
	public static void initialize() 
	{
		//Set default values
		idUser = 0;
		idRole = 0;
		roleName = "";
		
		login = "";
		pasHash = "";
		name = "";
		surname = "";
		adress = "";
		phone = "";
		email = "";
		
		setDate = new Date(0);
		cuatm = "";
		cuatmName = "";
		
		isAuthorized = false;
		currentCodeUnit = "0";
		currentCodeProd = "0";
		currentCodeAstr = "0";
		
		foundCodeUnit = "";
		foundCodeProd = "";
		
		filterRegUnit = DBRegUnits.FILTER_ACTIVE;
		filterDataPrice = DBDataPrices.FILTER_ALL;
		sortDataPrice = DBDataPrices.SORT_BY_CODE;
		sortRegUnit = DBRegUnits.SORT_BY_CODE;
		sortCountry = DBClsCounties.SORT_BY_CODE;
		sortProduct = DBClsProducts.SORT_BY_CODE;
		
		//Load database values
		loadData();
	}

	//----------------------------------------------------------------------------------------------
	//Import routine
	public static void importValue(String dbName, String dbValue)
	{
		if (dbName.equals("ID_USER"))		idUser = Long.parseLong(dbValue);
		if (dbName.equals("ID_ROLE"))		idRole = Long.parseLong(dbValue);
		if (dbName.equals("ROLE_NAME"))		roleName = dbValue;
		if (dbName.equals("LOGIN"))			login = dbValue;
		if (dbName.equals("PASS_HASH"))		pasHash = dbValue;
		if (dbName.equals("CUATM"))			cuatm = dbValue;
		if (dbName.equals("CUATM_NAME"))	cuatmName = dbValue;
		if (dbName.equals("NAME"))			name = dbValue;
		if (dbName.equals("SURNAME"))		surname = dbValue;
		if (dbName.equals("ADRESS"))		adress = dbValue;
		if (dbName.equals("PHONE"))			phone = dbValue;
		if (dbName.equals("EMAIL"))			email = dbValue;
	}

	//----------------------------------------------------------------------------------------------
	//Import routine
	public static boolean importToDB()
	{
		try
		{
			//Remember date settings
			Date setDateTmp = setDate;
			
			//Delete old record
			Database.db.delete("SYS_USER_VW", null, null);
			
			//Insert record
			ContentValues cv = new ContentValues();
			cv.put("ID_USER", idUser);
			cv.put("ID_ROLE", idRole);
			cv.put("ROLE_NAME", roleName);
			cv.put("LOGIN", login);
			cv.put("PASS_HASH", pasHash);
			cv.put("SET_DATE", MainLibrary.DateToStringSqlCustom(setDateTmp));
			cv.put("CUATM", cuatm);
			cv.put("CUATM_NAME", cuatmName);
			cv.put("NAME", name);
			cv.put("SURNAME", surname);
			cv.put("ADRESS", adress);
			cv.put("PHONE", phone);
			cv.put("EMAIL", email);
			Database.db.insert("SYS_USER_VW", null, cv);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	//----------------------------------------------------------------------------------------------
	private static void loadData()
	{
		//Load user data
		Cursor dbc = Database.db.query("SYS_USER_VW", null, null, null, null, null, null);
		if (dbc.moveToFirst())
		{
			idUser = dbc.getLong(dbc.getColumnIndex("ID_USER"));
			idRole = dbc.getLong(dbc.getColumnIndex("ID_ROLE"));
			roleName = dbc.getString(dbc.getColumnIndex("ROLE_NAME"));
			
			login = dbc.getString(dbc.getColumnIndex("LOGIN"));
			pasHash = dbc.getString(dbc.getColumnIndex("PASS_HASH"));
			name = dbc.getString(dbc.getColumnIndex("NAME"));
			surname = dbc.getString(dbc.getColumnIndex("SURNAME"));
			adress = dbc.getString(dbc.getColumnIndex("ADRESS"));
			phone = dbc.getString(dbc.getColumnIndex("PHONE"));
			email = dbc.getString(dbc.getColumnIndex("EMAIL"));
			
			setDate = MainLibrary.StringToDateSql(dbc.getString(dbc.getColumnIndex("SET_DATE")));
			cuatm = dbc.getString(dbc.getColumnIndex("CUATM"));
			cuatmName = dbc.getString(dbc.getColumnIndex("CUATM_NAME"));
		}
		dbc.close();

		//Load user settings data
		dbc = Database.db.query("USER_SETTINGS", null, null, null, null, null, null);
		if (dbc.moveToFirst()) do
		{
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("currentCodeUnit")) 	currentCodeUnit = dbc.getString(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("currentCodeProd")) 	currentCodeProd = dbc.getString(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("currentCodeAstr")) 	currentCodeAstr = dbc.getString(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("filterRegUnit")) 		filterRegUnit = dbc.getInt(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("filterDataPrice")) 	filterDataPrice = dbc.getInt(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("sortDataPrice")) 		sortDataPrice = dbc.getInt(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("sortRegUnit")) 		sortRegUnit = dbc.getInt(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("sortCountry")) 		sortCountry = dbc.getInt(dbc.getColumnIndex("SET_VALUE"));
			if (dbc.getString(dbc.getColumnIndex("SET_NAME")).equals("sortProduct")) 		sortProduct = dbc.getInt(dbc.getColumnIndex("SET_VALUE"));
		} while(dbc.moveToNext());
		dbc.close();
	}

	//----------------------------------------------------------------------------------------------
	public static String getNameFull()
	{
		return name + (surname.equals("") ? "" : " "+surname);
	}

	//----------------------------------------------------------------------------------------------
	public static int getDateSetYear()
	{
		return setDate.getYear()+1900;
	}

	//----------------------------------------------------------------------------------------------
	public static int getDateSetMonth()
	{
		return setDate.getMonth()+1;
	}

	//----------------------------------------------------------------------------------------------
	public static int getDateSetDecade()
	{
		if (setDate.getDate() == DECADE1) return DECADE1;
		else if (setDate.getDate() == DECADE2) return DECADE2;
		else return DECADE3;
	}

	//----------------------------------------------------------------------------------------------
	public static void setDateSet(Date val)
	{
		//Update data
		ContentValues cv = new ContentValues();
		cv.put("SET_DATE", MainLibrary.DateToStringSql(val));
		Database.db.update("SYS_USER_VW", cv, "ID_USER = ?", new String[] { String.valueOf(idUser) });
		
		//Set variable
		setDate = val;
	}

	//----------------------------------------------------------------------------------------------
	private static void setSettings(String setName, String val)
	{
		//Update data
		ContentValues cv = new ContentValues();
	    cv.clear();
		cv.put("SET_VALUE", val);
	    int upd = Database.db.update("USER_SETTINGS", cv, "SET_NAME = ?", new String[] { setName });
	    
	    //Insert data
	    if (upd == 0)
	    {
	    	cv.clear();
	    	cv.put("SET_NAME", setName);
	    	cv.put("SET_VALUE", val);
	    	Database.db.insert("USER_SETTINGS", null, cv);
	    }
	    cv.clear();
	}

	//----------------------------------------------------------------------------------------------
	public static void setCurrentCodeUnit(String val)
	{
		setSettings("currentCodeUnit", val);
		currentCodeUnit = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setCurrentCodeProd(String val)
	{
		setSettings("currentCodeProd", val);
		currentCodeProd = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setCurrentCodeAstr(String val)
	{
		setSettings("currentCodeAstr", val);
		currentCodeAstr = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setRegUnitFilter(int val)
	{
		setSettings("filterRegUnit", String.valueOf(val));
		filterRegUnit = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setDataPriceFilter(int val)
	{
		setSettings("filterDataPrice", String.valueOf(val));
		filterDataPrice = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setDataPriceSort(int val)
	{
		setSettings("sortDataPrice", String.valueOf(val));
		sortDataPrice = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setRegUnitSort(int val)
	{
		setSettings("sortRegUnit", String.valueOf(val));
		sortRegUnit = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setCountrySort(int val)
	{
		setSettings("sortCountry", String.valueOf(val));
		sortCountry = val;
	}

	//----------------------------------------------------------------------------------------------
	public static void setProductSort(int val)
	{
		setSettings("sortProduct", String.valueOf(val));
		sortProduct = val;
	}
	//----------------------------------------------------------------------------------------------
}
