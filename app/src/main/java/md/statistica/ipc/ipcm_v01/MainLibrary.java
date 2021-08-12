package md.statistica.ipc.ipcm_v01;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Environment;

//My library class (with some custom functions)
public class MainLibrary 
{
	//----------------------------------------------------------------------------------------------
	//Constant global variables
	public final static Locale dateLocale = Locale.ENGLISH;
	public final static String dateFormat = "dd.MM.yyyy";
	public final static String dateFormatSQL = "yyyy-MM-dd";
	public final static String dateTimeFormat = "dd.MM.yyyy HH:mm:ss";
	public final static String dateTimeFormatSQL = "yyyy-MM-dd HH:mm:ss";
	
	public final static String directoryImport = "/!IPC/Primirea datelor";		//???
	public final static String directoryExport = "/!IPC/Expedierea datelor";	//???
	
	//=================================== DIRECTORIES ROUTINES =====================================
	//----------------------------------------------------------------------------------------------
	//Creates if needed and returns the directory for import
	public static File getImportDirectory()
	{
		File getDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + directoryImport);
		if(!getDir.exists()) getDir.mkdirs();
		getDir.setReadable(true, false);
		return getDir;
	}

	//----------------------------------------------------------------------------------------------
	//Creates if needed and returns the file for import
	public static File getImportFile(String fileName)
	{
		File getFile = new File(getImportDirectory().getAbsolutePath(), fileName);
		getFile.setReadable(true, false);
		return getFile;
	}
	
	//----------------------------------------------------------------------------------------------
	//Creates if needed and returns the directory for export
	public static File getExportDirectory()
	{
		File getDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + directoryExport);
		if(!getDir.exists()) getDir.mkdirs(); getDir.setReadable(true);
		getDir.setReadable(true, false);
		return getDir;
	}
	
	//----------------------------------------------------------------------------------------------
	//Creates if needed and returns the file for export
	public static File getExportFile(String fileName)
	{
		File getFile = new File(getExportDirectory().getAbsolutePath(), fileName);
		getFile.setReadable(true, false);
		return getFile;
	}
	
	//===================================== STRING ROUTINES ========================================
	//----------------------------------------------------------------------------------------------
	//Creates if needed and returns the directory for import
	public static String strRepeat(String repeateString, int repeatTimes)
	{
		String resultString = "";
		for (int i = 0; i < repeatTimes; i++)
		{
			resultString += repeateString;
		}
		return resultString;
	}

	//=================================== MATHIMATICAL ROUTINES ====================================
	//----------------------------------------------------------------------------------------------
	//Round to integer value
		public static Double round(Double number)
	{
		return Double.valueOf(Math.round(number));
	}
	
	//----------------------------------------------------------------------------------------------
	//Round to value by specific digits
	public static Double round(Double number, int roundTo)
	{
		return Math.round(number*Math.pow(10.0, roundTo))/Math.pow(10.0, roundTo);
	}

	//================================= NUMERIC TYPES CONVERSIONS ==================================
	//----------------------------------------------------------------------------------------------
	//Formats double to string
	@SuppressLint("DefaultLocale")
	public static String DoubleToString(Double number)
	{
		if(number == number.intValue()) return String.format("%d", number.intValue());
		else return String.format("%s",number);
	}

	//=================================== DATA TYPES CONVERSIONS ===================================
	//----------------------------------------------------------------------------------------------
	//Get current date
	public static Date CurrentDate()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	//----------------------------------------------------------------------------------------------
	//Add one month to the date
	public static Date DateAddMonth(Date pDate, int pMonths)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pDate);
		calendar.add(Calendar.MONTH, pMonths);
		return calendar.getTime();
	}
	
	//----------------------------------------------------------------------------------------------
	//Conversion of the date to string (with standard format)
	public static String DateToString(Date pDate)
	{
		return DateToString(pDate, dateFormat);
	}

	//----------------------------------------------------------------------------------------------
	//Conversion of the date to SQL string format (for the filtering by date)
	public static String DateToStringCustom(Date pDate)
	{
		Date pDateCustom = (Date)pDate.clone();
		pDateCustom.setDate(DBUserInfo.DECADE1);
		return DateToString(pDateCustom, dateFormat);
	}

	//----------------------------------------------------------------------------------------------
	//Conversion of the date to SQL string format
	public static String DateToStringSql(Date pDate)
	{
		return DateToString(pDate, dateFormatSQL);
	}

	//----------------------------------------------------------------------------------------------
	//Conversion of the date to SQL string format (for the filtering by date)
	public static String DateToStringSqlCustom(Date pDate)
	{
		Date pDateCustom = (Date)pDate.clone();
		pDateCustom.setDate(DBUserInfo.DECADE1);
		return DateToString(pDateCustom, dateFormatSQL);
	}
	
	//----------------------------------------------------------------------------------------------
	//Conversion of the date and time to string (with standard format)
	public static String DateTimeToString(Date pDate)
	{
		return DateToString(pDate, dateTimeFormat);
	}

	//----------------------------------------------------------------------------------------------
	//Conversion of the date and time to SQL string format
	public static String DateTimeToStringSql(Date pDate)
	{
		return DateToString(pDate, dateTimeFormatSQL);
	}

	//----------------------------------------------------------------------------------------------
	//Conversion of the date to string (with needed format)
	public static String DateToString(Date pDate, String pFormat)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(pFormat, dateLocale);
		return formatter.format(pDate);
	}
	
	//----------------------------------------------------------------------------------------------
	//Conversion of the string (with standard format) to date
	public static Date StringToDate(String pString)
	{
		return StringToDate(pString, dateFormat);
	}

	//----------------------------------------------------------------------------------------------
	//Conversion of the string with SQL format to date
	public static Date StringToDateSql(String pString)
	{
		return StringToDate(pString, dateFormatSQL);
	}
	
	//----------------------------------------------------------------------------------------------
	//Conversion of the string (with standard format) to date
	public static Date StringToDateTime(String pString)
	{
		return StringToDate(pString, dateTimeFormat);
	}

	//----------------------------------------------------------------------------------------------
	//Conversion of the string with SQL format to date
	public static Date StringToDateTimeSql(String pString)
	{
		return StringToDate(pString, dateTimeFormatSQL);
	}
	
	//----------------------------------------------------------------------------------------------
	//Conversion of the string (with needed format) to date
	public static Date StringToDate(String pString, String pFormat)
	{
		Date returnDate;
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat(pFormat, dateLocale);
			returnDate = (Date)formatter.parse(pString);
		}
		catch(Exception e)
		{
			returnDate = new Date(0);
		}
		return returnDate;
	}

	//======================================= CODE EXEMPLES ========================================
	//----------------------------------------------------------------------------------------------
	/* Example of the simple message *
	public static void TestToast()
	{
		Toast.makeText(getBaseContext(), "Toast", Toast.LENGTH_LONG).show();
	} /* */
	
	//----------------------------------------------------------------------------------------------
	/* Example of the alert dialog *
	public static void TestAlertDialog(Context context)
	{
		new AlertDialog.Builder(context)
		.setCancelable(false)
        .setTitle("Title")
        .setMessage("Confirm some test operation (no operation is coded)?")
        .setNegativeButton(R.string.dialog_no, new OnClickListener() 
        {
        	public void onClick(DialogInterface arg0, int arg1) 
        	{
        		//Code to execute on "No" button pressed
        	} 
        })
        .setPositiveButton(R.string.dialog_yes, new OnClickListener() 
        {
        	public void onClick(DialogInterface arg0, int arg1) 
        	{
        		//Code to execute on "Yes" button pressed
        	} 
        }).create().show();
	} /* */
	//----------------------------------------------------------------------------------------------
}
