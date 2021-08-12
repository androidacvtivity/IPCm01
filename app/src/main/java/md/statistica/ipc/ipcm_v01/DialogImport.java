package md.statistica.ipc.ipcm_v01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.R.layout.simple_list_item_multiple_choice;
import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;

public class DialogImport extends Activity 
{
	Activity	this_link;
	Button		button_close, button_import;
	//Button		button_import;
	ListView	list_import_files;
	TextView	text_import_message;
	ProgressBar	progress_dialog_import;
	
	ImportTask	importTask;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_import);
		
		//Initialize views .........................................................................
		this_link					= this;
		button_close				= (Button)		findViewById(R.id.button_dialog_import_close);
		button_import				= (Button)		findViewById(R.id.button_dialog_import_import);
		list_import_files			= (ListView)	findViewById(R.id.list_dialog_import_files);
		text_import_message			= (TextView)	findViewById(R.id.text_dialog_import_message);
		progress_dialog_import		= (ProgressBar)	findViewById(R.id.progress_dialog_import);
		
		button_import.setEnabled(false);
		text_import_message.setText(R.string.dialog_import_message_default);
		text_import_message.setTextColor(Color.BLACK);
		progress_dialog_import.setVisibility(View.INVISIBLE);

		//Initialize listeners .....................................................................
		button_close.setOnClickListener(CloseClick);
		button_import.setOnClickListener(ImportClick);
		list_import_files.setOnItemClickListener(FileClick);

		//Initialize file list .....................................................................
		File directoryImport = MainLibrary.getImportDirectory();
		String[] importFiles = directoryImport.list(new FilenameFilter() 
		{
		    @SuppressLint("DefaultLocale")
			public boolean accept(File dir, String name) 
		    {
		        return (name.toLowerCase().startsWith("upd") && name.toLowerCase().endsWith(".xml"));
		    }
		});
		 list_import_files.setChoiceMode(CHOICE_MODE_MULTIPLE);
		 list_import_files.setAdapter(new ArrayAdapter<String>(this, simple_list_item_multiple_choice, importFiles));
	}

	//----------------------------------------------------------------------------------------------
	//File OnClick Listener
	OnItemClickListener FileClick = new OnItemClickListener() 
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			int checkedNumber = 0;
			SparseBooleanArray checkedItems = list_import_files.getCheckedItemPositions();
			for (int i = 0; i < list_import_files.getCount(); i++) if (checkedItems.get(i)) checkedNumber++;
			
			if (checkedNumber == 0) 
			{
				button_import.setEnabled(false);
				text_import_message.setText(R.string.dialog_import_message_default);
				text_import_message.setTextColor(Color.BLACK);
			}
			else 
			{
				String tmpStr = getResources().getString(R.string.dialog_import_message_selected)+": "+checkedNumber;
				button_import.setEnabled(true);
				text_import_message.setText(tmpStr);
				text_import_message.setTextColor(Color.BLACK);
			}
		}
	};

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
	//Import OnClick Listener
	OnClickListener ImportClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			importTask = new ImportTask();
			importTask.execute();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Import task (input - Void, intermediate - String, output - Void)
	class ImportTask extends AsyncTask<Void, String, Void> 
	{
		String importErrors;
		ListView listFilesLink;
		
		//..........................................................................................
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			//Internal variables
			importErrors = "";
			listFilesLink = list_import_files;
			
			//Set message
			text_import_message.setText(getResources().getString(R.string.dialog_import_message_imports));
			text_import_message.setTextColor(Color.BLACK);
			progress_dialog_import.setVisibility(View.VISIBLE);
			
			//Disable views
			button_close.setEnabled(false);
			button_import.setEnabled(false);
			list_import_files.setEnabled(false);
		}

		//..........................................................................................
		@Override
		protected Void doInBackground(Void... params) //input - Void
		{
			try 
			{
				//Import each file
				for (int i = 0; i < listFilesLink.getCount(); i++) if (listFilesLink.getCheckedItemPositions().get(i))
				{
					//Get file name
					String fileName = (String)listFilesLink.getItemAtPosition(i);
					publishProgress(getResources().getString(R.string.dialog_import_message_imports)+".\n"+fileName, "");
					
					//Import file
					String result = importFile(fileName);
					if (!result.equals("")) { importErrors = result; break; }
					
					//Uncheck the file
					publishProgress(getResources().getString(R.string.dialog_import_message_imports)+".\n"+fileName, String.valueOf(i));
				}
			} 
			catch (Exception e) 
			{
				importErrors = e.getMessage();
			}
			return null;
		}

		//..........................................................................................
	    @Override
	    protected void onProgressUpdate(String... values) 
	    {
	      super.onProgressUpdate(values);
	      
	      //Set progress information
	      if (!values[0].equals("")) text_import_message.setText(values[0]);
	      if (!values[1].equals("")) list_import_files.setItemChecked(Integer.parseInt(values[1]), false);
	    }

		//..........................................................................................
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			
			//Enable views
			button_close.setEnabled(true);
			button_import.setEnabled(true);
			list_import_files.setEnabled(true);
			progress_dialog_import.setVisibility(View.INVISIBLE);
			
			//Set message
			if (importErrors.equals(""))
			{
				button_import.setEnabled(false);
				text_import_message.setText(R.string.dialog_import_message_imported);
				text_import_message.setTextColor(Color.BLUE);
			}
			else
			{
				text_import_message.setText(R.string.dialog_import_message_imported_err);
				text_import_message.setTextColor(Color.RED);

				//Alert error explanation
				new AlertDialog.Builder(this_link)
		        .setTitle(R.string.dialog_error)
		        .setMessage(importErrors)
		        .setPositiveButton(R.string.dialog_ok, null).create().show();
			}
		}

		//..........................................................................................
		private String importFile(String fileName)
		{
			try
			{
				//Initialize XML-file and XML-parser
				FileInputStream xmlFile = new FileInputStream(MainLibrary.getImportFile(fileName));
				XmlPullParser xmlParser = Xml.newPullParser();
			    xmlParser.setInput(xmlFile, null);

			    //Flags for XML-events
			    int recNumber = 0;
			    int tagNumber = 0;
			    String tagName = "";
			    
			    boolean isUpdateFile = false;
			    boolean isUserRecord = false;
			    
			    boolean isCountryClear = false;
			    boolean isCountryRecord = false;
			    
			    boolean isProductClear = false;
			    boolean isProductRecord = false;
			    
			    boolean isRegUnitClear = false;
			    boolean isRegUnitRecord = false;
			    
			    boolean isDataPriceClear = false;
			    boolean isDataPriceRecord = false;
			    
			    //Import objects
			    DBClsCounties.ItemClass importCountry = null;
			    DBClsProducts.ItemClass importProduct = null;
			    DBRegUnits.ItemClass importRegUnit = null;
			    DBDataPrices.ItemClass importDataPrice = null;
				
			    //Parse XML-document
				int xmlEventType = xmlParser.getEventType();
				while (xmlEventType != XmlPullParser.END_DOCUMENT)
				{
					//Process current XML-tag
					switch (xmlEventType)
					{
						//..........................................................................
						case XmlPullParser.START_DOCUMENT: break;
						//..........................................................................
						case XmlPullParser.START_TAG: 
							tagNumber++; 
							tagName = xmlParser.getName();
							if (tagName.equals("UPDATE_FILE"))    { isUpdateFile      = true; } 
							if (tagName.equals("SYS_USER_VW"))    { isUserRecord      = true; } 
							if (tagName.equals("CLS_COUNTRY_VW")) { isCountryRecord   = true; importCountry   = new DBClsCounties.ItemClass(); }
							if (tagName.equals("CLS_PRODUCT_VW")) { isProductRecord   = true; importProduct   = new DBClsProducts.ItemClass(); }
							if (tagName.equals("REG_UNIT_VW"))    { isRegUnitRecord   = true; importRegUnit   = new DBRegUnits.ItemClass();    }
							if (tagName.equals("DATA_PRICE_VW"))  { isDataPriceRecord = true; importDataPrice = new DBDataPrices.ItemClass();  }
							break;

						//..........................................................................
						case XmlPullParser.TEXT:
							if (isUserRecord)		DBUserInfo.importValue(tagName, xmlParser.getText());
							if (isCountryRecord)	importCountry.importValue(tagName, xmlParser.getText());
							if (isProductRecord)	importProduct.importValue(tagName, xmlParser.getText());
							if (isRegUnitRecord)	importRegUnit.importValue(tagName, xmlParser.getText());
							if (isDataPriceRecord)	importDataPrice.importValue(tagName, xmlParser.getText());
							break;

						//..........................................................................
						case XmlPullParser.END_TAG: 
							tagName = "";
							if (xmlParser.getName().equals("SYS_USER_VW"))    { recNumber++; isUserRecord      = false; DBUserInfo.importToDB(); }
							if (xmlParser.getName().equals("CLS_COUNTRY_VW")) { recNumber++; isCountryRecord   = false; if (!isCountryClear)   { DBClsCounties.dropData();                       isCountryClear   = true; } importCountry.importToDB();   }
							if (xmlParser.getName().equals("CLS_PRODUCT_VW")) { recNumber++; isProductRecord   = false; if (!isProductClear)   { DBClsProducts.dropData();                       isProductClear   = true; } importProduct.importToDB();   }
							if (xmlParser.getName().equals("REG_UNIT_VW"))    { recNumber++; isRegUnitRecord   = false; if (!isRegUnitClear)   { DBRegUnits.dropData();                          isRegUnitClear   = true; } importRegUnit.importToDB();   }
							if (xmlParser.getName().equals("DATA_PRICE_VW"))  { recNumber++; isDataPriceRecord = false; if (!isDataPriceClear) { DBDataPrices.dropData(importDataPrice.setDate); isDataPriceClear = true; } importDataPrice.importToDB(); }
							
							if ((recNumber % 100) == 0) publishProgress(getResources().getString(R.string.dialog_import_message_imports)+".\n"+fileName+" ["+recNumber+"]", "");
							break;

						//..........................................................................
						case XmlPullParser.END_DOCUMENT: break;
						//..........................................................................
					}
					
					//Check if file has "update" tag to insure the right file import
					if (tagNumber > 0 && !isUpdateFile) 
					{
						throw new Exception(getResources().getString(R.string.dialog_import_message_imported_wrong_file));
					}
					
					//Move to the next XML-tag
					xmlEventType = xmlParser.next();
				}
				
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
