package md.statistica.ipc.ipcm_v01;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	//----------------------------------------------------------------------------------------------
	//Main views objects
	Activity		this_link;
	TextView		filter_indicator;
	Button			button_statunit;
	ImageButton		button_find_prod;
	ListView		list_product;
	
	RadioButton		radio_filter_active;
	RadioButton		radio_filter_remained;
	RadioButton		radio_filter_errors;
	Spinner			spinner_sort;
	
	Button			keyboard_1;
	Button			keyboard_2;
	Button			keyboard_3;
	Button			keyboard_4;
	Button			keyboard_5;
	Button			keyboard_6;
	Button			keyboard_7;
	Button			keyboard_8;
	Button			keyboard_9;
	Button			keyboard_0;
	Button			keyboard_dot;
	Button			keyboard_back;
	
	ImageButton		keyboard_submit;
	ImageButton		keyboard_cancel;
	ImageButton		keyboard_comment;
	ImageButton		keyboard_up;
	ImageButton		keyboard_down;
	ImageButton		keyboard_menu;

	TextView		text_reg_date;
	TextView		text_username;
	
	//Data variables
	DBDataPrices	dataPrices;
	
	//Dialog activities identifiers
	public static final int id_text_current_prod_price = 999999;
	public static final int ACTIVITY_RESULT_IMPORT = 1;
	public static final int ACTIVITY_RESULT_AUTHORIZE = 2;
	public static final int ACTIVITY_RESULT_REGUNIT = 3;
	public static final int ACTIVITY_RESULT_MENU = 4;
	public static final int ACTIVITY_RESULT_PERIOD = 5;
	public static final int ACTIVITY_RESULT_FIND = 6;
	public static final int ACTIVITY_RESULT_COMMENT = 7;
	public static final int ACTIVITY_RESULT_ASSORT_ADD = 8;
	public static final int ACTIVITY_RESULT_ASSORT_MOD = 9;
	public static final int ACTIVITY_RESULT_CLEAR_DATA = 10;

	//----------------------------------------------------------------------------------------------
	//Main activity initialization
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		//Initialize views .........................................................................
		this_link					= this;
		filter_indicator			= (TextView)	findViewById(R.id.text_main_filter_indicator);
		button_statunit				= (Button)		findViewById(R.id.button_statunit);
		button_find_prod			= (ImageButton)	findViewById(R.id.button_find_prod);
		list_product				= (ListView)	findViewById(R.id.list_product);
		
		radio_filter_active			= (RadioButton)	findViewById(R.id.radio_main_filter_active);
		radio_filter_remained		= (RadioButton)	findViewById(R.id.radio_main_filter_remained);
		radio_filter_errors			= (RadioButton)	findViewById(R.id.radio_main_filter_errors);
		spinner_sort				= (Spinner)		findViewById(R.id.spinner_main_sort);
		
		keyboard_1					= (Button)		findViewById(R.id.keyboard_1);   
		keyboard_2					= (Button)		findViewById(R.id.keyboard_2);   
		keyboard_3					= (Button)		findViewById(R.id.keyboard_3);   
		keyboard_4					= (Button)		findViewById(R.id.keyboard_4);   
		keyboard_5					= (Button)		findViewById(R.id.keyboard_5);   
		keyboard_6					= (Button)		findViewById(R.id.keyboard_6);   
		keyboard_7					= (Button)		findViewById(R.id.keyboard_7);   
		keyboard_8					= (Button)		findViewById(R.id.keyboard_8);   
		keyboard_9					= (Button)		findViewById(R.id.keyboard_9);   
		keyboard_0					= (Button)		findViewById(R.id.keyboard_0);   
		keyboard_dot				= (Button)		findViewById(R.id.keyboard_dot); 
		keyboard_back				= (Button)		findViewById(R.id.keyboard_back);
		
		keyboard_submit				= (ImageButton)	findViewById(R.id.keyboard_submit);
		keyboard_cancel				= (ImageButton)	findViewById(R.id.keyboard_cancel);
		keyboard_comment			= (ImageButton)	findViewById(R.id.keyboard_comment);
		keyboard_up					= (ImageButton)	findViewById(R.id.keyboard_up);
		keyboard_down				= (ImageButton)	findViewById(R.id.keyboard_down);
		keyboard_menu				= (ImageButton)	findViewById(R.id.keyboard_menu);

		text_reg_date				= (TextView)	findViewById(R.id.text_reg_date);
		text_username				= (TextView)	findViewById(R.id.text_username);

		//Initialize data ..........................................................................
		dataPrices = new DBDataPrices();

		//Initialize listeners .....................................................................
		button_statunit.setOnClickListener(StatUnitClick);
		button_find_prod.setOnClickListener(FindProdClick);

		radio_filter_active.setOnCheckedChangeListener(RadioCheck);
		radio_filter_remained.setOnCheckedChangeListener(RadioCheck);
		radio_filter_errors.setOnCheckedChangeListener(RadioCheck);
		spinner_sort.setOnItemSelectedListener(SpinnerClick);
		
		keyboard_1.setOnClickListener(KeyboardDigitClick);
		keyboard_2.setOnClickListener(KeyboardDigitClick);
		keyboard_3.setOnClickListener(KeyboardDigitClick);
		keyboard_4.setOnClickListener(KeyboardDigitClick);
		keyboard_5.setOnClickListener(KeyboardDigitClick);
		keyboard_6.setOnClickListener(KeyboardDigitClick);
		keyboard_7.setOnClickListener(KeyboardDigitClick);
		keyboard_8.setOnClickListener(KeyboardDigitClick);
		keyboard_9.setOnClickListener(KeyboardDigitClick);
		keyboard_0.setOnClickListener(KeyboardDigitClick);
		keyboard_dot.setOnClickListener(KeyboardDigitClick);
		keyboard_back.setOnClickListener(KeyboardDigitClick);
		
		keyboard_submit.setOnClickListener(KeyboardAdditionalClick);
		keyboard_cancel.setOnClickListener(KeyboardAdditionalClick);
		keyboard_comment.setOnClickListener(KeyboardAdditionalClick);
		keyboard_up.setOnClickListener(KeyboardAdditionalClick);
		keyboard_down.setOnClickListener(KeyboardAdditionalClick);
		keyboard_menu.setOnClickListener(KeyboardAdditionalClick);
		
		//Open database connection .................................................................
		Database.open(this);
		
		//Initialize user information ..............................................................
		DBUserInfo.initialize();
		
		//Select specific filter, which afterwards loads data ......................................
		if (DBUserInfo.filterDataPrice == DBDataPrices.FILTER_ALL) radio_filter_active.setChecked(true);
		else if (DBUserInfo.filterDataPrice == DBDataPrices.FILTER_REMAINED) radio_filter_remained.setChecked(true);
		else if (DBUserInfo.filterDataPrice == DBDataPrices.FILTER_ERRORS) radio_filter_errors.setChecked(true);
		else radio_filter_active.setChecked(true);
		
		//Set sort variable ........................................................................
		ArrayAdapter<CharSequence> spinner_sort_adapter = ArrayAdapter.createFromResource(this, R.array.main_sort_title_list, android.R.layout.simple_spinner_item);
		spinner_sort_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_sort.setPromptId(R.string.main_sort_title);
		spinner_sort.setAdapter(spinner_sort_adapter);
		
		spinner_sort.setSelection(0);
		if (DBUserInfo.sortDataPrice == DBDataPrices.SORT_BY_DATE) spinner_sort.setSelection(1);
		else if (DBUserInfo.sortDataPrice == DBDataPrices.SORT_BY_NAME) spinner_sort.setSelection(2);
		
		//Check database and import data or authorize user .........................................
		if (DBUserInfo.idUser == 0) importDataActivity(); else if (!DBUserInfo.isAuthorized) authorizeUserActivity();
	}

	//----------------------------------------------------------------------------------------------
	//Result on return from any activity called for result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ACTIVITY_RESULT_AUTHORIZE && !DBUserInfo.isAuthorized) finish();
		if (requestCode == ACTIVITY_RESULT_IMPORT && !DBUserInfo.isAuthorized) importDataResult();
		if (requestCode == ACTIVITY_RESULT_PERIOD && resultCode == RESULT_OK) periodResult(data);
		if (requestCode == ACTIVITY_RESULT_COMMENT && resultCode == RESULT_OK) commentActivityResult(data);
		if (requestCode == ACTIVITY_RESULT_FIND && resultCode == RESULT_OK) findDataPriceActivityResult(data);
		if (requestCode == ACTIVITY_RESULT_REGUNIT && resultCode == RESULT_OK) loadDataUnit();
		if (requestCode == ACTIVITY_RESULT_ASSORT_ADD && resultCode == RESULT_OK) loadDataUnit();
		if (requestCode == ACTIVITY_RESULT_ASSORT_MOD && resultCode == RESULT_OK) loadDataUnit();
		if (requestCode == ACTIVITY_RESULT_CLEAR_DATA && resultCode == RESULT_OK) loadDataUnit();
		if (requestCode == ACTIVITY_RESULT_MENU && resultCode == RESULT_OK && data != null)
		{
			int idMenuItem = data.getIntExtra("itemPressedId", 0);
			if (idMenuItem == R.id.button_dialog_menu_clear_data) dataClearActivity();
			if (idMenuItem == R.id.button_dialog_menu_assort_add) assortAddActivity();
			if (idMenuItem == R.id.button_dialog_menu_assort_mod) assortModActivity();
			if (idMenuItem == R.id.button_dialog_menu_assort_del) assortDelActivity();
			if (idMenuItem == R.id.button_dialog_menu_period) periodActivity();
			if (idMenuItem == R.id.button_dialog_menu_import) importDataActivity();
			if (idMenuItem == R.id.button_dialog_menu_export) exportDataActivity();
			if (idMenuItem == R.id.button_dialog_menu_exit) finish();
		}
	}

	//----------------------------------------------------------------------------------------------
	//Keyboard-additional OnClick Listener
	OnClickListener KeyboardAdditionalClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			if (!DBUserInfo.isAuthorized) return;
			
			if (v.getId() == R.id.keyboard_submit) dataPriceEditSubmit();
			if (v.getId() == R.id.keyboard_cancel) dataPriceEditCancel();
			if (v.getId() == R.id.keyboard_comment) commentActivity();
			if (v.getId() == R.id.keyboard_up) focusDataPrice(-1);
			if (v.getId() == R.id.keyboard_down) focusDataPrice(1);
			if (v.getId() == R.id.keyboard_menu) startActivityForResult(new Intent(this_link, DialogMenu.class), ACTIVITY_RESULT_MENU);
		}
	};

	//----------------------------------------------------------------------------------------------
	//Statistical unit OnClick Listener
	OnClickListener StatUnitClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			if (!DBUserInfo.isAuthorized) return;
			
			Intent intent = new Intent(this_link, DialogRegUnit.class);
			startActivityForResult(intent, ACTIVITY_RESULT_REGUNIT);
		}
	};

	//----------------------------------------------------------------------------------------------
	//Find product OnClick Listener
	OnClickListener FindProdClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			if (!DBUserInfo.isAuthorized) return;
			findDataPriceActivity();
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
				if (DBUserInfo.isAuthorized) DBUserInfo.setDataPriceFilter(DBDataPrices.FILTER_REMAINED);
				filter_indicator.setBackgroundResource(android.R.color.holo_blue_dark);
			}
			else if (radio_filter_errors.isChecked()) 
			{
				if (DBUserInfo.isAuthorized) DBUserInfo.setDataPriceFilter(DBDataPrices.FILTER_ERRORS);
				filter_indicator.setBackgroundResource(android.R.color.holo_red_dark);
			}
			else 
			{
				if (DBUserInfo.isAuthorized) DBUserInfo.setDataPriceFilter(DBDataPrices.FILTER_ALL);
				filter_indicator.setBackgroundResource(android.R.color.transparent);
			}
			if (DBUserInfo.isAuthorized) loadDataPrices();
		}
	};
	
	//----------------------------------------------------------------------------------------------
	//Sort button OnItemSelected Listener
    OnItemSelectedListener SpinnerClick = new OnItemSelectedListener() 
    {
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    	{
    		if (pos == 1) DBUserInfo.setDataPriceSort(DBDataPrices.SORT_BY_DATE);
    	    else if (pos == 2) DBUserInfo.setDataPriceSort(DBDataPrices.SORT_BY_NAME);
    	    else DBUserInfo.setDataPriceSort(DBDataPrices.SORT_BY_CODE);
    	    loadDataPrices();
        }
    	
        @Override
        public void onNothingSelected(AdapterView<?> arg0)
        {
            //Obligatory but not useful for this case
        }
    };

	//----------------------------------------------------------------------------------------------
	//Keyboard-digit OnClick Listener
	private static boolean editMode = false;
	OnClickListener KeyboardDigitClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			//Check global variables
			if (!DBUserInfo.isAuthorized) return;
			if (DBUserInfo.currentCodeAstr.equals("0")) return;
			
			//Specify pressed button
			TextView text_current_prod_price = (TextView)findViewById(MainActivity.id_text_current_prod_price);
			if (text_current_prod_price == null || text_current_prod_price.getVisibility() != View.VISIBLE) return;
			
			//Handle pressed button
			String oldVal = text_current_prod_price.getText().toString();
			String addVal = ((Button) v).getText().toString();
			String newVal = oldVal;
			
			//Handle backspace
			if (!oldVal.equals("") && addVal.equals("<")) 
			{
				newVal = oldVal.substring(0, newVal.length()-1);
				if (newVal.equals("")) newVal = "0";
				editMode = true;
			}
			//Handle the rest of the keys
			else
			{
				if (oldVal.indexOf(".") == -1 || ((newVal.length() - oldVal.indexOf(".") <= 2) && !addVal.equals(".")))
				{
					if ((newVal.equals("0") && !addVal.equals(".")) || !editMode) newVal = addVal;
					else newVal = oldVal+addVal;
					editMode = true;
				}
			}
			
			//Set the result value
			text_current_prod_price.setText(newVal);
			
			//Activate submit button
			keyboard_submit.setEnabled(true);
			keyboard_cancel.setEnabled(true);
		}
	};

	//----------------------------------------------------------------------------------------------
	//Product OnClick Listener
	OnItemClickListener RpoductClick = new OnItemClickListener() 
    {
        @Override
        public void onItemClick(AdapterView<?> a, View v, int position, long id) 
        {
        	if (!DBUserInfo.isAuthorized) return;
            
        	//Get item
        	DBDataPrices.ItemClass item = (DBDataPrices.ItemClass) (list_product.getItemAtPosition(position));
        	
        	//Set settings
            DBUserInfo.setCurrentCodeProd(item.codeProd);
            DBUserInfo.setCurrentCodeAstr(item.codeAsrt);
			
			//Refresh adapter
            MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
            adapter.notifyDataSetChanged();
            
            //Repaint selected values
            onDataPriceSelected(item);
        }
    };
	
	//----------------------------------------------------------------------------------------------
	//Load statistical unit information
	private void loadDataUnit()
	{
		//Load unit information and find current unit
		DBRegUnits regUnits = new DBRegUnits();
		regUnits.loadData(DBRegUnits.FILTER_ALL, DBRegUnits.SORT_BY_CODE, DBUserInfo.currentCodeUnit);
		DBRegUnits.ItemClass regUnit = regUnits.getItem(DBUserInfo.currentCodeUnit);
		
		//Draw current unit information
		if (regUnit != null) button_statunit.setText("[" + regUnit.code + "] " + regUnit.name);
		else button_statunit.setText(R.string.main_default_unit_name);
		
		//Load price data
		loadDataPrices();
	}
	
	//----------------------------------------------------------------------------------------------
	//Load price information
	private void loadDataPrices()
	{
		//Load price data
		dataPrices.loadData(DBUserInfo.filterDataPrice, DBUserInfo.sortDataPrice);
		
		//Set list view adapter and onClick Listener
		list_product.setAdapter(new MainActivityListAdapter(this_link, dataPrices.list));
		list_product.setOnItemClickListener(RpoductClick);
		
		//Focus on the current product
    	DBDataPrices.ItemClass item = dataPrices.getItemCurrent();
		focusDataPrice(item);
		editMode = false;
	}
	
	//----------------------------------------------------------------------------------------------
	//Set focus on the current item
	private void focusDataPrice(DBDataPrices.ItemClass item)
	{
    	//Focus item if it is not null
    	if (item != null) 
    	{
    		//Set settings
            DBUserInfo.setCurrentCodeProd(item.codeProd);
            DBUserInfo.setCurrentCodeAstr(item.codeAsrt);
            
            //Notify adapter
            MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
            adapter.notifyDataSetChanged();
            
    		//Focus comfortable position
    		int positionFocused = item.position;
    		if (!item.codeAsrt.equals("0"))
    		{
    			DBDataPrices.ItemClass itemProduct = dataPrices.getItem(item.codeUnit, item.codeProd, "0");
    			if (itemProduct != null) positionFocused = itemProduct.position;
    		}
    		list_product.setSelection(positionFocused);
    	}
    	
    	//Reset selected values (or clear if item is null)
		onDataPriceSelected(item);
	}
	
	//----------------------------------------------------------------------------------------------
	//Set focus assort item by direction: -1 (previous assortment), 1 (next assortment)
	private void focusDataPrice(int direction)
	{
		//Get item
    	DBDataPrices.ItemClass item = null;
    	if (direction == -1) item = dataPrices.getItemPrevAssort();
    	if (direction ==  1) item = dataPrices.getItemNextAssort();
		editMode = false;
    	
    	//Focus item
    	if (item != null)
    	{
        	//Set settings
            DBUserInfo.setCurrentCodeProd(item.codeProd);
            DBUserInfo.setCurrentCodeAstr(item.codeAsrt);
    		onDataPriceSelected(item);
            
            //Notify adapter
            MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
            adapter.notifyDataSetChanged();
            
    		//Focus comfortable position
    		int positionFocused = item.position;
    		if (!item.codeAsrt.equals("0") && direction == -1)
    		{
        		DBDataPrices.ItemClass itemProduct = dataPrices.getItem(item.codeUnit, item.codeProd, "0");
    			if (itemProduct != null) positionFocused = itemProduct.position;
    		}
    		list_product.smoothScrollToPosition(positionFocused);
    	}
		else
		{
			//List end message
			String tmpMsg = "";

			if (direction == -1) tmpMsg = getResources().getString(R.string.main_label_start_of_list);
			if (direction ==  1) tmpMsg = getResources().getString(R.string.main_label_end_of_list);
			
			Toast toast = Toast.makeText(getApplicationContext(), tmpMsg, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//Set focus on the item with a specific code
	private void onDataPriceSelected(DBDataPrices.ItemClass itemSelected)
	{
		boolean enabled = true;
    	if (itemSelected == null || itemSelected.codeAsrt.equals("0")) enabled = false;

		keyboard_0.setEnabled(enabled);
		keyboard_1.setEnabled(enabled);
		keyboard_2.setEnabled(enabled);
		keyboard_3.setEnabled(enabled);
		keyboard_4.setEnabled(enabled);
		keyboard_5.setEnabled(enabled);
		keyboard_6.setEnabled(enabled);
		keyboard_7.setEnabled(enabled);
		keyboard_8.setEnabled(enabled);
		keyboard_9.setEnabled(enabled);
		keyboard_dot.setEnabled(enabled);
		keyboard_back.setEnabled(enabled);
		keyboard_submit.setEnabled(false);
		keyboard_cancel.setEnabled(false);
		keyboard_comment.setEnabled(enabled);
		editMode = false;
	}
	
	//----------------------------------------------------------------------------------------------
	//Authorization activity
	private void authorizeUserActivity()
	{
		//Ask login if not authorizes
		if (!DBUserInfo.isAuthorized)
		{
			Intent intent = new Intent(this, DialogAuthorize.class);
			startActivityForResult(intent, ACTIVITY_RESULT_AUTHORIZE);
		}
		
		//Draw login information
		text_reg_date.setText(MainLibrary.DateToString(DBUserInfo.setDate));
		text_username.setText(DBUserInfo.getNameFull());
		
		//Load unit data
		loadDataUnit();
	}
	
	//----------------------------------------------------------------------------------------------
	//Import data activity (from XML-files)
	private void importDataActivity()
	{
		Intent intent = new Intent(this, DialogImport.class);
		startActivityForResult(intent, ACTIVITY_RESULT_IMPORT);
	}
	
	//----------------------------------------------------------------------------------------------
	//Import data activity result (from XML-files)
	private void importDataResult()
	{
		if (DBUserInfo.idUser == 0)
		{
			//Close application
			new AlertDialog.Builder(this_link)
			.setCancelable(false)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.main_dialog_undef_user_message)
	        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() 
	        {
	        	public void onClick(DialogInterface arg0, int arg1) 
	        	{
	        		finish();
	        	} 
	        }).create().show();
		}
		else
		{
			//Authorize user
			authorizeUserActivity();
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//Export data activity (to XML-files)
	private void exportDataActivity()
	{
		Intent intent = new Intent(this, DialogExport.class);
		startActivity(intent);
	}
	
	//----------------------------------------------------------------------------------------------
	//Period activity
	private void periodActivity()
	{
		Intent intent = new Intent(this, DialogPeriod.class);
		startActivityForResult(intent, ACTIVITY_RESULT_PERIOD);
	}
	
	//----------------------------------------------------------------------------------------------
	//Period activity result
	private void periodResult(Intent data)
	{
		if (data == null) return;
		
		//Format new date
		Date setDate = new Date();
		setDate = MainLibrary.StringToDate(data.getIntExtra("decade", 0)+"."+data.getIntExtra("month", 0)+"."+data.getIntExtra("year", 0));
		
		//Set new date
		DBUserInfo.setDateSet(setDate);
		text_reg_date.setText(MainLibrary.DateToString(DBUserInfo.setDate));
		
		//Reload data
		loadDataUnit();
		
		//Check new period for data
		if (dataPrices.existData(MainLibrary.DateAddMonth(DBUserInfo.setDate, -1)) && !dataPrices.existData(DBUserInfo.setDate))
		{
			new AlertDialog.Builder(this_link)
			.setCancelable(false)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.main_label_period_copy_previous_period)
	        .setNegativeButton(R.string.dialog_no, null)
	        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() 
	        {
	        	public void onClick(DialogInterface arg0, int arg1) 
	        	{
	        		//Copy data from previous period
	        		dataPrices.copyPreviousData(MainLibrary.DateAddMonth(DBUserInfo.setDate, -1), DBUserInfo.setDate);
	        		
	        		//Reload data
	        		loadDataUnit();
	        	} 
	        }).create().show();
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//Find price
	private void findDataPriceActivity()
	{
		Intent intent = new Intent(this_link, DialogRegUnitFind.class);
		startActivityForResult(intent, ACTIVITY_RESULT_FIND);
	}
	
	//----------------------------------------------------------------------------------------------
	//Period activity result
	private void findDataPriceActivityResult(Intent data)
	{
		if (data == null) return;
		
		//Get search data
		String codeFind = data.getStringExtra("code");
		String nameFind = data.getStringExtra("name");
    	DBDataPrices.ItemClass item = dataPrices.findItem(codeFind, nameFind);
    	
    	//Set focus and give message
		if (item != null)
		{
			focusDataPrice(item);
			
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.main_label_find_result_ok) + ": [" + item.codeProd + "] " + item.name, Toast.LENGTH_LONG);
			toast.show();
		}
		else
		{
			Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.main_label_find_result_no), Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	//----------------------------------------------------------------------------------------------
	//Clear data activity
	private void dataClearActivity()
	{
		Intent intent = new Intent(this_link, DialogClearData.class);
		startActivityForResult(intent, ACTIVITY_RESULT_CLEAR_DATA);
	}
	
	//----------------------------------------------------------------------------------------------
	//Add assortment activity
	private void assortAddActivity()
	{
		Intent intent = new Intent(this_link, DialogAssortment.class);
		intent.putExtra("codeUnit",  DBUserInfo.currentCodeUnit);
		intent.putExtra("codeProd",  DBUserInfo.currentCodeProd);
		intent.putExtra("codeAsrt",  "0");
		startActivityForResult(intent, ACTIVITY_RESULT_ASSORT_ADD);
	}
	
	//----------------------------------------------------------------------------------------------
	//Modification assortment activity
	private void assortModActivity()
	{
		DBDataPrices.ItemClass item = dataPrices.getItemCurrent();
		if (item == null || item.codeAsrt.equals("0"))
		{
			new AlertDialog.Builder(this_link)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.main_label_assortment_mod_no_item)
	        .setPositiveButton(R.string.dialog_ok, null).create().show();
			return;
		}
		
		Intent intent = new Intent(this_link, DialogAssortment.class);
		intent.putExtra("codeUnit",  item.codeUnit);
		intent.putExtra("codeProd",  item.codeProd);
		intent.putExtra("codeAsrt",  item.codeAsrt);
		startActivityForResult(intent, ACTIVITY_RESULT_ASSORT_MOD);
	}
	
	//----------------------------------------------------------------------------------------------
	//Delete assortment activity
	private void assortDelActivity()
	{
		//Get current assortment
		DBDataPrices.ItemClass item = dataPrices.getItemCurrent();
		if (item == null || item.codeAsrt.equals("0")) 
		{
			new AlertDialog.Builder(this_link)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.main_label_assortment_delete_no_item)
	        .setPositiveButton(R.string.dialog_ok, null).create().show();
			return;
		}
		
		//Check if assortment has data (24.10.2013: Statisticians refused from this check-rule)
		/*if (item.price_C1 != 0 || item.price_C2 != 0 || item.price_C3 != 0 || item.price_P1 != 0 || item.price_P2 != 0 || item.price_P3 != 0)
		{
			new AlertDialog.Builder(this_link)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.main_label_assortment_delete_has_data)
	        .setPositiveButton(R.string.dialog_ok, null).create().show();
			return;
		}*/

		//Confirm delete procedure
		new AlertDialog.Builder(this_link)
        .setTitle(R.string.dialog_attention)
        .setMessage(R.string.main_label_assortment_delete_confirm)
        .setNegativeButton(R.string.dialog_no, null)
        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() 
        {
        	public void onClick(DialogInterface arg0, int arg1) 
        	{
        		//Get current item
        		DBDataPrices.ItemClass itemPrev = dataPrices.getItemPrevAssort();
        		DBDataPrices.ItemClass item = dataPrices.getItemCurrent();
        		if (item == null || item.codeAsrt.equals("0")) return;
        		
        		//Delete current item
        		dataPrices.deleteItem(item);
        		focusDataPrice(itemPrev);
        		
                //Notify adapter
                MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
                adapter.notifyDataSetChanged();
        		editMode = false;
        	} 
        }).create().show();
	}
	
	//----------------------------------------------------------------------------------------------
	//Comment price activity
	private void commentActivity()
	{
		//Comment is needed only for the product level
		DBDataPrices.ItemClass item = dataPrices.getItemCurrentProd();
		if (item == null)
		{
			new AlertDialog.Builder(this_link)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.main_label_comment_no_item)
	        .setPositiveButton(R.string.dialog_ok, null).create().show();
			return;
		}
		
		Intent intent = new Intent(this_link, DialogComment.class);
		intent.putExtra("idDataStatus", (int)item.idDataStatus);
		intent.putExtra("textualComment", item.textualComment);
		startActivityForResult(intent, ACTIVITY_RESULT_COMMENT);
	}
	
	//----------------------------------------------------------------------------------------------
	//Comment price activity result
	private void commentActivityResult(Intent data)
	{
		if (data == null) return;
		
		//Get activity data
		int idDataStatus = data.getIntExtra("idDataStatus", 0);
		String textualComment = data.getStringExtra("textualComment");
		
		//Get product item and save comment
		DBDataPrices.ItemClass item = dataPrices.getItemCurrentProd();
		dataPrices.setItemComment(item, idDataStatus, textualComment);
		
		//Refresh adapter
        MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
        adapter.notifyDataSetChanged();
		editMode = false;
	}
	
	//----------------------------------------------------------------------------------------------
	//Cancel editing the price data 
	private void dataPriceEditCancel()
	{
		//Deactivate submit button
		keyboard_submit.setEnabled(false);
		keyboard_cancel.setEnabled(false);
		
        //Notify adapter
        MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
        adapter.notifyDataSetChanged();
        editMode = false;
	}
	
	//----------------------------------------------------------------------------------------------
	//Submit editing the price data 
	private void dataPriceEditSubmit()
	{
		//Get current view
		TextView text_current_prod_price = (TextView)findViewById(MainActivity.id_text_current_prod_price);
		if (text_current_prod_price == null || text_current_prod_price.getVisibility() != View.VISIBLE) return;
		
		//Get current item
		DBDataPrices.ItemClass item = dataPrices.getItemCurrent();
		if (text_current_prod_price == null || item == null || item.codeAsrt.equals("0")) return;
		
		//Set item price
		dataPrices.setItemPrice(item, Double.valueOf(text_current_prod_price.getText().toString()));
		
		//Deactivate submit button
		keyboard_submit.setEnabled(false);
		keyboard_cancel.setEnabled(false);
		
		//Refresh adapter
        MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
        adapter.notifyDataSetChanged();
		editMode = false;
        
        //Give a message
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.main_label_save_result_ok), Toast.LENGTH_SHORT);
		toast.show();
		
		//Check the price index of the product and change price status if necessary
		DBDataPrices.ItemClass itemProd = dataPrices.getItemCurrentProd();
		if (itemProd == null) return;
		
		if (itemProd.isHighVariaty())
		{
			new AlertDialog.Builder(this_link)
	        .setTitle(R.string.dialog_attention)
	        .setMessage(R.string.main_label_save_result_big_variaty)
	        .setNegativeButton(R.string.dialog_no, null)
	        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() 
	        {
	        	public void onClick(DialogInterface arg0, int arg1) 
	        	{
	        		//Get current product item
	        		DBDataPrices.ItemClass itemProd = dataPrices.getItemCurrentProd();
	        		if (itemProd == null) return;

	        		//Set product item status
	        		dataPrices.setItemComment(itemProd, 6, itemProd.textualComment);
	        		
	        		//Refresh adapter
	                MainActivityListAdapter adapter = (MainActivityListAdapter)list_product.getAdapter();
	                adapter.notifyDataSetChanged();
	        	} 
	        }).create().show();
		}
	}
	//----------------------------------------------------------------------------------------------
}
