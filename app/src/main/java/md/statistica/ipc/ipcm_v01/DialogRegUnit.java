package md.statistica.ipc.ipcm_v01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class DialogRegUnit extends Activity 
{
	//View variables
	Activity	this_link;
	TextView	filter_indicator;
	ListView	list_units;
	RadioButton	radio_filter_active;
	RadioButton	radio_filter_remained;
	RadioButton	radio_filter_errors;
	RadioButton	radio_filter_all;
	CheckBox	check_sort_name;
	ImageButton	button_find;
	
	//Data variables
	DBRegUnits	regUnits;
	boolean		check_first_time;
	
	//Dialog activities identifiers
	public final int    ACTIVITY_RESULT_REGUNIT_FIND = 1;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_regunit);
		
		//Initialize views .........................................................................
		this_link				= this;
		filter_indicator		= (TextView)	findViewById(R.id.text_dialog_regunit_filter_indicator);
		list_units				= (ListView)	findViewById(R.id.list_dialog_regunit_items);
		radio_filter_active		= (RadioButton)	findViewById(R.id.radio_dialog_regunit_filter_active);
		radio_filter_remained	= (RadioButton)	findViewById(R.id.radio_dialog_regunit_filter_remained);
		radio_filter_errors		= (RadioButton)	findViewById(R.id.radio_dialog_regunit_filter_errors);
		radio_filter_all		= (RadioButton)	findViewById(R.id.radio_dialog_regunit_filter_all);
		check_sort_name			= (CheckBox)	findViewById(R.id.check_dialog_regunit_sort);
		button_find				= (ImageButton)	findViewById(R.id.button_dialog_regunit_find);
		
		radio_filter_active.setOnCheckedChangeListener(RadioCheck);
		radio_filter_remained.setOnCheckedChangeListener(RadioCheck);
		radio_filter_errors.setOnCheckedChangeListener(RadioCheck);
		radio_filter_all.setOnCheckedChangeListener(RadioCheck);
		check_sort_name.setOnCheckedChangeListener(SortCheck);
		button_find.setOnClickListener(FindClick);

		//Initialize data ..........................................................................
		regUnits = new DBRegUnits();
		
		//Set sort variable ........................................................................
		check_first_time = true;
		if (DBUserInfo.sortRegUnit == DBRegUnits.SORT_BY_NAME) check_sort_name.setChecked(true);
		else check_sort_name.setChecked(false);
		check_first_time=false;
		
		//Select specific filter, which afterwards loads data ......................................
		if (DBUserInfo.filterRegUnit == DBRegUnits.FILTER_ACTIVE) radio_filter_active.setChecked(true);
		else if (DBUserInfo.filterRegUnit == DBRegUnits.FILTER_REMAINED) radio_filter_remained.setChecked(true);
		else if (DBUserInfo.filterRegUnit == DBRegUnits.FILTER_ERRORS) radio_filter_errors.setChecked(true);
		else if (DBUserInfo.filterRegUnit == DBRegUnits.FILTER_ALL) radio_filter_all.setChecked(true);
		else radio_filter_active.setChecked(true);
	}

	//----------------------------------------------------------------------------------------------
	//On return from the activity for result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ACTIVITY_RESULT_REGUNIT_FIND && resultCode == RESULT_OK && data != null) 
		{
			findItemResult(data.getStringExtra("code"), data.getStringExtra("name"));
		}
	}

	//----------------------------------------------------------------------------------------------
	//Find OnClick Listener
	OnClickListener FindClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			Intent intent = new Intent(this_link, DialogRegUnitFind.class);
			startActivityForResult(intent, ACTIVITY_RESULT_REGUNIT_FIND);
		}
	};

	//----------------------------------------------------------------------------------------------
	//Radio buttons OnClick Listener
	OnCheckedChangeListener RadioCheck = new OnCheckedChangeListener() 
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		{
			//Change user settings, after check is applied
			if (radio_filter_remained.isChecked()) 
			{
				DBUserInfo.setRegUnitFilter(DBRegUnits.FILTER_REMAINED);
				filter_indicator.setBackgroundResource(android.R.color.holo_blue_dark);
			}
			else if (radio_filter_errors.isChecked()) 
			{
				DBUserInfo.setRegUnitFilter(DBRegUnits.FILTER_ERRORS);
				filter_indicator.setBackgroundResource(android.R.color.holo_red_dark);
			}
			else if (radio_filter_all.isChecked()) 
			{
				DBUserInfo.setRegUnitFilter(DBRegUnits.FILTER_ALL);
				filter_indicator.setBackgroundResource(android.R.color.holo_green_dark);
			}
			else 
			{
				DBUserInfo.setRegUnitFilter(DBRegUnits.FILTER_ACTIVE);
				filter_indicator.setBackgroundResource(android.R.color.transparent);
			}
			loadRegUnitList();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Check button OnClick Listener
	OnCheckedChangeListener SortCheck = new OnCheckedChangeListener() 
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		{
			//Change user settings, after check is applied
			if (check_sort_name.isChecked()) DBUserInfo.setRegUnitSort(DBRegUnits.SORT_BY_NAME);
			else DBUserInfo.setRegUnitSort(DBRegUnits.SORT_BY_CODE);
			if (check_first_time) check_first_time=false; else loadRegUnitList();
		}
	};
	
	//----------------------------------------------------------------------------------------------
	//Set focus on the current item
	private int setFocus()
	{
		//Get item
    	int positionSelected = 0;
    	DBRegUnits.ItemClass item = regUnits.getItemCurrent();
    	
    	//Focus item
    	if (item != null) 
    	{
    		positionSelected = item.position;
        	list_units.setSelection(positionSelected);
    	}
		
		//return positionSelected;
		return positionSelected;
	}

	//----------------------------------------------------------------------------------------------
	//Set focus on the item with a searching code and name
	private int findItemResult(String codeFind, String nameFind)
	{
		//Get item
    	int positionSelected = 0;
    	DBRegUnits.ItemClass item = regUnits.findItem(codeFind, nameFind);
    	
    	//Set focus and give message
		if (item != null)
		{
			//Set temporary selection
			DBUserInfo.foundCodeUnit = item.code;
			((DialogRegUnitListAdapter)list_units.getAdapter()).notifyDataSetChanged();
			
			//Go to position
			positionSelected = item.position;
			list_units.setSelection(positionSelected);
			
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.dialog_regunit_label_find_result_ok) + ": [" + item.code + "] " + item.name, Toast.LENGTH_LONG);
			toast.show();
		}
		else
		{
            //Set temporary selection
			DBUserInfo.foundCodeUnit = "";
            ((DialogRegUnitListAdapter)list_units.getAdapter()).notifyDataSetChanged();
            
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.dialog_regunit_label_find_result_no), Toast.LENGTH_LONG);
			toast.show();
		}
		
		//return positionSelected;
		return positionSelected;
	}
	
	//----------------------------------------------------------------------------------------------
	//Load statistical units in the list view
	private void loadRegUnitList()
	{
		//Load data
		DBUserInfo.foundCodeUnit = "";
		regUnits.loadData(DBUserInfo.filterRegUnit, DBUserInfo.sortRegUnit);
		
		//Set list view adapter and onClick Listener
		list_units.setAdapter(new DialogRegUnitListAdapter(this_link, regUnits.list));
		list_units.setOnItemClickListener(new OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) 
            {
            	//After click - change user settings
            	DBRegUnits.ItemClass unit = (DBRegUnits.ItemClass) (list_units.getItemAtPosition(position));
                DBUserInfo.setCurrentCodeUnit(unit.code);
                
                //Set activity result value
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                
                //Close the activity
                finish();
            }
 
        });
		
		//Select current unit
		setFocus();
	}
	//----------------------------------------------------------------------------------------------
}
