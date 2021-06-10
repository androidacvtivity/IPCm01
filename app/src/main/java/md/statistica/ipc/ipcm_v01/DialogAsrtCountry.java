package md.statistica.ipc.ipcm_v01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DialogAsrtCountry extends Activity
{
	//View variables
	Activity	this_link;
	EditText	edit_code;
	EditText	edit_name;
	ListView	list_items;
	CheckBox	check_sort_name;
	
	//Data variables
	DBClsCounties	items;
	boolean	check_first_time;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_asrt_country);
		
		//Initialize views .........................................................................
		this_link		= this;
		edit_code		= (EditText)	findViewById(R.id.edit_dialog_asrt_country_code);
		edit_name		= (EditText)	findViewById(R.id.edit_dialog_asrt_country_name);
		list_items		= (ListView)	findViewById(R.id.list_dialog_asrt_country);
		check_sort_name	= (CheckBox)	findViewById(R.id.check_dialog_asrt_contry_sort);

		edit_name.requestFocus();
		edit_code.addTextChangedListener(FilterChange);
		edit_name.addTextChangedListener(FilterChange);
		check_sort_name.setOnCheckedChangeListener(SortCheck);
		
		//Set sort variable ........................................................................
		check_first_time = true;
		if (DBUserInfo.sortCountry == DBClsCounties.SORT_BY_NAME) check_sort_name.setChecked(true);
		else check_sort_name.setChecked(false);
		check_first_time=false;

		//Initialize data ..........................................................................
		items = new DBClsCounties();
		loadList();
	}

	//----------------------------------------------------------------------------------------------
	//Filter text change Listener
	TextWatcher FilterChange = new TextWatcher()
	{
        public void afterTextChanged(Editable s) 
        {
        	items.loadData(DBUserInfo.sortProduct, edit_code.getText().toString(), edit_name.getText().toString());
        	((DialogAsrtCountryAdapter)list_items.getAdapter()).notifyDataSetChanged();
        }
        
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    };

	//----------------------------------------------------------------------------------------------
	//Check button OnClick Listener
	OnCheckedChangeListener SortCheck = new OnCheckedChangeListener() 
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		{
			//Change user settings, after check is applied
			if (check_sort_name.isChecked()) DBUserInfo.setCountrySort(DBClsCounties.SORT_BY_NAME);
			else DBUserInfo.setCountrySort(DBClsCounties.SORT_BY_CODE);
			if (check_first_time) check_first_time=false; else loadList();
		}
	};
	
	//----------------------------------------------------------------------------------------------
	//Load list view
	private void loadList()
	{
		items.loadData(DBUserInfo.sortCountry);
		
		//Set list view adapter and onClick Listener
		list_items.setAdapter(new DialogAsrtCountryAdapter(this_link, items.list));
		list_items.setOnItemClickListener(new OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) 
            {
            	//After click - change user settings
            	DBClsCounties.ItemClass items = (DBClsCounties.ItemClass) (list_items.getItemAtPosition(position));
                
                //Set activity result value
                Intent intent = new Intent();
                intent.putExtra("code", items.code);
                setResult(RESULT_OK, intent);
                
                //Close the activity
                finish();
            }
 
        });
		
		//Select current item
		Intent intent = getIntent();
		DBClsCounties.ItemClass item = items.findItem(intent.getStringExtra("code"));
		if (item != null) list_items.setSelection(item.position);
	}
	//----------------------------------------------------------------------------------------------
}
