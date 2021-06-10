package md.statistica.ipc.ipcm_v01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHealper extends SQLiteOpenHelper 
{
	//----------------------------------------------------------------------------------------------
	//Database information
	private static final String DATABASE_NAME = "IPCm.db";
	
	//private static final int DATABASE_VERSION = 1;
	private static final int DATABASE_VERSION = 2; //modified: 2013.11.06
	public  static int getDBVersion() {return DATABASE_VERSION;}
	
	//----------------------------------------------------------------------------------------------
	public DatabaseHealper(Context context) 
	{
		//Initialize database
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		//Local data table with all user settings
		db.execSQL
		(
				"CREATE TABLE 				USER_SETTINGS "+
				"("+
				"	_id 					INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"	SET_NAME 				VARCHAR(200), "+
				"	SET_VALUE 				VARCHAR(200) "+
				");"
		);
		
		//Global data tables with data related to the central database
		db.execSQL
		(
				"CREATE TABLE 				SYS_USER_VW "+
				"("+
				"	_id 					INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"	ID_USER 				NUMBER, "+
				"	ID_ROLE 				NUMBER, "+
				"	ROLE_NAME 				VARCHAR(200), "+
				
				"	LOGIN 					VARCHAR(100), "+	//Global user identification
				"	PASS_HASH 				VARCHAR(200), "+	//Global user identification
				
				"	SET_DATE 				VARCHAR(10), "+		//Global user settings
				"	CUATM 					VARCHAR(7), "+		//Global user settings (read only)
				"	CUATM_NAME 				VARCHAR(200), "+	//Global user settings (read only)
				
				"	NAME 					VARCHAR(200), "+
				"	SURNAME 				VARCHAR(200), "+
				"	ADRESS 					VARCHAR(200), "+
				"	PHONE 					VARCHAR(50), "+
				"	EMAIL 					VARCHAR(200) "+
				");"
		);
		
		db.execSQL
		(
				"CREATE TABLE 				CLS_COUNTRY_VW "+
				"("+
				"	_id 					INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"	CODE_COUNTRY 			VARCHAR(3), "+
				"	NAME 					VARCHAR(200) "+
				");"
		);
		
		db.execSQL
		(
				"CREATE TABLE 				CLS_PRODUCT_VW "+
				"("+
				"	_id 					INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"	CODE_PROD 				VARCHAR(8), "+
				"	CODE_PROD_VERS 			VARCHAR(10), "+
				"	NAME 					VARCHAR(500), "+
				"	QUANTITY 				DOUBLE, "+
				"	ID_MESUREMENT_UNIT 		NUMBER, "+
				"	MESUREMENT_UNIT_SHORT 	VARCHAR(100), "+
				"	MESUREMENT_UNIT 		VARCHAR(200), "+
				"	ID_PROD_TYPE 			NUMBER, "+
				"	PROD_TYPE 				VARCHAR(200), "+
				"	COLLECT_ORDINARILY 		VARCHAR(1), "+
				"	COLLECT_PER_MONTH 		NUMBER, "+
				"	VARIATY 				DOUBLE, "+
				"	ID_PROD_STATUS 			NUMBER, "+
				"	PROD_STATUS 			VARCHAR(100) "+
				");"
		);
		
		db.execSQL
		(
				"CREATE TABLE 				REG_UNIT_VW "+
				"("+
				"	_id 					INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"	CUATM 					VARCHAR(7), "+
				"	CODE_UNIT 				VARCHAR(8), "+
				"	CODE_UNIT_VERS 			VARCHAR(10), "+
				"	CUATM_NAME 				VARCHAR(200), "+
				"	NAME 					VARCHAR(200), "+
				"	ADRESS 					VARCHAR(200), "+
				"	PHONE 					VARCHAR(50), "+
				"	EMAIL 					VARCHAR(200), "+
				"	WWW 					VARCHAR(200), "+
				"	SCHEDULE 				VARCHAR(500), "+
				"	CONACT_PERSON 			VARCHAR(200), "+
				"	ID_UNIT_TYPE 			NUMBER, "+
				"	UNIT_TYPE 				VARCHAR(200), "+
				"	ID_UNIT_STATUS 			NUMBER, "+
				"	UNIT_STATUS 			VARCHAR(200) "+
				");"
		);
		
		db.execSQL
		(
				"CREATE TABLE 				DATA_PRICE_VW "+
				"("+
				"	_id 					INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"	SET_DATE 				VARCHAR(10), "+
				"	CUATM 					VARCHAR(7), "+
				"	CODE_UNIT 				VARCHAR(8), "+
				"	CODE_PROD 				VARCHAR(8), "+
				"	CODE_ASSORT 			VARCHAR(8), "+
				"	NAME 					VARCHAR(500), "+
				"	QUANTITY 				DOUBLE, "+
				"	ASSORT_COUNTRY 			VARCHAR(3), "+
				"	ASSORT_COMPANY 			VARCHAR(255), "+
				"	ASSORT_BRAND 			VARCHAR(255), "+
				"	ID_DATA_STATUS 			NUMBER, "+
				"	PRICE_P1 				DOUBLE, "+
				"	PRICE_P2 				DOUBLE, "+
				"	PRICE_P3 				DOUBLE, "+
				"	PRICE_C1 				DOUBLE, "+
				"	PRICE_C2 				DOUBLE, "+
				"	PRICE_C3 				DOUBLE, "+
				"	REPEATED_COUNT 			NUMBER, "+
				"	TEXTUAL_COMMENT 		VARCHAR(500), "+
				"	MOD_DATE 				VARCHAR(19) "+
				");"
		);
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		//[1] ......................................................................................
		if(oldVersion == 0 && newVersion == 1) 
		{
			//Do noting - this is the first version of database
		}
		
		//[2] ......................................................................................
		if(oldVersion == 1 && newVersion == 2) //modified: 2013.11.06
		{
			//In this version of the database: 
			//Products collected once a month are collected in Decade-2
			
			//Copy Decade-1 into Decade-2
			db.execSQL
			(
				"UPDATE DATA_PRICE_VW SET PRICE_P2=PRICE_P1, PRICE_C2=PRICE_C1 "+
				"WHERE CODE_PROD IN(SELECT CODE_PROD FROM CLS_PRODUCT_VW WHERE COLLECT_PER_MONTH=1);"
			);
			
			//Clear Decade-1
			db.execSQL
			(
				"UPDATE DATA_PRICE_VW SET PRICE_P1=0, PRICE_C1=0 "+
				"WHERE CODE_PROD IN(SELECT CODE_PROD FROM CLS_PRODUCT_VW WHERE COLLECT_PER_MONTH=1);"
			);
		}
		//..........................................................................................
	}
	//----------------------------------------------------------------------------------------------

}
