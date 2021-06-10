package md.statistica.ipc.ipcm_v01;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DialogClearData extends Activity
{
	Activity		this_link;
	Button			button_close;
	Button			button_clear;
	ListView		list_dataset;
	TextView		text_clear_message;
	ProgressBar		progress_dialog_clear;
	
	ClearDataTask	clearDataTask;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_clear_data);
		
		//Initialize views .........................................................................
		this_link				= this;
		button_close			= (Button)		findViewById(R.id.button_dialog_clear_data_close);
		button_clear			= (Button)		findViewById(R.id.button_dialog_clear_data);
		list_dataset			= (ListView)	findViewById(R.id.list_dialog_clear_data_list);
		text_clear_message		= (TextView)	findViewById(R.id.text_dialog_clear_data_message);
		progress_dialog_clear	= (ProgressBar)	findViewById(R.id.progress_dialog_clear_data);
		
		button_clear.setEnabled(false);
		text_clear_message.setText(R.string.dialog_clear_data_message_default);
		text_clear_message.setTextColor(Color.BLACK);
		progress_dialog_clear.setVisibility(View.INVISIBLE);

		//Initialize listeners .....................................................................
		button_close.setOnClickListener(CloseClick);
		button_clear.setOnClickListener(ClearClick);
		list_dataset.setOnItemClickListener(DatasetClick);

		//Initialize list ..........................................................................
		initializeList();
	}
	
	private void initializeList()
	{
		DBDataPrices dataPrices = new DBDataPrices();
		List<String> dataList = dataPrices.getDatasets();
		list_dataset.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_dataset.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, dataList));
	}

	//----------------------------------------------------------------------------------------------
	//Data OnClick Listener
	OnItemClickListener DatasetClick = new OnItemClickListener() 
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			int checkedNumber = 0;
			SparseBooleanArray checkedItems = list_dataset.getCheckedItemPositions();
			for (int i = 0; i < list_dataset.getCount(); i++) if (checkedItems.get(i)) checkedNumber++;
			
			if (checkedNumber == 0) 
			{
				button_clear.setEnabled(false);
				text_clear_message.setText(R.string.dialog_clear_data_message_default);
				text_clear_message.setTextColor(Color.BLACK);
			}
			else 
			{
				String tmpStr = getResources().getString(R.string.dialog_clear_data_message_selected)+": "+checkedNumber;
				button_clear.setEnabled(true);
				text_clear_message.setText(tmpStr);
				text_clear_message.setTextColor(Color.BLACK);
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
	//Clear OnClick Listener
	OnClickListener ClearClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			new AlertDialog.Builder(this_link)
			.setCancelable(false)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.dialog_clear_data_confirm)
	        .setNegativeButton(R.string.dialog_no, null)
	        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() 
	        {
	        	public void onClick(DialogInterface arg0, int arg1) 
	        	{
	        		clearDataTask = new ClearDataTask();
	    			clearDataTask.execute();
	    			
	    			Intent intent = new Intent();
	    	        setResult(RESULT_OK, intent);
	        	} 
	        }).create().show();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Clear data task (input - Void, intermediate - String, output - Void)
	class ClearDataTask extends AsyncTask<Void, String, Void> 
	{
		String clearErrors;
		ListView listDataLink;
		
		//..........................................................................................
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			//Internal variables
			clearErrors = "";
			listDataLink = list_dataset;
			
			//Set message
			text_clear_message.setText(getResources().getString(R.string.dialog_clear_data_message_clears));
			text_clear_message.setTextColor(Color.BLACK);
			progress_dialog_clear.setVisibility(View.VISIBLE);
			
			//Disable views
			button_close.setEnabled(false);
			button_clear.setEnabled(false);
			list_dataset.setEnabled(false);
		}

		//..........................................................................................
		@Override
		protected Void doInBackground(Void... params) //input - Void
		{
			try 
			{
				//Clear each dataset
				for (int i = 0; i < listDataLink.getCount(); i++) if (listDataLink.getCheckedItemPositions().get(i))
				{
					//Get file name
					String datasetName = (String)listDataLink.getItemAtPosition(i);
					publishProgress(getResources().getString(R.string.dialog_clear_data_message_clears)+".\n"+datasetName, "");
					
					//Clear Dataset
					try { DBDataPrices.dropData(MainLibrary.StringToDate(datasetName)); }
					catch (Exception e) { clearErrors =  e.getMessage(); break; }
					
					//Uncheck the file
					publishProgress(getResources().getString(R.string.dialog_clear_data_message_clears)+".\n"+datasetName, String.valueOf(i));
				}
			} 
			catch (Exception e) 
			{
				clearErrors = e.getMessage();
			}
			return null;
		}

		//..........................................................................................
	    @Override
	    protected void onProgressUpdate(String... values) 
	    {
	      super.onProgressUpdate(values);
	      
	      //Set progress information
	      if (!values[0].equals("")) text_clear_message.setText(values[0]);
	      if (!values[1].equals("")) list_dataset.setItemChecked(Integer.parseInt(values[1]), false);
	    }

		//..........................................................................................
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			
			//Refresh data list
			initializeList();
			
			//Enable views
			button_close.setEnabled(true);
			button_clear.setEnabled(true);
			list_dataset.setEnabled(true);
			progress_dialog_clear.setVisibility(View.INVISIBLE);
			
			//Set message
			if (clearErrors.equals(""))
			{
				button_clear.setEnabled(false);
				text_clear_message.setText(R.string.dialog_clear_data_message_cleared);
				text_clear_message.setTextColor(Color.BLUE);
			}
			else
			{
				text_clear_message.setText(R.string.dialog_clear_data_message_cleared_err);
				text_clear_message.setTextColor(Color.RED);

				//Alert error explanation
				new AlertDialog.Builder(this_link)
		        .setTitle(R.string.dialog_error)
		        .setMessage(clearErrors)
		        .setPositiveButton(R.string.dialog_ok, null).create().show();
			}
		}
		//..........................................................................................
	}
	//----------------------------------------------------------------------------------------------
}
