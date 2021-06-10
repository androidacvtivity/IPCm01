package md.statistica.ipc.ipcm_v01;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DialogExport extends Activity
{
	Activity	this_link;
	Button		button_close;
	Button		button_export;
	TextView	text_export_period;
	TextView	text_export_message;
	ProgressBar	progress_dialog_export;
	
	ExportTask	importTask;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_export);
		
		//Initialize views .........................................................................
		this_link				= this;
		button_close			= (Button)		findViewById(R.id.button_dialog_export_close);
		button_export			= (Button)		findViewById(R.id.button_dialog_export_export);
		text_export_period		= (TextView)	findViewById(R.id.text_dialog_export_period);
		text_export_message		= (TextView)	findViewById(R.id.text_dialog_export_message);
		progress_dialog_export	= (ProgressBar)	findViewById(R.id.progress_dialog_export);
		
		text_export_period.setText(getResources().getString(R.string.dialog_export_period) + ": " + MainLibrary.DateToString(DBUserInfo.setDate));
		text_export_message.setText(R.string.dialog_export_message_default);
		text_export_message.setTextColor(Color.BLACK);
		progress_dialog_export.setVisibility(View.INVISIBLE);

		//Initialize listeners .....................................................................
		button_close.setOnClickListener(CloseClick);
		button_export.setOnClickListener(ExportClick);
	}

	//----------------------------------------------------------------------------------------------
	//Close OnClick Listener
	OnClickListener CloseClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			finish();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Export OnClick Listener
	OnClickListener ExportClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			//Old file format - temporary solution (!!!)
			if (MainLibrary.CurrentDate().after(MainLibrary.StringToDate("01.01.2050")))
			{
				new AlertDialog.Builder(this_link)
		        .setTitle(R.string.dialog_attention)
		        .setMessage(R.string.dialog_export_message_temporary_old_export_show)
		        .setPositiveButton(R.string.dialog_ok, null).create().show();
				return;
			}
			
			importTask = new ExportTask();
			importTask.execute();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Import task (input - Void, intermediate - String, output - Void)
	class ExportTask extends AsyncTask<Void, String, Void> 
	{
		String exportErrors;
		
		//..........................................................................................
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			//Internal variables
			exportErrors = "";
			
			//Set message
			text_export_message.setText(getResources().getString(R.string.dialog_export_message_exports));
			text_export_message.setTextColor(Color.BLACK);
			progress_dialog_export.setVisibility(View.VISIBLE);
			
			//Disable views
			button_close.setEnabled(false);
			button_export.setEnabled(false);
		}

		//..........................................................................................
		@Override
		protected Void doInBackground(Void... params) //input - Void
		{
			try 
			{
				if (exportErrors.equals("")) exportErrors = exportFile_OldVersion();
				if (exportErrors.equals("")) exportErrors = exportFile();
			} 
			catch (Exception e) 
			{
				exportErrors = e.getMessage();
			}
			return null;
		}

		//..........................................................................................
	    @Override
	    protected void onProgressUpdate(String... values) 
	    {
	      super.onProgressUpdate(values);
	      
	      //Set progress information
	      if (!values[0].equals("")) text_export_message.setText(values[0]);
	    }

		//..........................................................................................
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			
			//Enable views
			button_close.setEnabled(true);
			button_export.setEnabled(true);
			progress_dialog_export.setVisibility(View.INVISIBLE);
			
			//Set message
			if (exportErrors.equals(""))
			{
				text_export_message.setText(R.string.dialog_export_message_exported);
				text_export_message.setTextColor(Color.BLUE);
			}
			else
			{
				text_export_message.setText(R.string.dialog_export_message_exported_err);
				text_export_message.setTextColor(Color.RED);

				//Alert error explanation
				new AlertDialog.Builder(this_link)
		        .setTitle(R.string.dialog_error)
		        .setMessage(exportErrors)
		        .setPositiveButton(R.string.dialog_ok, null).create().show();
			}
			
			//Rescan the directory to make contents be available on PC through USB-cable
			MediaScannerConnection.scanFile(this_link, new String []{ MainLibrary.getExportDirectory().getAbsolutePath().toString() }, null, null);
		}

		//..........................................................................................
		//Get XML-node without text value
		private String xmlNode(int nodeLevel, String nodeName)
		{
			String resultStr = MainLibrary.strRepeat("  ", nodeLevel) + "<" + nodeName + ">\r\n";
			return resultStr;
		}

		//..........................................................................................
		//Get XML-node with text value
		private String xmlNode(int nodeLevel, String nodeName, String nodeValue)
		{
			String resultStr = "";
			if ( nodeValue.equals("")) resultStr = MainLibrary.strRepeat("  ", nodeLevel) + "<" + nodeName + " />\r\n";
			if (!nodeValue.equals("")) resultStr = MainLibrary.strRepeat("  ", nodeLevel) + "<" + nodeName +   ">" + nodeValue.replace("&", " ").replace("<", " ") + "</" + nodeName + ">\r\n";
			return resultStr;
		}

		//..........................................................................................
		//Old file format - temporary solution (!!!)
		private String getOldCuatm(String cuatm)
		{
			return cuatm.substring(0, 4);
		}

		//..........................................................................................
		//Old file format - temporary solution (!!!)
		private String getOldUser(long idUser)
		{
			if (getOldCuatm(DBUserInfo.cuatm).equals("0100") && idUser == 145) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("0100") && idUser == 128) return "02";
			if (getOldCuatm(DBUserInfo.cuatm).equals("0100") && idUser == 129) return "03";
			if (getOldCuatm(DBUserInfo.cuatm).equals("0100") && idUser == 130) return "04";
			if (getOldCuatm(DBUserInfo.cuatm).equals("0100") && idUser == 127) return "05";
			if (getOldCuatm(DBUserInfo.cuatm).equals("0300") && idUser == 131) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("0300") && idUser == 132) return "02";
			if (getOldCuatm(DBUserInfo.cuatm).equals("0300") && idUser == 144) return "03";
			if (getOldCuatm(DBUserInfo.cuatm).equals("1701") && idUser == 137) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("1701") && idUser == 138) return "02";
			if (getOldCuatm(DBUserInfo.cuatm).equals("4101") && idUser == 139) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("4101") && idUser == 140) return "02";
			if (getOldCuatm(DBUserInfo.cuatm).equals("6401") && idUser == 133) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("6401") && idUser == 134) return "02";
			if (getOldCuatm(DBUserInfo.cuatm).equals("7801") && idUser == 143) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("7801") && idUser == 126) return "02";
			if (getOldCuatm(DBUserInfo.cuatm).equals("9201") && idUser == 141) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("9201") && idUser == 142) return "02";
			if (getOldCuatm(DBUserInfo.cuatm).equals("9601") && idUser == 135) return "01";
			if (getOldCuatm(DBUserInfo.cuatm).equals("9601") && idUser == 136) return "02";

			return "01";
		}

		//..........................................................................................
		//Old file format - temporary solution (!!!)
		private String exportFile_OldVersion()
		{
			try
			{
				//Make file name
				String fileName = getOldCuatm(DBUserInfo.cuatm) + "PRET_" + getOldUser(DBUserInfo.idUser) + "_" + String.valueOf(DBUserInfo.getDateSetYear()) + "_" + String.valueOf(DBUserInfo.getDateSetMonth()) + "_";
				if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE1) fileName += "1";
				if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE2) fileName += "2";
				if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE3) fileName += "3";
				fileName += ".xml";
				
				//Initialize month data
				publishProgress(getResources().getString(R.string.dialog_export_message_exports)+".\n"+fileName+" [...]");
				DBDataPrices dataPrices = new DBDataPrices();
				dataPrices.loadData(DBDataPrices.FILTER_ALL, DBDataPrices.SORT_BY_CODE, DBUserInfo.setDate);
				
				//Check data for errors
				//if (dataPrices.existErrors()) return getResources().getString(R.string.dialog_export_message_exported_err_exists_errors);
				//if (dataPrices.existRemained()) return getResources().getString(R.string.dialog_export_message_exported_err_exists_remained);
				
				//Initialize XML-file
				FileOutputStream xmlFile = new FileOutputStream(MainLibrary.getExportFile(fileName));
				BufferedOutputStream xmlBuffer = new BufferedOutputStream(xmlFile);
				
				//Start file
				xmlBuffer.write("<?xml version=\"1.0\" standalone=\"yes\"?>\r\n".getBytes());
				xmlBuffer.write(xmlNode(0, "DocumentElement").getBytes());
				
				//Fill in file
				String xmlRow;
			    int recNumber = 0;
				if ((recNumber % 100) == 0) publishProgress(getResources().getString(R.string.dialog_export_message_exports)+".\n"+fileName+" ["+recNumber+"]");
				for (DBDataPrices.ItemClass item: dataPrices.list)
				{
					if (!item.codeAsrt.equals("0")) continue;
					xmlRow = xmlNode(1, "PRETURI");
					
					xmlRow += xmlNode(2, "YEAR", String.valueOf(DBUserInfo.getDateSetYear()));
					xmlRow += xmlNode(2, "MONTH", String.valueOf(DBUserInfo.getDateSetMonth()));
					if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE1) xmlRow += xmlNode(2, "DECADE", "1");
					if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE2) xmlRow += xmlNode(2, "DECADE", "2");
					if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE3) xmlRow += xmlNode(2, "DECADE", "3");
					xmlRow += xmlNode(2, "CODE_REGION", getOldCuatm(DBUserInfo.cuatm));
					xmlRow += xmlNode(2, "CODE_STORE", item.codeUnit);
					xmlRow += xmlNode(2, "CODE_PRODUCT", item.codeProd);
					xmlRow += xmlNode(2, "CODE_OPERATOR", getOldUser(DBUserInfo.idUser));
					if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE1) xmlRow += xmlNode(2, "PRICE", String.valueOf(item.price_C1));
					if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE2) xmlRow += xmlNode(2, "PRICE", String.valueOf(item.price_C2));
					if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE3) xmlRow += xmlNode(2, "PRICE", String.valueOf(item.price_C3));
					xmlRow += xmlNode(2, "CONSISTS_FROM", String.valueOf(item.consistsFrom));
					xmlRow += xmlNode(2, "CODE_DESCRIPTION", (item.idDataStatus == 0 ? "" : "0" + String.valueOf(item.idDataStatus)));
					xmlRow += xmlNode(2, "COMENT", item.textualComment);
					xmlRow += xmlNode(2, "DATE_MODIFICATE", MainLibrary.DateToString(item.modDate, "yyyy-MM-dd")+"T"+MainLibrary.DateToString(item.modDate, "HH:mm:ss")+"+01:00");
					
					xmlRow += xmlNode(1, "/PRETURI");
					xmlBuffer.write(xmlRow.getBytes());
					
					recNumber++;
					if ((recNumber % 100) == 0) publishProgress(getResources().getString(R.string.dialog_export_message_exports)+".\n"+fileName+" ["+recNumber+"]");
				}
				
				//Close file
				xmlBuffer.write(xmlNode(0, "/DocumentElement").getBytes());
				xmlBuffer.close();
				
				return "";
			}
			catch (Exception e)
			{
				return e.getMessage();
			}
		}

		//..........................................................................................
		//Export data
		private String exportFile()
		{
			try
			{
				//Make file name
				String fileName = "DAT - " + MainLibrary.DateToString(DBUserInfo.setDate, "yyyy.MM.dd") + " - " + DBUserInfo.cuatm + " - " + DBUserInfo.getNameFull() + ".xml";
				
				//Initialize month data
				publishProgress(getResources().getString(R.string.dialog_export_message_exports)+".\n"+fileName+" [...]");
				DBDataPrices dataPrices = new DBDataPrices();
				dataPrices.loadData(DBDataPrices.FILTER_ALL, DBDataPrices.SORT_BY_CODE, DBUserInfo.setDate);

				//Check data for errors
				//if (dataPrices.existErrors()) return getResources().getString(R.string.dialog_export_message_exported_err_exists_errors);
				//if (dataPrices.existRemained()) return getResources().getString(R.string.dialog_export_message_exported_err_exists_remained);

				//Initialize XML-file
				FileOutputStream xmlFile = new FileOutputStream(MainLibrary.getExportFile(fileName));
				BufferedOutputStream xmlBuffer = new BufferedOutputStream(xmlFile);
				
				//Start file
				xmlBuffer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n\r\n".getBytes());
				xmlBuffer.write(xmlNode(0, "DATA_FILE").getBytes());
				
				//Meta-information about mobile application
				xmlBuffer.write(xmlNode(1, "DATA_META").getBytes());
				xmlBuffer.write(xmlNode(2, "AplicationVersion", getPackageManager().getPackageInfo(getPackageName(), 0).versionName).getBytes());
				xmlBuffer.write(xmlNode(2, "DatabaseVersion", String.valueOf(DatabaseHealper.getDBVersion())).getBytes());
				xmlBuffer.write(xmlNode(2, "ExportCuatm", DBUserInfo.cuatm).getBytes());
				xmlBuffer.write(xmlNode(2, "ExportUser", DBUserInfo.login).getBytes());
				xmlBuffer.write(xmlNode(2, "ExportDate", MainLibrary.DateToString(DBUserInfo.setDate)).getBytes());
				xmlBuffer.write(xmlNode(2, "FileDate", MainLibrary.DateTimeToString(MainLibrary.CurrentDate())).getBytes());
				xmlBuffer.write(xmlNode(1, "/DATA_META").getBytes());
				xmlBuffer.write("\r\n".getBytes());
				
				//Fill in file
				String xmlRow;
			    int recNumber = 0;
				if ((recNumber % 100) == 0) publishProgress(getResources().getString(R.string.dialog_export_message_exports)+".\n"+fileName+" ["+recNumber+"]");
				for (DBDataPrices.ItemClass item: dataPrices.list)
				{
					xmlRow = xmlNode(1, "DATA_IMPORT");
					
					xmlRow += xmlNode(2, "SET_DATE", MainLibrary.DateToString(DBUserInfo.setDate));
					xmlRow += xmlNode(2, "CUATM", DBUserInfo.cuatm);
					xmlRow += xmlNode(2, "CODE_UNIT", item.codeUnit);
					xmlRow += xmlNode(2, "CODE_PROD", item.codeProd);
					xmlRow += xmlNode(2, "CODE_ASSORT", item.codeAsrt);
					xmlRow += xmlNode(2, "NAME", item.name);
					xmlRow += xmlNode(2, "QUANTITY", String.valueOf(item.quantity));
					xmlRow += xmlNode(2, "ASSORT_COUNTRY", item.asrtCountry);
					xmlRow += xmlNode(2, "ASSORT_COMPANY", item.asrtCompany);
					xmlRow += xmlNode(2, "ASSORT_BRAND", item.asrtBrand);
					xmlRow += xmlNode(2, "PRICE_C1", String.valueOf(item.price_C1));
					xmlRow += xmlNode(2, "PRICE_C2", String.valueOf(item.price_C2));
					xmlRow += xmlNode(2, "PRICE_C3", String.valueOf(item.price_C3));
					xmlRow += xmlNode(2, "ID_DATA_STATUS", String.valueOf(item.idDataStatus));
					xmlRow += xmlNode(2, "REPEATED_COUNT", String.valueOf(item.repeatedCount));
					xmlRow += xmlNode(2, "TEXTUAL_COMMENT", item.textualComment);
					xmlRow += xmlNode(2, "MOD_DATE", MainLibrary.DateTimeToString(item.modDate));
					xmlRow += xmlNode(2, "MOD_USER", String.valueOf(DBUserInfo.idUser));
					
					xmlRow += xmlNode(1, "/DATA_IMPORT");
					xmlBuffer.write(xmlRow.getBytes());
					
					recNumber++;
					if ((recNumber % 100) == 0) publishProgress(getResources().getString(R.string.dialog_export_message_exports)+".\n"+fileName+" ["+recNumber+"]");
				}
				
				//Close file
				xmlBuffer.write(xmlNode(0, "/DATA_FILE").getBytes());
				xmlBuffer.close();
				return "";
			}
			catch (Exception e)
			{
				return e.getMessage();
			}
		}
		//..........................................................................................
	}
	//----------------------------------------------------------------------------------------------
}
